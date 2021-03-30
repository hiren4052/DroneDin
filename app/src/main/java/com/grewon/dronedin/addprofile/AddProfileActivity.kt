package com.grewon.dronedin.addprofile

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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.addprofile.contract.AddProfileContract
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.FileValidationUtils
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.main.MainActivity
import com.grewon.dronedin.mapscreen.MapScreenActivity
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.IdentificationBean
import com.grewon.dronedin.server.LocationBean
import com.grewon.dronedin.server.ProfileBean
import com.grewon.dronedin.server.params.ProfileUpdateParams
import com.grewon.dronedin.utils.ListUtils
import com.grewon.dronedin.utils.ScreenUtils
import com.grewon.dronedin.utils.TextChangeListeners
import com.grewon.dronedin.utils.ValidationUtils
import com.theartofdev.edmodo.cropper.CropImage
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.activity_add_profile.*

import kotlinx.android.synthetic.main.file_bottom_dialog.view.*
import kotlinx.android.synthetic.main.image_bottom_dialog.view.*
import kotlinx.android.synthetic.main.image_bottom_dialog.view.ll_camera
import kotlinx.android.synthetic.main.image_bottom_dialog.view.ll_gallery
import retrofit2.Retrofit
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddProfileActivity : BaseActivity(), View.OnClickListener, AddProfileContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var addProfilePresenter: AddProfileContract.Presenter

    private var picturePath: File? = null
    private var imageType: String = "front"
    private var serverFrontImage: String = ""
    private var serverBackImage: String = ""
    private var userImage: String = ""

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var locationAddress: String = ""
    private var identificationId: String? = ""
    private var identificationList: ArrayList<IdentificationBean.IdentificationBeanItem>? = null
    private var isEdit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScreenUtils.changeStatusBarColor(
            this,
            ContextCompat.getColor(this, R.color.span_text_color)
        )
        setContentView(R.layout.activity_add_profile)
        setClicks()
        initView()
        addTextChangeListeners()
    }

    private fun addTextChangeListeners() {
        TextChangeListeners.editErrorTextRemover(edit_name, input_name)
        TextChangeListeners.editErrorTextRemover(edit_email, input_email)
        TextChangeListeners.editErrorTextRemover(edit_number, input_number)
        TextChangeListeners.editErrorTextRemover(edit_number, input_number)
        TextChangeListeners.autoCompleteErrorTextRemover(edt_location, input_location)
        TextChangeListeners.autoCompleteErrorTextRemover(
            edt_identification_document,
            input_identification
        )
    }

    private fun initView() {
        isEdit = intent.getBooleanExtra(AppConstant.TAG, false)

        if (isEdit) {
            img_back.visibility = View.VISIBLE
        } else {
            img_back.visibility = View.GONE
        }

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        addProfilePresenter.attachView(this)
        addProfilePresenter.attachApiInterface(retrofit)

        addProfilePresenter.getProfile()

        if (isPilotAccount()) {
            pilot_layout.visibility = View.VISIBLE
            txt_save.text = getString(R.string.next)

            addProfilePresenter.getIdentificationsData()

        } else {
            pilot_layout.visibility = View.GONE
            txt_save.text = getString(R.string.save)

        }
    }

    private fun setClicks() {
        front_image_layout.setOnClickListener(this)
        back_image_layout.setOnClickListener(this)
        edt_identification_document.setOnClickListener(this)
        edt_location.setOnClickListener(this)
        txt_save.setOnClickListener(this)
        user_layout.setOnClickListener(this)
        floating_user.setOnClickListener(this)
        img_back.setOnClickListener(this)
    }

    private fun passIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(
                    this,
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
                        this,
                        MapScreenActivity::class.java
                    ).putExtra(AppConstant.LOCATION_BEAN, locationsBean),
                    AppConstant.ADD_LOCATION_REQUEST_CODE
                )
            }
        } else {
            val locationsBean = LocationBean(latitude, longitude, locationAddress)
            startActivityForResult(
                Intent(
                    this,
                    MapScreenActivity::class.java
                ).putExtra(AppConstant.LOCATION_BEAN, locationsBean),
                AppConstant.ADD_LOCATION_REQUEST_CODE
            )
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
                            if (imageType == "front") {
                                serverFrontImage = filePath
                                Glide.with(this)
                                    .load(filePath)
                                    .into(front_image)
                            } else if (imageType == "user") {
                                userImage = filePath
                                Glide.with(this)
                                    .load(filePath)
                                    .into(user_image)
                            } else {
                                serverBackImage = filePath
                                Glide.with(this)
                                    .load(filePath)
                                    .into(back_image)
                            }
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
            AppConstant.PICKFILE_REQUEST_CODE -> {
                if (data != null) {
                    if (resultCode == RESULT_OK) {
                        val fileList =
                            data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_DOCS)
                        if (fileList != null) {
                            val filePath: String = FileValidationUtils.getPath(this, fileList[0])
                            if (imageType == "front") {
                                serverFrontImage = filePath
                                Glide.with(this)
                                    .load(filePath)
                                    .into(front_image)
                            } else {
                                serverBackImage = filePath
                                Glide.with(this)
                                    .load(filePath)
                                    .into(back_image)
                            }
                            LogX.E(filePath.toString())
                        } else {
                            Toast.makeText(this, R.string.some_thing_went_wrong, Toast.LENGTH_SHORT)
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

                    edt_location.setText(locationAddress.trim(), false)
                }
            }


        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.front_image_layout -> {
                imageType = "front"
                openGalleryDialog()
            }
            R.id.back_image_layout -> {
                imageType = "back"
                openGalleryDialog()
            }
            R.id.floating_user, R.id.user_layout -> {
                imageType = "user"
                openGalleryDialog()
            }
            R.id.edt_location -> {
                passIntent()
            }
            R.id.edt_identification_document -> {
                identification_spinner.performClick()
            }
            R.id.img_back -> {
                finish()
            }
            R.id.txt_save -> {
                if (ValidationUtils.isEmptyFiled(userImage)) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_select_profile_image))
                } else if (ValidationUtils.isEmptyFiled(edit_name.text.toString())) {
                    input_name.error = getString(R.string.please_enter_full_name_business_name)
                } else if (ValidationUtils.isEmptyFiled(edit_email.text.toString())) {
                    input_email.error = getString(R.string.please_enter_email_address)
                } else if (ValidationUtils.isEmptyFiled(edit_number.text.toString())) {
                    edit_number.error = getString(R.string.please_enter_phone_number)
                } else if (ValidationUtils.isEmptyFiled(edt_location.text.toString())) {
                    input_location.error = getString(R.string.please_select_location)
                } else {
                    if (isPilotAccount()) {
                        when {
                            ValidationUtils.isEmptyFiled(edt_identification_document.text.toString()) -> {
                                input_identification.error =
                                    getString(R.string.please_select_location)
                            }
                            ValidationUtils.isEmptyFiled(serverFrontImage) -> {
                                DroneDinApp.getAppInstance()
                                    .showToast(getString(R.string.please_select_front_side))
                            }
                            ValidationUtils.isEmptyFiled(serverBackImage) -> {
                                DroneDinApp.getAppInstance()
                                    .showToast(getString(R.string.please_select_back_side))
                            }
                            else -> {
                                apiCall()
                            }
                        }
                    } else {
                        apiCall()
                    }
                }


            }
        }
    }

    private fun apiCall() {
        val profileUpdateParams = ProfileUpdateParams(
            userName = edit_name.text.toString(),
            userPhoneNumber = edit_number.text.toString(),
            userAddress = locationAddress,
            userLatitude = latitude,
            userLongitude = longitude,
            proofId = identificationId,
            proofFrontSide = serverFrontImage,
            proofBackSide = serverBackImage,
            userImage
        )
        addProfilePresenter.updateProfile(profileUpdateParams)

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
            .setMaxCount(1) //optional
            .setActivityTheme(R.style.AppLibAppTheme) //optional
            .pickFile(this, AppConstant.PICKFILE_REQUEST_CODE)

    }

    override fun onProfileGetSuccessful(response: ProfileBean) {
        if (response.data != null) {

            setView(response.data)
        }
    }

    private fun setView(data: ProfileBean.Data) {

        edit_name.setText(
            data.userName.toString()
        )
        edit_email.setText(
            data.userEmail.toString()
        )
        edit_number.setText(
            data.userPhoneNumber.toString()
        )
        if (data.userAddress != null && !ValidationUtils.isEmptyFiled(data.userAddress.toString())) {
            locationAddress = data.userAddress.toString()
            latitude = data.userLatitude?.toDouble()!!
            longitude = data.userLongitude?.toDouble()!!

            edt_location.setText(locationAddress, false)
        }

        userImage = data.profileImage.toString()

        Glide.with(this).load(userImage)
            .apply(RequestOptions().placeholder(R.drawable.ic_user_place_holder)).into(user_image)


        if (isPilotAccount()) {

            if (data.proof != null) {
                edt_identification_document.setText(data.proof.proofName.toString(), false)
            }

            serverFrontImage = data.proofFrontSide.toString()
            serverBackImage = data.proofBackSide.toString()
            identificationId = data.proof?.proofId

            Glide.with(this).load(serverFrontImage)
                .apply(RequestOptions().placeholder(R.drawable.ic_image_select_back))
                .into(front_image)

            Glide.with(this).load(serverBackImage)
                .apply(RequestOptions().placeholder(R.drawable.ic_image_select_back))
                .into(back_image)

        }

    }

    override fun onProfileGetFailed(loginParams: ProfileUpdateParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onProfileUpdateSuccessFully(loginParams: ProfileBean) {
        if (loginParams.data != null && loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            if (!isEdit) {
                if (isPilotAccount()) {
                    startActivity(Intent(this, AddMoreProfileActivity::class.java))
                } else {
                    val userData = preferenceUtils.getLoginCredentials()
                    userData?.data?.profileImage = loginParams.data.profileImage
                    userData?.data?.isStepComplete = true
                    preferenceUtils.saveLoginCredential(userData!!)
                    startActivity(Intent(this, MainActivity::class.java))
                }
            } else {
                val userData = preferenceUtils.getLoginCredentials()
                userData?.data?.isStepComplete = true
                preferenceUtils.saveLoginCredential(userData!!)
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    override fun onProfileUpdateFailed(loginParams: ProfileUpdateParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

    override fun onIdentificationsDataGetSuccessful(response: IdentificationBean) {
        if (response != null) {
            identificationList = response
            setIdentificationSpinner()
        }

    }

    private fun setIdentificationSpinner() {
        val expenseAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.simple_spinner_dropdown_item,
            ListUtils.getIdentificationsDocumentStrings(identificationList) as ArrayList<String>
        )
        expenseAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        identification_spinner.adapter = expenseAdapter

        identification_spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    identificationId = identificationList?.get(position)?.proofId
                    edt_identification_document.setText(identificationList?.get(position)?.proofName)
                }

            }
    }

    override fun onIdentificationsDataFailed(loginParams: CommonMessageBean) {

    }


}