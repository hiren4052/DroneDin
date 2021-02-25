package com.grewon.dronedin.addcard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.utils.ListUtils
import kotlinx.android.synthetic.main.activity_add_card.*

class AddCardActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        initView()
        setClicks()
    }

    private fun setClicks() {
        txt_save.setOnClickListener(this)
        im_back.setOnClickListener(this)
        txt_year.setOnClickListener(this)
        txt_month.setOnClickListener(this)
    }

    private fun initView() {

        Glide.with(this).asGif().load(R.drawable.credit_card_animation).into(top_image)


        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.simple_spinner_dropdown_item,
            ListUtils.getMonthList()
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        month_spinner.adapter = adapter

        month_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                txt_month.text = ListUtils.getMonthList()[position]

            }

        }


        val yearAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.simple_spinner_dropdown_item,
            ListUtils.getYearList()
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        year_spinner.adapter = yearAdapter

        year_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                txt_year.text = ListUtils.getYearList()[position]

            }

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_save -> {
                finish()
            }
            R.id.im_back -> {
                finish()
            }
            R.id.txt_month -> {
                month_spinner.performClick()
            }
            R.id.txt_year -> {
                year_spinner.performClick()
            }
        }
    }
}