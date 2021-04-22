package com.grewon.dronedin.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseFragment
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.changepassword.ChangePasswordActivity
import com.grewon.dronedin.clientprofile.ClientProfileActivity
import com.grewon.dronedin.dispute.DisputeActivity
import com.grewon.dronedin.membership.MemberShipActivity
import com.grewon.dronedin.paymentmethod.PaymentMethodActivity
import com.grewon.dronedin.pilotprofile.PilotProfileActivity
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.MainScreenData
import com.grewon.dronedin.settings.contract.SettingsContract
import com.grewon.dronedin.splash.SplashActivity
import com.grewon.dronedin.utils.IconUtils
import com.grewon.dronedin.utils.ValidationUtils
import com.grewon.dronedin.web.WebActivity
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.badge_type
import kotlinx.android.synthetic.main.fragment_settings.blur_img_user
import kotlinx.android.synthetic.main.fragment_settings.im_back
import kotlinx.android.synthetic.main.fragment_settings.img_user
import kotlinx.android.synthetic.main.fragment_settings.txt_user_name
import kotlinx.android.synthetic.main.logout_bottom_dialog.view.*
import retrofit2.Retrofit
import javax.inject.Inject


class SettingsFragment : BaseFragment(), View.OnClickListener, SettingsContract.View {

    private var logoutDialog: BottomSheetDialog? = null

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var settingsPresenter: SettingsContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setClicks()
        initView()
    }

    private fun setClicks() {
        im_back.setOnClickListener(this)
        im_edit.setOnClickListener(this)
        txt_payment_method.setOnClickListener(this)
        txt_change_password.setOnClickListener(this)
        txt_privacy.setOnClickListener(this)
        txt_terms.setOnClickListener(this)
        txt_membership.setOnClickListener(this)
        txt_customer_support.setOnClickListener(this)
        txt_dispute.setOnClickListener(this)
        notification_switch.setOnClickListener(this)
        txt_logout.setOnClickListener(this)
    }

    private fun initView() {
        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        settingsPresenter.attachView(this)
        settingsPresenter.attachApiInterface(retrofit)

        Glide.with(this)
            .load(preferenceUtils.getProfileData()?.data?.profileImage)
            .apply(RequestOptions().placeholder(R.drawable.ic_user_place_holder))
            .into(img_user)

        txt_user_name.text = preferenceUtils.getProfileData()?.data?.userName

        if (preferenceUtils.getProfileData() != null && !ValidationUtils.isEmptyFiled(
                preferenceUtils.getProfileData()?.data?.profileImage!!
            )
        ) {
            Glide.with(this)
                .load(preferenceUtils.getProfileData()?.data?.profileImage!!)
                .apply(
                    RequestOptions.bitmapTransform(BlurTransformation(22, 3))
                        .placeholder(R.drawable.drone_for_blur)
                )
                .into(blur_img_user)
        } else {
            Glide.with(this)
                .load(R.drawable.drone_for_blur)
                .apply(
                    RequestOptions.bitmapTransform(BlurTransformation(22, 3))
                )
                .into(blur_img_user)
        }

        if (preferenceUtils.getProfileData() != null && preferenceUtils.getProfileData()?.data != null && preferenceUtils.getProfileData()?.data?.notification != null
        ) {
            notification_switch.isChecked =
                preferenceUtils.getProfileData()?.data?.notification == "on"
        }



        if (isPilotAccount()) {
            txt_membership.visibility = View.VISIBLE
            view_notification.visibility = View.VISIBLE
            IconUtils.setBadgeImage(
                requireContext(),
                badge_type,
                preferenceUtils.getProfileData()?.data?.badge!!.toString()
            )
        } else {
            txt_membership.visibility = View.GONE
            view_notification.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.im_back -> {

            }
            R.id.im_edit -> {
                if (isPilotAccount()) {
                    startActivity(
                        Intent(context, PilotProfileActivity::class.java).putExtra(
                            AppConstant.ID,
                            preferenceUtils.getLoginCredentials()?.data?.userId
                        ).putExtra(AppConstant.TAG, true)
                    )
                } else {
                    startActivity(
                        Intent(context, ClientProfileActivity::class.java).putExtra(
                            AppConstant.ID,
                            preferenceUtils.getLoginCredentials()?.data?.userId
                        ).putExtra(AppConstant.TAG, true)
                    )
                }

            }
            R.id.txt_payment_method -> {
                startActivity(Intent(context, PaymentMethodActivity::class.java))
            }
            R.id.txt_change_password -> {
                startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
            }
            R.id.txt_privacy -> {
                startActivity(
                    Intent(context, WebActivity::class.java).putExtra(
                        AppConstant.WEB_URL,
                        preferenceUtils.getProfileData()?.data?.privacyPolicyUrl.toString()
                    ).putExtra(AppConstant.TAG, getString(R.string.privacy_policy))

                )
            }
            R.id.txt_terms -> {
                startActivity(
                    Intent(context, WebActivity::class.java).putExtra(
                        AppConstant.WEB_URL,
                        preferenceUtils.getProfileData()?.data?.termsConditionsUrl.toString()
                    ).putExtra(AppConstant.TAG, getString(R.string.terms_amp_conditions))

                )
            }
            R.id.txt_membership -> {
                startActivity(Intent(context, MemberShipActivity::class.java))
            }
            R.id.txt_customer_support -> {
                startActivity(
                    Intent(context, WebActivity::class.java).putExtra(
                        AppConstant.WEB_URL,
                        preferenceUtils.getProfileData()?.data?.customerSupportUrl.toString()
                    ).putExtra(AppConstant.TAG, getString(R.string.customer_support))

                )
            }
            R.id.txt_dispute -> {
                startActivity(Intent(context, DisputeActivity::class.java))
            }
            R.id.notification_switch -> {
                val notificationStatus = if (notification_switch.isChecked) "on" else "off"
                settingsPresenter.notificationOnOff(notificationStatus)
            }
            R.id.txt_logout -> {
                openLogoutDialog()
            }
        }

    }

    @SuppressLint("InflateParams")
    private fun openLogoutDialog() {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.logout_bottom_dialog, null)
        logoutDialog = BottomSheetDialog(
            requireContext(), R.style.CustomBottomSheetDialogTheme
        )
        logoutDialog?.setContentView(view)

        val txtCancel = view.txt_cancel
        val txtLogout = view.txt_logout

        txtCancel.setOnClickListener { logoutDialog?.dismiss() }
        txtLogout.setOnClickListener {

            settingsPresenter.logoutUser()
        }

        logoutDialog?.show()
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

    override fun onLogoutSuccessful(response: CommonMessageBean) {
        if (response.msg != null) {
            DroneDinApp.getAppInstance().showToast(response.msg)

            if (logoutDialog != null) {
                logoutDialog?.dismiss()
            }
            passIntent()
        }
    }

    override fun onLogoutFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun onNotificationOnOffSuccessful(response: MainScreenData) {
        preferenceUtils.saveProfileData(response)
    }

    override fun onNotificationOnOffFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    private fun passIntent() {
        preferenceUtils.flushData()
        startActivity(Intent(requireContext(), SplashActivity::class.java))
        activity?.finishAffinity()
    }

}