package com.grewon.dronedin.clientjobs.posted

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.clientjobs.clientoffers.ClientOffersActivity
import com.grewon.dronedin.pilotfindjobs.adapter.JobsImageAdapter
import com.grewon.dronedin.milestoneadapter.MileStoneAdapter
import com.grewon.dronedin.utils.ListUtils
import com.plumillonforge.android.chipview.Chip
import kotlinx.android.synthetic.main.activity_posted_job_details.*


class PostedJobDetailsActivity : BaseActivity(), View.OnClickListener {

    private var mileStoneAdapter: MileStoneAdapter? = null
    private var jobsImageAdapter: JobsImageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posted_job_details)
        initView()
        setClicks()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_edit.setOnClickListener(this)
        txt_delete.setOnClickListener(this)
        txt_see_list.setOnClickListener(this)
        img_toolbar.setOnClickListener(this)
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
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_edit -> {
                finish()
            }
            R.id.txt_delete -> {
                finish()
            }
            R.id.txt_see_list -> {
                startActivity(Intent(this, ClientProposalsActivity::class.java))
            }
            R.id.img_toolbar -> {
                openPopUpMenu()
            }
        }
    }

    private fun openPopUpMenu() {
        val popup = PopupMenu(this, img_toolbar)
        //Inflating the Popup using xml file
        //Inflating the Popup using xml file
        popup.menuInflater.inflate(R.menu.posted_job_bottom, popup.menu)

        //registering popup with OnMenuItemClickListener

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener {
            if (it.itemId == R.id.im_saved_pilots) {

            } else if (it.itemId == R.id.im_sent_invitations) {

            } else if (it.itemId == R.id.im_sent_offers) {
                startActivity(Intent(this, ClientOffersActivity::class.java))
            }
            true
        }

        popup.show()
    }
}