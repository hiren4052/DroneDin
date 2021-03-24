package com.grewon.dronedin.offers.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grewon.dronedin.R
import com.grewon.dronedin.server.OffersDataBean
import com.grewon.dronedin.utils.MapUtils
import com.grewon.dronedin.utils.TimeUtils
import kotlinx.android.synthetic.main.layout_offers_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class OffersAdapter(
    val context: Context,
    private val onItemClickListeners: OnItemClickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListeners {

        fun onOffersItemClick(jobsDataBean: OffersDataBean.Data?)


    }


    var itemList = ArrayList<OffersDataBean.Data>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_offers_item,
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
            holder.textClientName.text = item.userName
            holder.textJobLocation.text =
                MapUtils.getStateName(
                    context, item.jobLatitude?.toDouble()!!,
                    item.jobLongitude?.toDouble()!!
                )


            holder.textDate.text =
                TimeUtils.getLocalTimes(context, item.offerDatecreated.toString())

            holder.textOfferedPrice.text =
                context.getString(R.string.price_string, item.offerTotalPrice)
            holder.itemView.setOnClickListener { onItemClickListeners.onOffersItemClick(item) }

        }


    }


    fun addItemsList(list: ArrayList<OffersDataBean.Data>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCategory = itemView.txt_category_name
        val textJobTitle = itemView.txt_job_title
        val textClientName = itemView.txt_client_name
        val textJobLocation = itemView.txt_job_location
        val textOfferedPrice = itemView.txt_offered_price
        val textDate = itemView.txt_date
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}