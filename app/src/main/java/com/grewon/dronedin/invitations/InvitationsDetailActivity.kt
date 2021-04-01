package com.grewon.dronedin.invitations

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.extraadapter.ChipEquipmentsAdapter
import com.grewon.dronedin.extraadapter.ChipSkillsAdapter
import com.grewon.dronedin.message.ChatActivity
import com.grewon.dronedin.attachments.JobAttachmentsAdapter
import com.grewon.dronedin.milestone.adapter.MileStoneAdapter
import com.grewon.dronedin.submitproposal.SubmitProposalActivity
import com.grewon.dronedin.utils.ListUtils
import kotlinx.android.synthetic.main.activity_invitations_detail.*

class InvitationsDetailActivity : BaseActivity(), View.OnClickListener {

    private var mileStoneAdapter: MileStoneAdapter? = null
    private var jobsImageAdapter: JobAttachmentsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invitations_detail)
        initView()
        setClicks()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_send_proposal.setOnClickListener(this)
        txt_message.setOnClickListener(this)
    }

    private fun initView() {
        setMileStoneAdapter()
        setImageAdapter()

        setSkillsAdapter()
        setEquipmentsData()

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

    private fun setImageAdapter() {
        image_recycle.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        jobsImageAdapter = JobAttachmentsAdapter(this)
        image_recycle.adapter = jobsImageAdapter

    }

    private fun setMileStoneAdapter() {
        mile_stone_recycle.layoutManager = LinearLayoutManager(this)
        mileStoneAdapter = MileStoneAdapter(this)
        mile_stone_recycle.adapter = mileStoneAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_message -> {
//                startActivity(Intent(this, ChatActivity::class.java).putExtra(
//                    AppConstant.ID,
//                    proposalsDetailBean?.pilot?.userId
//                ))
            }
            R.id.txt_send_proposal -> {
                startActivity(Intent(this, SubmitProposalActivity::class.java))
            }
        }
    }
}