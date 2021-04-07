package com.grewon.dronedin.dispute

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*

class DisputeActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispute)
        setClicks()
        initView()
    }

    private fun initView() {
        txt_toolbar_title.text = getString(R.string.disputes)
    }

    private fun setClicks() {

        img_back.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }
}