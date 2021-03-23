package com.grewon.dronedin.pilotfindjobs.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grewon.dronedin.R
import com.grewon.dronedin.server.PilotJobsDataBean
import com.grewon.dronedin.utils.MapUtils
import com.grewon.dronedin.utils.TimeUtils
import kotlinx.android.synthetic.main.layout_find_pilot_jobs_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class PilotFindJobsAdapter(
    val context: Context,
    private val onItemClickListeners: OnItemClickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListeners {

        fun onItemClick(jobsDataBean: PilotJobsDataBean.Data?)

        fun onJobSaved(jobsDataBean: PilotJobsDataBean.Data?, adapterPosition: Int)

    }


    var itemList = ArrayList<PilotJobsDataBean.Data>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_find_pilot_jobs_item,
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
            holder.textJobTitle.text = item.jobTitle?.trim()
            holder.textJobDescription.text = item.jobDescription?.trim()
            holder.textClientName.text = item.userName?.trim()
            holder.textBudget.text =
                context.getString(R.string.price_string, item.totalPrice?.trim())

            if (item.jobLatitude != null && item.jobLongitude != null) {
                holder.textJobLocation.text = MapUtils.getStateName(
                    context,
                    item.jobLatitude.toDouble(),
                    item.jobLongitude.toDouble()
                )
            }

            holder.textDate.text = TimeUtils.getLocalTimes(context, item.jobDatecreated.toString())

            holder.itemView.setOnClickListener { onItemClickListeners.onItemClick(item) }


            holder.favouriteCheck.setOnClickListener {
                onItemClickListeners.onJobSaved(item, holder.adapterPosition)
            }

        }


    }


    fun addItemsList(list: ArrayList<PilotJobsDataBean.Data>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favouriteCheck = itemView.favourite_check
        val textCategory = itemView.txt_category_name
        val textJobTitle = itemView.txt_job_title
        val textJobDescription = itemView.txt_job_description
        val textClientName = itemView.txt_client_name
        val textJobLocation = itemView.txt_job_location
        val textBudget = itemView.txt_budget
        val textDate = itemView.txt_date
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }

    fun notifySavedItem(adapterPosition: Int) {
        for ((index, item) in itemList.withIndex()) {
            if (adapterPosition == index) {
                if (item.saveJob != null) {
                    item.saveJob = null
                } else {
                    item.saveJob = "1"
                }
            }
        }
        notifyDataSetChanged()
    }


}