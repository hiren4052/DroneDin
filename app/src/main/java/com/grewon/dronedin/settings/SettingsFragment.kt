package com.grewon.dronedin.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grewon.dronedin.R
import com.grewon.dronedin.paymentmethod.PaymentMethodActivity
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.fragment_settings.*


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
            .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
            .into(blur_img_user)
    }

    override fun onClick(v: View?) {

        when (R.id.im_back) {

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

            }
            R.id.txt_terms -> {

            }
            R.id.txt_membership -> {

            }
            R.id.txt_logout -> {

            }
        }

    }

}