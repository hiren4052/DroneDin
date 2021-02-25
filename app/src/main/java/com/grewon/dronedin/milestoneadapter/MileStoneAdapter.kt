package com.grewon.dronedin.milestoneadapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grewon.dronedin.R
import com.grewon.dronedin.server.MilestonesDataBean
import kotlinx.android.synthetic.main.layout_mile_stone_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class MileStoneAdapter(
    val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var itemList = ArrayList<MilestonesDataBean.Result>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_mile_stone_item,
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

            holder.txtMileStonetitle.text = "Milestone " + (position + 1)
            holder.txtMileStonePrice.text =
                context.getString(R.string.price_string, (position * 10).toString())

        }


    }


    fun addItemsList(list: ArrayList<MilestonesDataBean.Result>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtMileStonetitle = itemView.txt_mile_stone_title
        val txtMileStonePrice = itemView.txt_mile_stone_price
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}