package com.grewon.dronedin.pilotjobhistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.message.ChatActivity
import com.grewon.dronedin.milestoneadapter.ActiveMileStoneAdapter
import com.grewon.dronedin.milestoneadapter.MilestoneDetailActivity
import com.grewon.dronedin.milestoneadapter.SubmitMilestoneActivity
import com.grewon.dronedin.pilotfindjobs.adapter.JobsImageAdapter
import com.grewon.dronedin.server.MilestonesDataBean
import com.grewon.dronedin.utils.ListUtils
import com.plumillonforge.android.chipview.Chip
import kotlinx.android.synthetic.main.activity_pilot_history_details.*

class PilotHistoryDetailsActivity : BaseActivity(), View.OnClickListener,
    ActiveMileStoneAdapter.OnItemClickListeners {
    private var mileStoneAdapter: ActiveMileStoneAdapter? = null
    private var jobsImageAdapter: JobsImageAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilot_history_details)
        initView()
        setClicks()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_submit_review.setOnClickListener(this)
        txt_message.setOnClickListener(this)
    }

    private fun initView() {
        setMileStoneAdapter()
        setImageAdapter()

        val skillChipList: List<Chip> = ListUtils.getSkillsBean()
        chip_skills.chipList = skillChipList

        val equipmentsChipList: List<Chip> = ListUtils.getEquipmentsBean()
        chip_equipments.chipList = equipmentsChipList

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
            R.id.txt_submit_review -> {
                finish()
            }
            R.id.txt_message -> {
                startActivity(Intent(this, ChatActivity::class.java))
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