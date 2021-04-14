package com.grewon.dronedin.dispute.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.grewon.dronedin.R
import com.grewon.dronedin.enum.DISPUTE_STATUS
import com.grewon.dronedin.enum.OFFERS_STATUS
import com.grewon.dronedin.server.DisputeBean
import com.grewon.dronedin.utils.MapUtils
import com.grewon.dronedin.utils.TimeUtils
import kotlinx.android.synthetic.main.layout_dispute_item.view.*

/**
 * Created by Jeff Klima on 2019-08-20.
 */


class DisputeAdapter(
    val context: Context, private val isPilot: Boolean,
    private val onItemClickListeners: OnItemClickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListeners {

        fun onDisputeItemClick(jobsDataBean: DisputeBean.Data?)


    }


    var itemList = ArrayList<DisputeBean.Data>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_dispute_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]

        if (holder is ItemViewHolder) {

            holder.textCategory.text = item.categoryName
            holder.textJobTitle.text = item.jobTitle

            if (isPilot) {
                holder.titleUserType.text = context.getString(R.string.client_)
                holder.txtUserName.text = item.clientName

            } else {
                holder.titleUserType.text = context.getString(R.string.pilot_)
                holder.txtUserName.text = item.pilotName
            }

            holder.textAmount.text = context.getString(R.string.price_string, item.milestonePrice)
            holder.textDisputeStatus.text = item.disputeStatus

            if (item.disputeStatus == DISPUTE_STATUS.resolved.name) {
                holder.textDisputeStatus.background =
                    ContextCompat.getDrawable(context, R.drawable.ic_round_active_background)
                holder.textDisputeStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.active_text_color
                    )
                )
            } else if (item.disputeStatus == DISPUTE_STATUS.pending.name) {
                holder.textDisputeStatus.background =
                    ContextCompat.getDrawable(context, R.drawable.ic_round_cancelled_background)
                holder.textDisputeStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.cancelled_text_color
                    )
                )

            }

            holder.itemView.setOnClickListener { onItemClickListeners.onDisputeItemClick(item) }
        }


    }


    fun addItemsList(list: ArrayList<DisputeBean.Data>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCategory = itemView.txt_category_name
        val textJobTitle = itemView.txt_job_title
        val textDisputeStatus = itemView.txt_dispute_status
        val textAmount = itemView.txt_amount
        val titleUserType = itemView.title_user_type
        val txtUserName = itemView.txt_user_name
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}