package com.grewon.dronedin.submitproposal

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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.helper.FileValidationUtils
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.attachments.UploadAttachmentsAdapter
import com.grewon.dronedin.dialogs.AlertViewDialog
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.server.CreateMilestoneBean
import com.grewon.dronedin.milestone.adapter.CreateMileStoneAdapter
import com.grewon.dronedin.milestone.adapter.MileStoneAdapter
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.MilestonesDataBean
import com.grewon.dronedin.server.params.SubmitProposalParams
import com.grewon.dronedin.server.params.UploadAttachmentsParams
import com.grewon.dronedin.submitproposal.contract.SubmitProposalContract
import com.grewon.dronedin.utils.ValidationUtils
import com.theartofdev.edmodo.cropper.CropImage
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.activity_submit_proposal.*
import kotlinx.android.synthetic.main.file_bottom_dialog.view.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import retrofit2.Retrofit
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class SubmitProposalActivity : BaseActivity(), View.OnClickListener,
    UploadAttachmentsAdapter.OnItemLongClickListeners,
    CreateMileStoneAdapter.OnRemoveItemClickListeners {


    private var alertDialog: AlertViewDialog? = null
    private var picturePath: File? = null
    private var createMileStoneAdapter: CreateMileStoneAdapter? = null
    private var jobsImageAdapter: UploadAttachmentsAdapter? = null
    private var mileStoneAdapter: MileStoneAdapter? = null
    private var jobId: String = ""
    private var totalPrice: String = ""
    private var jobTitle: String = ""
    private var jobDescription: String = ""
    private var existingMileStoneList = ArrayList<MilestonesDataBean>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_proposal)
        txt_toolbar_title.text = getString(R.string.submit_proposal)
        setClicks()
        initView()
    }

    private fun initView() {


        jobId = intent.getStringExtra(AppConstant.ID).toString()
        totalPrice = intent.getStringExtra(AppConstant.PRICE).toString()
        jobTitle = intent.getStringExtra(AppConstant.TITLE).toString()
        jobDescription = intent.getStringExtra(AppConstant.DESCRIPTION).toString()
        existingMileStoneList = intent.getParcelableArrayListExtra(AppConstant.BEAN)!!


        edt_title.setText(jobTitle)
        //edt_description.setText(jobDescription)

        existing_total_price.text = getString(R.string.price_string, totalPrice)


        create_milestone_recycle.layoutManager = LinearLayoutManager(this)
        createMileStoneAdapter = CreateMileStoneAdapter(this, this)
        create_milestone_recycle.adapter = createMileStoneAdapter



        setImageAdapter()
        setExistingMileStoneAdapter()
    }

    private fun setImageAdapter() {
        image_recycle.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        jobsImageAdapter = UploadAttachmentsAdapter(this, this)
        image_recycle.adapter = jobsImageAdapter

    }

    private fun setExistingMileStoneAdapter() {
        existing_milestone_recycle.layoutManager = LinearLayoutManager(this)
        mileStoneAdapter = MileStoneAdapter(this)
        existing_milestone_recycle.adapter = mileStoneAdapter
        mileStoneAdapter?.addItemsList(existingMileStoneList)
    }

    private fun setClicks() {
        im_add_milestone.setOnClickListener(this)
        im_add_attachments.setOnClickListener(this)
        existing_milestone_check.setOnClickListener(this)
        new_milestone_check.setOnClickListener(this)
        img_back.setOnClickListener(this)
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

                        edt_price.setText(createMileStoneAdapter?.getPriceString())

                    }
                }
            }
            R.id.im_add_attachments -> {
                openFileDialog()
            }
            R.id.img_back -> {
                finish()
            }
            R.id.txt_submit -> {
                if (ValidationUtils.isEmptyFiled(edt_title.text.toString())) {
                    DroneDinApp.getAppInstance().showToast(getString(R.string.please_enter_title))
                } else if (ValidationUtils.isEmptyFiled(edt_description.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_enter_description))
                } else if (new_milestone_check.isChecked) {
                    if (ValidationUtils.isEmptyFiled(edt_price.text.toString())) {
                        DroneDinApp.getAppInstance()
                            .showToast(getString(R.string.please_enter_proposal_price))
                    } else if (createMileStoneAdapter != null && createMileStoneAdapter?.itemList?.size == 0) {
                        DroneDinApp.getAppInstance()
                            .showToast(getString(R.string.please_create_milestone))
                    } else {
                        apiCall()
                    }
                } else {
                    apiCall()

                }
            }
            R.id.existing_milestone_check -> {
                if (existing_milestone_check.isChecked) {
                    new_milestone_check.isChecked = false
                    existing_milestone_layout.visibility = View.VISIBLE
                    create_milestone_layout.visibility = View.GONE
                    edit_price_layout.visibility = View.GONE
                    existing_total_price.visibility = View.VISIBLE
                } else {
                    new_milestone_check.isChecked = true
                    existing_milestone_layout.visibility = View.GONE
                    create_milestone_layout.visibility = View.VISIBLE
                    edit_price_layout.visibility = View.VISIBLE
                    existing_total_price.visibility = View.GONE
                }
            }
            R.id.new_milestone_check -> {
                if (new_milestone_check.isChecked) {
                    existing_milestone_check.isChecked = false
                    existing_milestone_layout.visibility = View.GONE
                    create_milestone_layout.visibility = View.VISIBLE
                    edit_price_layout.visibility = View.VISIBLE
                    existing_total_price.visibility = View.GONE
                } else {
                    existing_milestone_check.isChecked = true
                    existing_milestone_layout.visibility = View.VISIBLE
                    create_milestone_layout.visibility = View.GONE
                    edit_price_layout.visibility = View.GONE
                    existing_total_price.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun apiCall() {

        val params = SubmitProposalParams()
        params.proposal_title = edt_title.text.toString()
        params.proposal_description = edt_description.text.toString()

        if (new_milestone_check.isChecked) {
            params.proposal_total_price = edt_price.text.toString()
            params.proposal_milestone = AppConstant.NEW_MILESTONE
            params.milestone = createMileStoneAdapter?.itemList
        } else {
            params.proposal_total_price = existing_total_price.text.toString().replace("$", "")
            params.proposal_milestone = AppConstant.EXISTING_MILESTONE

            val milestoneList = ArrayList<CreateMilestoneBean>()


            for(item in mileStoneAdapter?.itemList!!){
                val bean=CreateMilestoneBean()
                bean.price=item.milestonePrice!!
                bean.details=item.milestoneDetails!!
                bean.milestoneId=item.milestoneId!!
                milestoneList.add(bean)
            }

            params.milestone = milestoneList

        }

        params.attachments = jobsImageAdapter?.itemList

        params.job_id = jobId

        startActivityForResult(
            Intent(
                this,
                ReviewProposalActivity::class.java
            ).putExtra(AppConstant.BEAN, params), 13
        )


    }

    @SuppressLint("InflateParams")
    private fun openFileDialog() {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.file_bottom_dialog, null)
        val dialog = BottomSheetDialog(
            this, R.style.CustomBottomSheetDialogTheme
        )
        dialog.setContentView(view)

        val linearCamera = view.ll_camera
        val linearGallery = view.ll_gallery
        val linearFile = view.ll_file
        val textDialogTitle = view.txt_dialog_title

        textDialogTitle.text = getString(R.string.select_a_file)


        textDialogTitle.setOnClickListener { dialog.dismiss() }

        linearCamera.setOnClickListener {

            dialog.dismiss()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(
                        this,
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
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(
                        this,
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
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(
                        this,
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
            .setMaxCount(1)
            .setVideoSizeLimit(AppConstant.VIDEO_FILE_SIZE)
            .setImageSizeLimit(AppConstant.IMAGE_FILE_SIZE)
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
                            .start(this)

                    } catch (e: Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }

                }

            AppConstant.GALLERY_INTENT_REQUEST_CODE ->

                if (resultCode == Activity.RESULT_OK) {

                    try {

                        val selectedImage = data?.data as Uri
                        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor = contentResolver?.query(
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
                            .start(this)

                    } catch (e: Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }


            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                if (data != null) {
                    val result = CropImage.getActivityResult(data)
                    if (resultCode == Activity.RESULT_OK) {
                        val resultUri = result.uri
                        val filePath: String = FileValidationUtils.getPath(this, resultUri)
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
                    if (resultCode == RESULT_OK) {
                        val fileList =
                            data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_DOCS)
                        if (fileList != null) {
                            val filePath: String =
                                FileValidationUtils.getPath(this, fileList[0])
                            LogX.E(filePath.toString())
                            jobsImageAdapter?.addItems(UploadAttachmentsParams(filePath))
                        } else {
                            Toast.makeText(
                                this,
                                R.string.some_thing_went_wrong,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }

            }
            13 -> {
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK)
                    finish()
                }
            }


        }
    }

    override fun onLongClick(adapterPosition: Int, item: UploadAttachmentsParams) {
        openRemoveAlertDialog(adapterPosition)
    }

    private fun openRemoveAlertDialog(adapterPosition: Int) {
        alertDialog = AlertViewDialog(this, R.style.DialogThme)
        alertDialog!!.setTitle(getString(R.string.remove_attachment_message))
        alertDialog!!.setPositiveBtnTxt(getString(R.string.yes))
        alertDialog!!.setNegativeBtnTxt(getString(R.string.no))
        alertDialog!!.setOkListener(View.OnClickListener {
            jobsImageAdapter?.removeItem(adapterPosition)
            alertDialog?.dismiss()

        })
        alertDialog!!.show()
    }


    override fun onItemRemove(adapterPosition: Int) {
        edt_price.setText(createMileStoneAdapter?.getPriceString())
    }


}