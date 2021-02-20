package com.grewon.dronedin.pilotfindjobs.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grewon.dronedin.R
import com.grewon.dronedin.server.JobsDataBean
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

        fun onItemClick(jobsDataBean: JobsDataBean.Result?)



    }


    var itemList = ArrayList<JobsDataBean.Result>()


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
        return 10
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
      //  val item = itemList[position]

        if (holder is ItemViewHolder) {







        }


    }


    fun addItemsList(list: ArrayList<JobsDataBean.Result>) {
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
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}