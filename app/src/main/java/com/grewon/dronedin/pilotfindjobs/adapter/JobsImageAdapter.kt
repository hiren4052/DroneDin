package com.grewon.dronedin.pilotfindjobs.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.grewon.dronedin.R
import com.grewon.dronedin.server.JobsImageDataBean
import kotlinx.android.synthetic.main.layout_jobs_image_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class JobsImageAdapter(
    val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var itemList = ArrayList<JobsImageDataBean.Result>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_jobs_image_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //   val item = itemList[position]


        if (holder is ItemViewHolder) {

            Glide.with(context)
                .load("https://www.sustainableplaces.eu/wp-content/uploads/2017/02/SmartBuilding.jpg")
                .into(holder.jobsImage)


        }


    }


    fun addItemsList(list: ArrayList<JobsImageDataBean.Result>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val jobsImage = itemView.jobs_image
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}