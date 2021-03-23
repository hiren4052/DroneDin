package com.grewon.dronedin.proposals.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grewon.dronedin.R
import com.grewon.dronedin.server.ProposalsDataBean
import com.grewon.dronedin.utils.MapUtils
import com.grewon.dronedin.utils.TimeUtils
import kotlinx.android.synthetic.main.layout_proposals_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class ProposalsAdapter(
    val context: Context,
    private val onItemClickListeners: OnItemClickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListeners {

        fun onProposalsItemClick(jobsDataBean: ProposalsDataBean.Data?)


    }


    var itemList = ArrayList<ProposalsDataBean.Data>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_proposals_item,
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
            holder.textJobTitle.text = item.proposalTitle
            holder.textClientName.text = item.userName
            holder.textJobLocation.text =
                MapUtils.getStateName(
                    context, item.jobLatitude?.toDouble()!!,
                    item.jobLongitude?.toDouble()!!
                )


            holder.textDate.text = TimeUtils.getLocalTimes(context, item.proposalDatecreated.toString())

            holder.textPrice.text =
                context.getString(R.string.price_string, item.proposalTotalPrice)

            holder.itemView.setOnClickListener { onItemClickListeners.onProposalsItemClick(null) }
        }


    }


    fun addItemsList(list: ArrayList<ProposalsDataBean.Data>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCategory = itemView.txt_category_name
        val textJobTitle = itemView.txt_job_title
        val textClientName = itemView.txt_client_name
        val textJobLocation = itemView.txt_job_location
        val textPrice = itemView.txt_price
        val textDate = itemView.txt_date
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}