package com.grewon.dronedin.intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.intro.page.FirstFragment
import com.grewon.dronedin.intro.page.SecondFragment
import com.grewon.dronedin.intro.page.ThirdFragment
import com.grewon.dronedin.signin.SignInActivity
import com.grewon.dronedin.signup.SignUpActivity
import com.grewon.dronedin.signup.SignUpTypeActivity
import com.grewon.dronedin.utils.TextUtils
import com.grewon.dronedin.viewpager.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        initView()
        setClickListeners()
    }

    private fun initView() {

        txt_sign_up.text = TextUtils.signUpSpannableString(this)
        setViewPager()

    }

    private fun setClickListeners() {

        txt_sign_up.setOnClickListener(this)
        txt_login.setOnClickListener(this)

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    ic_indicator.setImageResource(R.drawable.ic_indicator1)
                } else if (position == 1) {
                    ic_indicator.setImageResource(R.drawable.ic_indicator2)
                } else if (position == 2) {
                    ic_indicator.setImageResource(R.drawable.ic_indicator3)
                }

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun setViewPager() {
        val viewpagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewpagerAdapter.addFragment(FirstFragment(), "First")
        viewpagerAdapter.addFragment(SecondFragment(), "Second")
        viewpagerAdapter.addFragment(ThirdFragment(), "Third")
        view_pager.adapter = viewpagerAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_login -> {
                startActivity(Intent(this, SignInActivity::class.java))

            }
            R.id.txt_sign_up -> {
                startActivity(Intent(this, SignUpTypeActivity::class.java))
            }
        }

    }

}