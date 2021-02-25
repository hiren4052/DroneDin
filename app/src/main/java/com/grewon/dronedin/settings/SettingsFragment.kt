package com.grewon.dronedin.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.paymentmethod.PaymentMethodActivity
import com.grewon.dronedin.splash.SplashActivity
import com.grewon.dronedin.utils.ListUtils
import com.grewon.dronedin.web.WebActivity
import com.plumillonforge.android.chipview.Chip
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.jobs_map_bottom_dialog.view.*
import kotlinx.android.synthetic.main.logout_bottom_dialog.view.*


class SettingsFragment : Fragment(), View.OnClickListener {

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
        txt_logout.setOnClickListener(this)
    }

    private fun initView() {
        Glide.with(this).load(R.drawable.img_dummy)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(22, 3)))
            .into(blur_img_user)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.im_back -> {

            }
            R.id.im_edit -> {

            }
            R.id.txt_payment_method -> {
                startActivity(Intent(context, PaymentMethodActivity::class.java))
            }
            R.id.txt_change_password -> {

            }
            R.id.txt_privacy -> {
                startActivity(
                    Intent(context, WebActivity::class.java).putExtra(
                        AppConstant.WEB_URL,
                        AppConstant.PRIVACY_URL
                    ).putExtra(AppConstant.TAG, getString(R.string.privacy_policy))

                )
            }
            R.id.txt_terms -> {
                startActivity(
                    Intent(context, WebActivity::class.java).putExtra(
                        AppConstant.WEB_URL,
                        AppConstant.TERMS_OF_SERVICE_URL
                    ).putExtra(AppConstant.TAG, getString(R.string.terms_amp_conditions))

                )
            }
            R.id.txt_membership -> {

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
        val dialog = BottomSheetDialog(
            requireContext(), R.style.CustomBottomSheetDialogTheme
        )
        dialog.setContentView(view)

        val txtCancel = view.txt_cancel
        val txtLogout = view.txt_logout

        txtCancel.setOnClickListener { dialog.dismiss() }
        txtLogout.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(requireContext(), SplashActivity::class.java))
            activity?.finishAffinity()
        }

        dialog.show()
    }

}