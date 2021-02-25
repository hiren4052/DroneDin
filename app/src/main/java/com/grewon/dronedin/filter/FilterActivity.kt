package com.grewon.dronedin.filter

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.filter.adapter.FilterCategoryAdapter
import com.grewon.dronedin.filter.adapter.FilterEquipmentsAdapter
import com.grewon.dronedin.filter.adapter.FilterSkillsAdapter
import com.grewon.dronedin.utils.IconUtils
import com.grewon.dronedin.utils.ListUtils
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import kotlinx.android.synthetic.main.activity_filter.*


class FilterActivity : BaseActivity(), FilterSkillsAdapter.OnFilterSkillsItemSelected,
    FilterEquipmentsAdapter.OnFilterEquipmentsItemSelected,
    FilterCategoryAdapter.OnCategoryItemSelected, View.OnClickListener {

    private var filterCategoryAdapter: FilterCategoryAdapter? = null
    private var filterEquipmentsAdapter: FilterEquipmentsAdapter? = null
    private var filterSkillsAdapter: FilterSkillsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        setClicks()
        initView()
        setCategoryAdapter()
        setSkillsAdapter()
        setEquipmentsAdapter()
    }

    private fun initView() {
        sb_range_selector.setProgress(1f, 100f)
        sb_range_selector.leftSeekBar.setIndicatorText("$1")
        sb_range_selector.rightSeekBar.setIndicatorText("$100")

        sb_range_selector.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onRangeChanged(
                view: RangeSeekBar,
                leftValue: Float,
                rightValue: Float,
                isFromUser: Boolean
            ) {

                view.leftSeekBar.setIndicatorText("$" + leftValue.toInt())
                view.rightSeekBar.setIndicatorText("$" + rightValue.toInt())


                txt_price_range.text = getString(
                    R.string.price_range_value,
                    leftValue.toInt().toString(),
                    rightValue.toInt().toString()
                )

            }

            override fun onStartTrackingTouch(view: RangeSeekBar, isLeft: Boolean) {}
            override fun onStopTrackingTouch(view: RangeSeekBar, isLeft: Boolean) {}
        })
    }

    private fun setClicks() {
        layout_category.setOnClickListener(this)
        layout_skill.setOnClickListener(this)
        layout_equipments.setOnClickListener(this)
        img_back.setOnClickListener(this)
        txt_apply.setOnClickListener(this)
    }

    private fun setSkillsAdapter() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        skills_recycle.layoutManager = layoutManager
        filterSkillsAdapter = FilterSkillsAdapter(this, this)
        skills_recycle.adapter = filterSkillsAdapter
        filterSkillsAdapter?.addItemsList(ListUtils.getSkillsBean())
    }

    private fun setEquipmentsAdapter() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        equipments_recycle.layoutManager = layoutManager
        filterEquipmentsAdapter = FilterEquipmentsAdapter(this, this)
        equipments_recycle.adapter = filterEquipmentsAdapter
        filterEquipmentsAdapter?.addItemsList(ListUtils.getEquipmentsBean())
    }

    private fun setCategoryAdapter() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        category_recycle.layoutManager = layoutManager
        filterCategoryAdapter = FilterCategoryAdapter(this, this)
        category_recycle.adapter = filterCategoryAdapter
        filterCategoryAdapter?.addItemsList(ListUtils.getCategoryBean())
    }

    override fun onFilterSkillsItemSelected() {

    }

    override fun onFilterEquipmentsItemSelected() {

    }

    override fun onCategoryItemSelected() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_apply -> {
                startActivity(Intent(this, FilterResultActivity::class.java))
            }
            R.id.layout_category -> {
                if (layout_category_recycle.visibility == View.GONE) {
                    IconUtils.setRightDrawableIcontoText(
                        this,
                        R.drawable.ic_up_chevron,
                        text_select_category
                    )
                    layout_category_recycle.visibility = View.VISIBLE
                } else {
                    IconUtils.setRightDrawableIcontoText(
                        this,
                        R.drawable.ic_drop_down,
                        text_select_category
                    )
                    layout_category_recycle.visibility = View.GONE
                }

            }
            R.id.layout_skill -> {
                if (layout_skills_recycle.visibility == View.GONE) {
                    IconUtils.setRightDrawableIcontoText(
                        this,
                        R.drawable.ic_up_chevron,
                        text_select_skills
                    )
                    layout_skills_recycle.visibility = View.VISIBLE
                } else {
                    IconUtils.setRightDrawableIcontoText(
                        this,
                        R.drawable.ic_drop_down,
                        text_select_skills
                    )
                    layout_skills_recycle.visibility = View.GONE
                }
            }
            R.id.layout_equipments -> {
                if (layout_equipments_recycle.visibility == View.GONE) {
                    IconUtils.setRightDrawableIcontoText(
                        this,
                        R.drawable.ic_up_chevron,
                        text_select_equipments
                    )
                    layout_equipments_recycle.visibility = View.VISIBLE
                } else {
                    IconUtils.setRightDrawableIcontoText(
                        this,
                        R.drawable.ic_drop_down,
                        text_select_equipments
                    )
                    layout_equipments_recycle.visibility = View.GONE
                }
            }
        }
    }

}