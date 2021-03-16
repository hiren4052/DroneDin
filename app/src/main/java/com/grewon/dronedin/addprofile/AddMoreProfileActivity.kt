package com.grewon.dronedin.addprofile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.grewon.dronedin.R
import com.grewon.dronedin.addprofile.adapter.ProfileEqipmentsAdapter
import com.grewon.dronedin.addprofile.adapter.ProfileSkillsAdapter
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.main.MainActivity
import com.grewon.dronedin.utils.ListUtils
import kotlinx.android.synthetic.main.activity_add_more_profile.*
import kotlinx.android.synthetic.main.activity_add_more_profile.top_image
import kotlinx.android.synthetic.main.activity_add_more_profile.txt_save
import kotlinx.android.synthetic.main.activity_add_profile.*
import kotlinx.android.synthetic.main.activity_sign_up_type.*


class AddMoreProfileActivity : BaseActivity(), View.OnClickListener,
    ProfileSkillsAdapter.OnFilterSkillsItemSelected,
    ProfileEqipmentsAdapter.OnFilterSkillsItemSelected {

    private var profileEquipmentsAdapter: ProfileEqipmentsAdapter? = null
    private var profileSkillsAdapter: ProfileSkillsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_more_profile)
        initView()
        setClicks()
    }

    private fun setClicks() {
        txt_save.setOnClickListener(this)
        im_back.setOnClickListener(this)
        txt_category.setOnClickListener(this)

    }

    private fun setSkillsAdapter() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        skills_recycle.layoutManager = layoutManager
        profileSkillsAdapter = ProfileSkillsAdapter(this, this)
        skills_recycle.adapter = profileSkillsAdapter
        profileSkillsAdapter?.addItemsList(ListUtils.getSkillsBean())
    }

    private fun setEquipmentsAdapter() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        equipments_recycle.layoutManager = layoutManager
        profileEquipmentsAdapter = ProfileEqipmentsAdapter(this, this)
        equipments_recycle.adapter = profileEquipmentsAdapter
        profileEquipmentsAdapter?.addItemsList(ListUtils.getEquipmentsBean())
    }


    private fun initView() {
        DroneDinApp.getAppInstance().loadGifImage(R.drawable.add_profile,top_image)
        setCategoryAdapter()
        setSkillsAdapter()
        setEquipmentsAdapter()
    }

    private fun setCategoryAdapter() {

        val categoryAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.simple_spinner_dropdown_item,
            ListUtils.getCategoryStrings(ListUtils.getCategoryBean())
        )
        categoryAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        category_spinner.adapter = categoryAdapter

        category_spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    txt_category.text = ListUtils.getCategoryBean()[position].userProfileName
                }

            }
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.txt_save -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            R.id.txt_category -> {
                category_spinner.performClick()
            }
            R.id.im_back -> {
                finish()
            }
        }
    }

    override fun onFilterSkillsItemSelected() {

    }

}