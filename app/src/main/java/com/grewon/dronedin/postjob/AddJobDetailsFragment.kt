package com.grewon.dronedin.postjob

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseFragment
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.dialogs.AlertViewDialog
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.FileValidationUtils
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.mapscreen.MapScreenActivity
import com.grewon.dronedin.server.CreateMilestoneBean
import com.grewon.dronedin.milestone.adapter.CreateMileStoneAdapter
import com.grewon.dronedin.postjob.contract.JobPostContract
import com.grewon.dronedin.server.CreateJobsBean
import com.grewon.dronedin.server.LocationBean
import com.grewon.dronedin.server.params.CreateJobsParams
import com.grewon.dronedin.server.params.UploadAttachmentsParams
import com.grewon.dronedin.attachments.UploadAttachmentsAdapter
import com.grewon.dronedin.utils.ValidationUtils
import com.theartofdev.edmodo.cropper.CropImage
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.file_bottom_dialog.view.*
import kotlinx.android.synthetic.main.fragment_add_job_details.*
import retrofit2.Retrofit

import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class AddJobDetailsFragment : BaseFragment(), View.OnClickListener,
    UploadAttachmentsAdapter.OnItemLongClickListeners, JobPostContract.View {

    @Inject
    lateinit var postJobPresenter: JobPostContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    private var alertDialog: AlertViewDialog? = null
    private var createMileStoneAdapter: CreateMileStoneAdapter? = null
    private var jobsImageAdapter: UploadAttachmentsAdapter? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var locationAddress: String = ""
    private var picturePath: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_job_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setClicks()
        initView()

    }

    private fun setData() {
        if (!ValidationUtils.isEmptyFiled((activity as PostJobActivity).createJobsParams?.jobTitle.toString())) {
            edt_title.setText((activity as PostJobActivity).createJobsParams?.jobTitle.toString())
        }

        if (!ValidationUtils.isEmptyFiled((activity as PostJobActivity).createJobsParams?.jobDescription.toString())) {
            edt_description.setText((activity as PostJobActivity).createJobsParams?.jobDescription.toString())
        }

        if (!ValidationUtils.isEmptyFiled((activity as PostJobActivity).createJobsParams?.jobAddress.toString())) {
            txt_location.text =
                (activity as PostJobActivity).createJobsParams?.jobAddress.toString()
            locationAddress = (activity as PostJobActivity).createJobsParams?.jobAddress.toString()
            latitude = (activity as PostJobActivity).createJobsParams?.jobLatitude!!
            longitude = (activity as PostJobActivity).createJobsParams?.jobLongitude!!
        }

        if (!ValidationUtils.isEmptyFiled((activity as PostJobActivity).createJobsParams?.jobTotalPrice.toString())) {
            edt_price.setText((activity as PostJobActivity).createJobsParams?.jobTotalPrice.toString())
        }

        if ((activity as PostJobActivity).createJobsParams?.attachments != null) {
            jobsImageAdapter?.addItemsList((activity as PostJobActivity).createJobsParams?.attachments!!)
        }

        if ((activity as PostJobActivity).createJobsParams?.mileStones != null) {
            createMileStoneAdapter?.addItemsList((activity as PostJobActivity).createJobsParams?.mileStones!!)
        }

    }

    private fun initView() {
        DroneDinApp.getAppInstance().getAppComponent().inject(this)

        postJobPresenter.attachView(this)
        postJobPresenter.attachApiInterface(retrofit)


        milestone_recycle.layoutManager = LinearLayoutManager(context)
        createMileStoneAdapter = CreateMileStoneAdapter(requireContext())
        milestone_recycle.adapter = createMileStoneAdapter
        setImageAdapter()

        setData()

    }

    private fun setImageAdapter() {
        image_recycle.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        jobsImageAdapter = UploadAttachmentsAdapter(requireContext(), this)
        image_recycle.adapter = jobsImageAdapter

    }

    private fun setClicks() {
        im_add_milestone.setOnClickListener(this)
        im_add_attachments.setOnClickListener(this)
        txt_location.setOnClickListener(this)
        txt_submit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.im_add_milestone -> {
                when {
                    ValidationUtils.isEmptyFiled(edt_milestone_price.text.toString()) -> {
                        DroneDinApp.getAppInstance()
                            .showToast(getString(R.string.please_enter_milestone_price))
                    }
                    ValidationUtils.isEmptyFiled(edt_milestone_description.text.toString()) -> {
                        DroneDinApp.getAppInstance()
                            .showToast(getString(R.string.please_enter_milestone_description))
                    }
                    else -> {


                        createMileStoneAdapter?.addItems(
                            CreateMilestoneBean(
                                edt_milestone_description.text.toString(),
                                edt_milestone_price.text.toString()
                            )
                        )
                        edt_milestone_price.setText("")
                        edt_milestone_description.setText("")
                    }
                }
            }
            R.id.im_add_attachments -> {
                openFileDialog()
            }
            R.id.txt_location -> {
                passIntent()
            }

            R.id.txt_submit -> {
                if (ValidationUtils.isEmptyFiled(edt_title.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_enter_job_title))
                } else if (ValidationUtils.isEmptyFiled(edt_description.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_enter_job_description))
                } else if (ValidationUtils.isEmptyFiled(txt_location.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_select_job_location))
                } else if (ValidationUtils.isEmptyFiled(edt_price.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_enter_total_project_price))
                } else if (createMileStoneAdapter != null && createMileStoneAdapter!!.itemList.size == 0) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_create_milestone))
                } else {
                    (activity as PostJobActivity).createJobsParams?.jobTitle =
                        edt_title.text.toString()
                    (activity as PostJobActivity).createJobsParams?.jobDescription =
                        edt_description.text.toString()
                    (activity as PostJobActivity).createJobsParams?.jobAddress = locationAddress
                    (activity as PostJobActivity).createJobsParams?.jobLatitude = latitude
                    (activity as PostJobActivity).createJobsParams?.jobLongitude = longitude
                    (activity as PostJobActivity).createJobsParams?.jobTotalPrice =
                        edt_price.text.toString()
                    (activity as PostJobActivity).createJobsParams?.attachments =
                        jobsImageAdapter?.itemList
                    (activity as PostJobActivity).createJobsParams?.mileStones =
                        createMileStoneAdapter?.itemList
                    postJobPresenter.postJob((activity as PostJobActivity).createJobsParams!!)
                }
            }
        }
    }

    private fun passIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    AppConstant.LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                val locationsBean = LocationBean(latitude, longitude, locationAddress)
                startActivityForResult(
                    Intent(
                        requireContext(),
                        MapScreenActivity::class.java
                    ).putExtra(AppConstant.LOCATION_BEAN, locationsBean),
                    AppConstant.ADD_LOCATION_REQUEST_CODE
                )
            }
        } else {
            val locationsBean = LocationBean(latitude, longitude, locationAddress)
            startActivityForResult(
                Intent(
                    requireContext(),
                    MapScreenActivity::class.java
                ).putExtra(AppConstant.LOCATION_BEAN, locationsBean),
                AppConstant.ADD_LOCATION_REQUEST_CODE
            )
        }

    }

    @SuppressLint("InflateParams")
    private fun openFileDialog() {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.file_bottom_dialog, null)
        val dialog = BottomSheetDialog(
            requireContext(), R.style.CustomBottomSheetDialogTheme
        )
        dialog.setContentView(view)

        val linearCamera = view.ll_camera
        val linearGallery = view.ll_gallery
        val linearFile = view.ll_file
        val textDialogTitle = view.txt_dialog_title


        textDialogTitle.setOnClickListener { dialog.dismiss() }

        linearCamera.setOnClickListener {

            dialog.dismiss()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        AppConstant.CAMERA_PERMISSION_REQUEST_CODE
                    )
                } else {
                    cameraIntent()
                }
            } else {
                cameraIntent()
            }
        }

        linearGallery.setOnClickListener {

            dialog.dismiss()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        AppConstant.GALLERY_PERMISSION_REQUEST_CODE
                    )
                } else {
                    galleryIntent()
                }
            } else {
                galleryIntent()
            }
        }

        linearFile.setOnClickListener {

            dialog.dismiss()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        AppConstant.PICKFILE_PERMISSION_REQUEST_CODE
                    )
                } else {
                    fileIntent()
                }
            } else {
                fileIntent()
            }
        }



        dialog.show()

    }


    private fun fileIntent() {

        FilePickerBuilder.instance
            .setMaxCount(1) //optional
            .setActivityTheme(R.style.AppLibAppTheme) //optional
            .pickFile(this, AppConstant.PICKFILE_REQUEST_CODE)

    }

    private fun cameraIntent() {

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        picturePath =
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    .toString() + "/photo_" + timeStamp + ".jpg"
            )
        val outputUri = Uri.fromFile(picturePath)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        startActivityForResult(intent, AppConstant.CAMERA_INTENT_REQUEST_CODE)

    }

    private fun galleryIntent() {

        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(pickPhoto, AppConstant.GALLERY_INTENT_REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            AppConstant.CAMERA_INTENT_REQUEST_CODE ->

                if (resultCode == Activity.RESULT_OK) {

                    try {


                        CropImage.activity(Uri.fromFile(picturePath))
                            //.setFixAspectRatio(true)
                            // .setAspectRatio(16, 9)
                            .start(requireContext(), this)

                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }

                }

            AppConstant.GALLERY_INTENT_REQUEST_CODE ->

                if (resultCode == Activity.RESULT_OK) {

                    try {

                        val selectedImage = data?.data as Uri
                        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor = requireContext().contentResolver?.query(
                            selectedImage, filePathColumn,
                            null, null, null
                        )
                        cursor?.moveToFirst()
                        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                        val path = cursor?.getString(columnIndex!!)
                        cursor?.close()

                        CropImage.activity(selectedImage)
                            //.setFixAspectRatio(true)
                            // .setAspectRatio(16, 9)
                            .start(requireContext(), this)

                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }


            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                if (data != null) {
                    val result = CropImage.getActivityResult(data)
                    if (resultCode == Activity.RESULT_OK) {
                        val resultUri = result.uri
                        val filePath: String =
                            FileValidationUtils.getPath(requireContext(), resultUri)
                        if (filePath != null) {

                            jobsImageAdapter?.addItems(UploadAttachmentsParams(filePath))

                        } else {
                            DroneDinApp.getAppInstance()
                                .showToast(getString(R.string.sometings_went_wrong))
                        }
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        val e = result.error
                        DroneDinApp.getAppInstance().showToast(e.message.toString())
                    }
                } else {
                    DroneDinApp.getAppInstance().showToast(getString(R.string.cancel_request))
                }
            }
            AppConstant.PICKFILE_REQUEST_CODE -> {
                if (data != null) {
                    if (resultCode == AppCompatActivity.RESULT_OK) {
                        val fileList =
                            data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_DOCS)
                        if (fileList != null) {
                            val filePath: String =
                                FileValidationUtils.getPath(requireContext(), fileList[0])
                            LogX.E(filePath.toString())
                            jobsImageAdapter?.addItems(UploadAttachmentsParams(filePath))
                        } else {
                            Toast.makeText(
                                requireContext(),
                                R.string.some_thing_went_wrong,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }

            }

            AppConstant.ADD_LOCATION_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    latitude = data.getDoubleExtra(AppConstant.LATITUDE, 37.8136)
                    longitude = data.getDoubleExtra(AppConstant.LONGITUDE, 144.9631)
                    locationAddress = data.getStringExtra(AppConstant.ADDRESS)!!

                    txt_location.text = locationAddress.trim()
                }
            }

            66 -> {
                if (resultCode == Activity.RESULT_OK) {
                    activity?.finish()
                }
            }


        }
    }

    override fun onLongClick(adapterPosition: Int) {
        openRemoveAlertDialog(adapterPosition)
    }

    private fun openRemoveAlertDialog(adapterPosition: Int) {
        alertDialog = AlertViewDialog(requireContext(), R.style.DialogThme)
        alertDialog!!.setTitle(getString(R.string.remove_attachment_message))
        alertDialog!!.setPositiveBtnTxt(getString(R.string.yes))
        alertDialog!!.setNegativeBtnTxt(getString(R.string.no))
        alertDialog!!.setOkListener(View.OnClickListener {
            jobsImageAdapter?.removeItem(adapterPosition)
            alertDialog?.dismiss()

        })
        alertDialog!!.show()
    }

    override fun onPostJobSuccessFully(loginParams: CreateJobsBean) {
        if (loginParams.id != null) {
            startActivityForResult(
                Intent(
                    requireContext(),
                    PostJobSubmittedActivity::class.java
                ).putExtra(AppConstant.ID, loginParams.id),
                66
            )
        }

    }

    override fun onPostJobFailed(loginParams: CreateJobsParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

}