package com.grewon.dronedin.portfolio

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
import com.grewon.dronedin.attachments.UploadAttachmentsAdapter
import com.grewon.dronedin.dialogs.AlertViewDialog
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.FileValidationUtils
import com.grewon.dronedin.portfolio.contract.PortFolioContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.PilotProfileBean
import com.grewon.dronedin.server.params.AddPortFolioParams
import com.grewon.dronedin.server.params.UploadAttachmentsParams
import com.grewon.dronedin.utils.ValidationUtils
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_port_folio.*
import kotlinx.android.synthetic.main.activity_port_folio.edt_description
import kotlinx.android.synthetic.main.activity_port_folio.edt_title
import kotlinx.android.synthetic.main.activity_port_folio.im_add_attachments
import kotlinx.android.synthetic.main.activity_port_folio.image_recycle
import kotlinx.android.synthetic.main.fragment_add_job_details.*
import kotlinx.android.synthetic.main.image_bottom_dialog.view.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import retrofit2.Retrofit
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PortFolioActivity : BaseActivity(), View.OnClickListener, PortFolioContract.View,
    UploadAttachmentsAdapter.OnItemLongClickListeners {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var portFolioPresenter: PortFolioContract.Presenter

    private var picturePath: File? = null
    private var jobsImageAdapter: UploadAttachmentsAdapter? = null

    private var portFolioBean: PilotProfileBean.Portfolio? = null
    private var alertDialog: AlertViewDialog? = null
    private var attachmentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_port_folio)
        setImageAdapter()
        initView()
        setClicks()
    }

    private fun setClicks() {
        txt_save.setOnClickListener(this)
        img_back.setOnClickListener(this)
        im_add_attachments.setOnClickListener(this)
        txt_delete.setOnClickListener(this)

    }

    private fun initView() {

        portFolioBean = intent.getParcelableExtra(AppConstant.BEAN)

        if (portFolioBean != null) {
            txt_delete.visibility = View.VISIBLE
            txt_toolbar_title.text = getString(R.string.edit_project)
            edt_description.setText(portFolioBean?.description)
            edt_title.setText(portFolioBean?.title)

            val attachmentList = ArrayList<UploadAttachmentsParams>()
            if (portFolioBean?.attachment != null) {
                for (item in portFolioBean?.attachment!!) {
                    attachmentList.add(
                        UploadAttachmentsParams(
                            item?.attachment!!,
                            item.attachmentId!!

                        )
                    )
                }
                jobsImageAdapter?.addItemsList(attachmentList)
            }
        } else {
            txt_delete.visibility = View.GONE
            txt_toolbar_title.text = getString(R.string.new_project)
        }


        DroneDinApp.getAppInstance().getAppComponent().inject(this)

        portFolioPresenter.attachView(this)
        portFolioPresenter.attachApiInterface(retrofit)

    }

    private fun setImageAdapter() {
        image_recycle.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        jobsImageAdapter = UploadAttachmentsAdapter(this, this)
        image_recycle.adapter = jobsImageAdapter

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.im_add_attachments -> {
                openGalleryDialog()
            }
            R.id.txt_delete -> {
                portFolioPresenter.deletePortFolio(portFolioBean?.portfolioId.toString())
            }

            R.id.txt_save -> {
                if (ValidationUtils.isEmptyFiled(edt_title.text.toString())) {
                    DroneDinApp.getAppInstance().showToast(getString(R.string.please_enter_title))
                } else if (ValidationUtils.isEmptyFiled(edt_description.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_enter_description))
                } else if (jobsImageAdapter?.itemList?.size == 0) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_add_pictures))
                } else if (jobsImageAdapter?.itemList?.size == 0) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_add_pictures))
                } else if (jobsImageAdapter?.itemList?.size!! > 5) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.maximum_5_image_allow))
                } else {

                    val params = AddPortFolioParams()
                    params.portfolio_title = edt_title.text.toString()
                    params.portfolio_desc = edt_description.text.toString()
                    params.attachments = jobsImageAdapter?.itemList

                    if (portFolioBean != null) {
                        portFolioPresenter.updatePortFolio(
                            params,
                            portFolioBean?.portfolioId.toString()
                        )
                    } else {
                        portFolioPresenter.addPortFolio(params)
                    }
                }
            }
        }
    }


    @SuppressLint("InflateParams")
    private fun openGalleryDialog() {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.image_bottom_dialog, null)
        val dialog = BottomSheetDialog(
            this, R.style.CustomBottomSheetDialogTheme
        )
        dialog.setContentView(view)

        val linearCamera = view.ll_camera
        val linearGallery = view.ll_gallery
        val linearCancel = view.ll_cancel


        linearCancel.setOnClickListener { dialog.dismiss() }

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

        dialog.show()

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


        }
    }

    override fun onAddPortFolioSuccessful(response: CommonMessageBean) {
        DroneDinApp.getAppInstance().showToast(response.msg.toString())
        setResult(RESULT_OK)
        finish()
    }

    override fun onAddPortFolioFailed(loginParams: AddPortFolioParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

    override fun onUpdatePortFolioSuccessful(response: CommonMessageBean) {
        DroneDinApp.getAppInstance().showToast(response.msg.toString())
        setResult(RESULT_OK)
        finish()
    }

    override fun onUpdatePortFolioFailed(loginParams: AddPortFolioParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onDeleteAttachmentSuccessfully(commonMessageBean: CommonMessageBean) {
        if (commonMessageBean.msg != null) {
            DroneDinApp.getAppInstance().showToast(commonMessageBean.msg)
            jobsImageAdapter?.removeItem(attachmentPosition)
        }
    }

    override fun onDeleteAttachmentFailed(commonMessageBean: CommonMessageBean) {
        if (commonMessageBean.msg != null) {
            DroneDinApp.getAppInstance().showToast(commonMessageBean.msg)

        }
    }

    override fun onDeletePortFolioSuccessfully(commonMessageBean: CommonMessageBean) {
        DroneDinApp.getAppInstance().showToast(commonMessageBean.msg.toString())
        setResult(RESULT_OK)
        finish()
    }

    override fun onDeletePortFolioFailed(commonMessageBean: CommonMessageBean) {
        if (commonMessageBean.msg != null) {
            DroneDinApp.getAppInstance().showToast(commonMessageBean.msg)

        }
    }

    override fun onLongClick(adapterPosition: Int, item: UploadAttachmentsParams) {
        attachmentPosition = adapterPosition
        openRemoveAlertDialog(adapterPosition, item)
    }

    private fun openRemoveAlertDialog(adapterPosition: Int, item: UploadAttachmentsParams) {
        alertDialog = AlertViewDialog(this, R.style.DialogThme)
        alertDialog!!.setTitle(getString(R.string.remove_attachment_message))
        alertDialog!!.setPositiveBtnTxt(getString(R.string.yes))
        alertDialog!!.setNegativeBtnTxt(getString(R.string.no))
        alertDialog!!.setOkListener(View.OnClickListener {
            if (ValidationUtils.isEmptyFiled(item.attachmentId)) {
                jobsImageAdapter?.removeItem(adapterPosition)
            } else {
                portFolioPresenter.deleteAttachment(item.attachmentId)
            }
            alertDialog?.dismiss()

        })
        alertDialog!!.show()
    }

}