package com.grewon.dronedin.submitproposal.adapter

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
    val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


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


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val editTitle = itemView.edt_title
        val editPrice = itemView.edt_price
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}