package com.grewon.dronedin.addbank

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.helper.FileValidationUtils
import com.grewon.dronedin.utils.TimeUtils
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_add_bank_account.*
import kotlinx.android.synthetic.main.activity_add_bank_account.txt_save
import kotlinx.android.synthetic.main.image_bottom_dialog.view.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddBankAccountActivity : BaseActivity(), View.OnClickListener {

    private var picturePath: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bank_account)
        setClicks()
        initView()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_save.setOnClickListener(this)
        im_identity_layout.setOnClickListener(this)
        edt_birth_date.setOnClickListener(this)
    }

    private fun initView() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_identity_layout -> {
                openGalleryDialog()
            }
            R.id.img_back -> {
                finish()
            }
            R.id.txt_save -> {
                finish()
            }

            R.id.edt_birth_date -> {
               TimeUtils.showBirthDatePickerDialogForText(this,edt_birth_date)
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
                            Glide.with(this)
                                .load(filePath)
                                .into(identity_image)

                            //uploadPresenter.upload(filePath)
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

}