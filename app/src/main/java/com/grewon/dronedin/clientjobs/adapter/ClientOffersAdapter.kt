package com.grewon.dronedin.clientjobs.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grewon.dronedin.R
import com.grewon.dronedin.server.OffersDataBean
import kotlinx.android.synthetic.main.layout_offers_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class ClientOffersAdapter(
    val context: Context,
    private val onItemClickListeners: OnItemClickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListeners {

        fun onOffersItemClick(jobsDataBean: OffersDataBean.Result?)


    }


    var itemList = ArrayList<OffersDataBean.Result>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_client_offers_item,
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
            holder.itemView.setOnClickListener { onItemClickListeners.onOffersItemClick(null) }

        }


    }


    fun addItemsList(list: ArrayList<OffersDataBean.Result>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCategory = itemView.txt_category_name
        val textJobTitle = itemView.txt_job_title
        val textClientName = itemView.txt_client_name
        val textJobLocation = itemView.txt_job_location
        val textOfferedPrice = itemView.txt_offered_price
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}