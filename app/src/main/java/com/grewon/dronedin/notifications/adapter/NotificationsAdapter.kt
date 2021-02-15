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

        fun onItemClick(jobsDataBean: NotificationDataBean.Result?)

        fun onDeleteItem(jobsDataBean: NotificationDataBean.Result?, adapterPosition: Int)

    }


    var itemList = ArrayList<NotificationDataBean.Result>()


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
        return 10
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //  val item = itemList[position]

        if (holder is ItemViewHolder) {

            Glide.with(context)
                .load(AppConstant.ORIGINAL_IMAGE_URL + "")
                .apply(RequestOptions().placeholder(ScreenUtils.getRandomNotificationImage()))
                .into(holder.typeImage)


        }


    }


    fun addItemsList(list: ArrayList<NotificationDataBean.Result>) {
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