package com.grewon.dronedin.offers

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.message.ChatActivity
import com.grewon.dronedin.pilotfindjobs.adapter.JobsImageAdapter
import com.grewon.dronedin.pilotfindjobs.adapter.MileStoneAdapter
import com.grewon.dronedin.utils.ListUtils
import com.plumillonforge.android.chipview.Chip
import kotlinx.android.synthetic.main.activity_offers_detail.chip_equipments
import kotlinx.android.synthetic.main.activity_offers_detail.chip_skills
import kotlinx.android.synthetic.main.activity_offers_detail.image_recycle
import kotlinx.android.synthetic.main.activity_offers_detail.img_back
import kotlinx.android.synthetic.main.activity_offers_detail.mile_stone_recycle
import kotlinx.android.synthetic.main.activity_offers_detail.txt_message
import kotlinx.android.synthetic.main.activity_offers_detail.txt_send_proposal

class OffersDetailActivity : BaseActivity(), View.OnClickListener {
    private var mileStoneAdapter: MileStoneAdapter? = null
    private var jobsImageAdapter: JobsImageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offers_detail)
        setClicks()
        initView()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_message.setOnClickListener(this)
        txt_send_proposal.setOnClickListener(this)
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
        mileStoneAdapter = MileStoneAdapter(this)
        mile_stone_recycle.adapter = mileStoneAdapter
    }

    override fun onClick(v: View?) {
        when (R.id.img_back) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_message -> {
                startActivity(Intent(this, ChatActivity::class.java))
            }
            R.id.txt_send_proposal -> {
                startActivity(Intent(this, ChatActivity::class.java))
            }
        }
    }
}