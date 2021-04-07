package com.grewon.dronedin.clientjobs.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grewon.dronedin.R
import com.grewon.dronedin.server.ProposalsDataBean
import com.grewon.dronedin.utils.MapUtils
import com.grewon.dronedin.utils.TimeUtils
import kotlinx.android.synthetic.main.layout_client_proposals_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class ClientProposalsAdapter(
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
                R.layout.layout_client_proposals_item,
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
            holder.textPrice.text =
                context.getString(R.string.price_string, item.proposalTotalPrice)
            holder.textClientName.text = item.userName
            holder.textJobLocation.text = MapUtils.getStateName(
                context,
                item.userLatitude?.toDouble()!!, item.userLongitude?.toDouble()!!
            )
            if (item.rate != null) {
                holder.textRatings.text = item.rate
            }
            holder.txtDate.text = TimeUtils.getLocalTimes(context, item.proposalDatecreated.toString())

            holder.itemView.setOnClickListener { onItemClickListeners.onProposalsItemClick(item) }
        }


    }


    fun addItemsList(list: ArrayList<ProposalsDataBean.Data>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCategory = itemView.txt_category_name
        val textJobTitle = itemView.txt_job_title
        val textClientName = itemView.txt_pilot_name
        val textJobLocation = itemView.txt_location
        val textPrice = itemView.txt_price
        val textRatings = itemView.txt_ratings
        val txtDate = itemView.txt_date
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}