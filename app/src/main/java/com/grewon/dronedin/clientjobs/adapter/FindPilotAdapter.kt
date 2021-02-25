package com.grewon.dronedin.clientjobs.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grewon.dronedin.R
import com.grewon.dronedin.server.JobsDataBean
import com.grewon.dronedin.utils.ListUtils
import com.plumillonforge.android.chipview.Chip
import kotlinx.android.synthetic.main.activity_find_jobs_details.*
import kotlinx.android.synthetic.main.layout_find_pilot_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class FindPilotAdapter(
    val context: Context,
    private val onItemClickListeners: OnItemClickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListeners {

        fun onPilotItemClick(jobsDataBean: JobsDataBean.Result?)


    }


    var itemList = ArrayList<JobsDataBean.Result>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_find_pilot_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //  val item = itemList[position]

        if (holder is ItemViewHolder) {

            val skillChipList: List<Chip> = ListUtils.getSkillsBean()
            holder.chipSkills.chipList = skillChipList

            val equipmentsChipList: List<Chip> = ListUtils.getEquipmentsBean()
            holder.chipEquipments.chipList = equipmentsChipList

            holder.itemView.setOnClickListener { onItemClickListeners.onPilotItemClick(null) }


        }


    }


    fun addItemsList(list: ArrayList<JobsDataBean.Result>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favouriteCheck = itemView.favourite_check
        val txtRating = itemView.txt_ratings
        val txtUseName = itemView.txt_user_name
        val textJobLocation = itemView.txt_job_location
        val textPrice = itemView.txt_price
        val chipSkills = itemView.chip_skills
        val chipEquipments = itemView.chip_equipments
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}