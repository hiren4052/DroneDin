package com.grewon.dronedin.milestone.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.grewon.dronedin.R
import com.grewon.dronedin.server.MilestonesDataBean
import kotlinx.android.synthetic.main.layout_active_mile_stone_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class ActiveMileStoneAdapter(
    val context: Context, val onItemClickListeners: OnItemClickListeners
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    interface OnItemClickListeners {

        fun onMilestoneItemClick(jobsDataBean: MilestonesDataBean?)


    }

    var itemList = ArrayList<MilestonesDataBean>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_active_mile_stone_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //   val item = itemList[position]


        if (holder is ItemViewHolder) {

            holder.txtMileStonetitle.text =
                (position + 1).toString() + ". We need a photographer for a short term project."
            if (position == 2) {
                holder.textMilestoneStatus.background =
                    ContextCompat.getDrawable(context, R.drawable.ic_round_cancelled_background)
                holder.textMilestoneStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.cancelled_text_color
                    )
                )
                holder.textMilestoneStatus.text = context.getText(R.string.cancelled)
                holder.dateTitle.text = context.getText(R.string.cancelled_on)
                holder.itemView.setOnClickListener {
                    onItemClickListeners.onMilestoneItemClick(
                        MilestonesDataBean(milestoneStatus = "cancel")
                    )
                }
            } else if (position == 3) {
                holder.textMilestoneStatus.background =
                    ContextCompat.getDrawable(context, R.drawable.ic_round_active_background)
                holder.textMilestoneStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.active_text_color
                    )
                )
                holder.textMilestoneStatus.text = context.getText(R.string.active)
                holder.dateTitle.text = context.getText(R.string.started_on_)
                holder.itemView.setOnClickListener {
                    onItemClickListeners.onMilestoneItemClick(
                        MilestonesDataBean(milestoneStatus = "active")
                    )
                }
            } else {
                holder.itemView.setOnClickListener {
                    onItemClickListeners.onMilestoneItemClick(
                        MilestonesDataBean(milestoneStatus = "complete")
                    )
                }
            }

        }


    }


    fun addItemsList(list: ArrayList<MilestonesDataBean>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtMileStonetitle = itemView.txt_mile_stone_title
        val txtMileStonePrice = itemView.txt_mile_stone_price
        val textDate = itemView.txt_started_date
        val dateTitle = itemView.title_date
        val textMilestoneStatus = itemView.txt_milestone_status
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}