package com.grewon.dronedin.membership.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.grewon.dronedin.R
import com.grewon.dronedin.server.MemberShipBean
import kotlinx.android.synthetic.main.layout_membership_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class MembershipAdapter(
    val context: Context,
    private val onItemClickListeners: OnItemClickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListeners {

        fun onMembershipItemClick(jobsDataBean: MemberShipBean.Data?)


    }


    var itemList = ArrayList<MemberShipBean.Data>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_membership_item,
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


            Glide.with(context).load(item.packageImage).into(holder.packageImage)

            holder.packageName.text = item.packageName
            holder.txtPrice.text = context.getString(R.string.price_string, item.packagePrice)
            holder.packageType.text = context.getString(R.string.package_type, item.packageType)
            holder.packageDescription.text = item.packageDescription


            holder.itemView.setOnClickListener { onItemClickListeners.onMembershipItemClick(item) }
        }


    }


    fun addItemsList(list: ArrayList<MemberShipBean.Data>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val packageType = itemView.package_type
        val packageDescription = itemView.package_description
        val packageName = itemView.package_name
        val packageImage = itemView.package_image
        val txtPrice = itemView.txt_price
    }


}