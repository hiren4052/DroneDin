package com.grewon.dronedin.pilotjobhistory.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.grewon.dronedin.R
import com.grewon.dronedin.server.JobsDataBean
import kotlinx.android.synthetic.main.layout_pilot_history_jobs_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class PilotJobsHistoryAdapter(
    val context: Context,
    private val onItemClickListeners: OnItemClickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListeners {

        fun onJobsHistoryItemClick(jobsDataBean: JobsDataBean.Result?)


    }


    var itemList = ArrayList<JobsDataBean.Result>()


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
        return 10
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //  val item = itemList[position]

        if (holder is ItemViewHolder) {

            if(position==1){
                holder.textjobStatus.background=ContextCompat.getDrawable(context,R.drawable.ic_round_completed_background)
                holder.textjobStatus.setTextColor(ContextCompat.getColor(context,R.color.completed_text_color))
                holder.textjobStatus.text=context.getText(R.string.completed)
            }else if(position==2){
                holder.textjobStatus.background=ContextCompat.getDrawable(context,R.drawable.ic_round_cancelled_background)
                holder.textjobStatus.setTextColor(ContextCompat.getColor(context,R.color.cancelled_text_color))
                holder.textjobStatus.text=context.getText(R.string.cancelled)
            }


        }


    }


    fun addItemsList(list: ArrayList<JobsDataBean.Result>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCategory = itemView.txt_category_name
        val textJobTitle = itemView.txt_job_title
        val textjobStatus = itemView.txt_jobs_status
        val textStartedDate = itemView.txt_started_date
        val textBudget = itemView.txt_budget
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}