package com.grewon.dronedin.pilotactivejobs

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.extraadapter.ChipEquipmentsAdapter
import com.grewon.dronedin.extraadapter.ChipSkillsAdapter
import com.grewon.dronedin.milestone.adapter.ActiveMileStoneAdapter
import com.grewon.dronedin.milestone.MilestoneDetailActivity
import com.grewon.dronedin.milestone.SubmitMilestoneActivity
import com.grewon.dronedin.pilotfindjobs.adapter.JobsImageAdapter
import com.grewon.dronedin.server.MilestonesDataBean
import com.grewon.dronedin.utils.ListUtils
import kotlinx.android.synthetic.main.activity_pilot_active_jobs_detail.*


class PilotActiveJobsDetailActivity : BaseActivity(), View.OnClickListener,
    ActiveMileStoneAdapter.OnItemClickListeners {

    private var mileStoneAdapter: ActiveMileStoneAdapter? = null
    private var jobsImageAdapter: JobsImageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilot_active_jobs_detail)
        initView()
        setClicks()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
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
        jobsImageAdapter = JobsImageAdapter(this)
        image_recycle.adapter = jobsImageAdapter

    }

    private fun setMileStoneAdapter() {
        mile_stone_recycle.layoutManager = LinearLayoutManager(this)
        mileStoneAdapter = ActiveMileStoneAdapter(this, this)
        mile_stone_recycle.adapter = mileStoneAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }

        }
    }

    override fun onMilestoneItemClick(jobsDataBean: MilestonesDataBean.Result?) {
        if (jobsDataBean?.userProfileName == "active") {
            startActivity(Intent(this, SubmitMilestoneActivity::class.java))
        } else {
            startActivity(Intent(this, MilestoneDetailActivity::class.java))
        }
    }
}