package com.grewon.dronedin.notifications.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.server.NotificationDataBean
import com.grewon.dronedin.utils.ListUtils
import com.grewon.dronedin.utils.ScreenUtils
import com.grewon.dronedin.utils.TimeUtils
import kotlinx.android.synthetic.main.layout_notifcation_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class NotificationsAdapter(
    val context: Context,
    private val onItemClickListeners: OnItemClickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListeners {

        fun onItemClick(jobsDataBean: NotificationDataBean.Data?)

        fun onDeleteItem(jobsDataBean: NotificationDataBean.Data?, adapterPosition: Int)

    }


    var itemList = ArrayList<NotificationDataBean.Data>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_notifcation_item,
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

            Glide.with(context)
                .load(ScreenUtils.getRandomNotificationImage())
                .apply(RequestOptions().placeholder(ScreenUtils.getRandomNotificationImage()))
                .into(holder.typeImage)

            holder.textMessages.text = item.notificationMessage
            holder.textDate.text =
                TimeUtils.getServerToAppDate(item.notificationDatecreated.toString())

            holder.itemView.setOnClickListener { onItemClickListeners.onItemClick(item) }

        }


    }


    fun addItemsList(list: ArrayList<NotificationDataBean.Data>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeImage = itemView.img_type
        val textDate = itemView.txt_date
        val textMessages = itemView.txt_messages
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}