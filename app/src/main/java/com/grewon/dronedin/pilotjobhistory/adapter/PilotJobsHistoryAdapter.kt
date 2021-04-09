package com.grewon.dronedin.pilotjobhistory.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.enum.OFFERS_STATUS
import com.grewon.dronedin.server.JobsDataBean
import com.grewon.dronedin.server.PilotJobHistoryBean
import com.grewon.dronedin.utils.TimeUtils
import kotlinx.android.synthetic.main.layout_pilot_history_jobs_item.view.title_date
import kotlinx.android.synthetic.main.layout_pilot_history_jobs_item.view.txt_budget
import kotlinx.android.synthetic.main.layout_pilot_history_jobs_item.view.txt_category_name
import kotlinx.android.synthetic.main.layout_pilot_history_jobs_item.view.txt_job_title
import kotlinx.android.synthetic.main.layout_pilot_history_jobs_item.view.txt_jobs_status
import kotlinx.android.synthetic.main.layout_pilot_history_jobs_item.view.txt_started_date


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class PilotJobsHistoryAdapter(
    val context: Context,
    private val onItemClickListeners: OnItemClickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListeners {

        fun onJobsHistoryItemClick(jobsDataBean: PilotJobHistoryBean.Data.History)


    }


    var itemList = ArrayList<PilotJobHistoryBean.Data.History>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_pilot_history_jobs_item,
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
            holder.textJobTitle.text = item.offerTitle
            holder.textBudget.text = context.getString(R.string.price_string, item.offerTotalPrice)
            holder.textStartedDate.text =
                TimeUtils.getServerToAppDate(item.offerStatusChangeDate.toString())


            if (item.offerStatus == OFFERS_STATUS.active.name) {
                holder.textjobStatus.background =
                    ContextCompat.getDrawable(context, R.drawable.ic_round_active_background)
                holder.textjobStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.active_text_color
                    )
                )
                holder.textjobStatus.text = context.getString(R.string.active)
                holder.dateTitle.text = context.getString(R.string.started_on_)
            } else if (item.offerStatus == OFFERS_STATUS.completed.name) {
                holder.textjobStatus.background =
                    ContextCompat.getDrawable(context, R.drawable.ic_round_completed_background)
                holder.textjobStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.completed_text_color
                    )
                )
                holder.textjobStatus.text = context.getString(R.string.completed)
                holder.dateTitle.text = context.getString(R.string.completed_on)
            } else if (item.offerStatus == OFFERS_STATUS.cancelled.name) {
                holder.textjobStatus.background =
                    ContextCompat.getDrawable(context, R.drawable.ic_round_cancelled_background)
                holder.textjobStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.cancelled_text_color
                    )
                )
                holder.textjobStatus.text = context.getString(R.string.cancelled)
                holder.dateTitle.text = context.getString(R.string.completed_on)
            }

            holder.itemView.setOnClickListener { onItemClickListeners.onJobsHistoryItemClick(item) }

        }


    }


    fun addItemsList(list: ArrayList<PilotJobHistoryBean.Data.History>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCategory = itemView.txt_category_name
        val textJobTitle = itemView.txt_job_title
        val textjobStatus = itemView.txt_jobs_status
        val textStartedDate = itemView.txt_started_date
        val textBudget = itemView.txt_budget
        val dateTitle = itemView.title_date

    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}