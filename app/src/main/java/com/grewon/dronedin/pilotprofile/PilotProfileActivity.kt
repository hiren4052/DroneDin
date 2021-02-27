package com.grewon.dronedin.pilotprofile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.extraadapter.ChipEquipmentsAdapter
import com.grewon.dronedin.extraadapter.ChipSkillsAdapter
import com.grewon.dronedin.message.ChatActivity
import com.grewon.dronedin.review.adapter.WhiteScreenReviewsAdapter
import com.grewon.dronedin.utils.ListUtils
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_pilot_profile.*

class PilotProfileActivity : BaseActivity(), View.OnClickListener {

    private var reviewsAdapter: WhiteScreenReviewsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilot_profile)
        setClicks()
        initView()
    }

    private fun setClicks() {
        im_back.setOnClickListener(this)
        im_message.setOnClickListener(this)
    }

    private fun initView() {

        Glide.with(this).load(R.drawable.img_dummy)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(22, 3)))
            .into(blur_img_user)

        segment_group.setOnPositionChangedListener {
            setSegmentViewVisibility(it)
        }

        setSkillsAdapter()
        setEquipmentsData()
        setReviewAdapter()

    }

    private fun setSegmentViewVisibility(position: Int) {
        first_segment_layout.visibility = if (position == 0) View.VISIBLE else View.GONE
        second_segment_layout.visibility = if (position == 1) View.VISIBLE else View.GONE
        third_segment_layout.visibility = if (position == 2) View.VISIBLE else View.GONE
    }

    private fun setSkillsAdapter() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        chip_skills.layoutManager = layoutManager
        val skillsAdapter = ChipSkillsAdapter(this, R.color.gray_f4, R.color.gray_71)
        chip_skills.adapter = skillsAdapter
        skillsAdapter.addItemsList(ListUtils.getSkillsBean())

    }

    private fun setEquipmentsData() {

        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        chip_equipments.layoutManager = layoutManager
        val equipmentsAdapter = ChipEquipmentsAdapter(this, R.color.light_sky_blue, R.color.gray_71)
        chip_equipments.adapter = equipmentsAdapter
        equipmentsAdapter.addItemsList(ListUtils.getEquipmentsBean())

    }

    private fun setReviewAdapter() {
        review_recycle.layoutManager = LinearLayoutManager(this)
        reviewsAdapter = WhiteScreenReviewsAdapter(this)
        review_recycle.adapter = reviewsAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_back -> {
                finish()
            }
            R.id.im_message -> {
                startActivity(Intent(this, ChatActivity::class.java))
            }
        }
    }
}