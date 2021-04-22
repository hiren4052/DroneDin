package com.grewon.dronedin.milestone.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grewon.dronedin.R
import com.grewon.dronedin.server.CreateMilestoneBean
import kotlinx.android.synthetic.main.layout_create_mile_stone_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class CreateMileStoneAdapter(
    val context: Context, var onItemRemoveClickListeners: OnRemoveItemClickListeners
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnRemoveItemClickListeners {
        fun onItemRemove(adapterPosition: Int)
    }


    var itemList = ArrayList<CreateMilestoneBean>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_create_mile_stone_item,
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

            holder.txtCount.text = (position + 1).toString() + "."
            holder.txtMilstoneDesc.text = item.details
            holder.txtPrice.text = context.getString(R.string.price_string, item.price)

            holder.imgClose.setOnClickListener {
                removeItem(position)
                onItemRemoveClickListeners.onItemRemove(holder.adapterPosition)
            }

        }


    }


    fun addItemsList(list: ArrayList<CreateMilestoneBean>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun addItems(list: CreateMilestoneBean) {
        itemList.add(list)
        notifyDataSetChanged()
    }

    fun getPriceString(): String {
        if (itemList.size > 0) {
            var totalPrice: Double = 0.0
            for (item in itemList) {
                totalPrice += item.price.toDouble()
            }
            return totalPrice.toString()
        }
        return ""
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtCount = itemView.txt_count
        val txtMilstoneDesc = itemView.txt_milestone_desc
        val imgClose = itemView.img_close
        val txtPrice = itemView.txt_price
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}