package com.grewon.dronedin.message.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grewon.dronedin.R
import com.grewon.dronedin.server.MessagesDataBean
import com.grewon.dronedin.utils.IconUtils
import com.grewon.dronedin.utils.ScreenUtils
import com.grewon.dronedin.utils.TimeUtils
import kotlinx.android.synthetic.main.layout_messages_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class MessagesAdapter(
    val context: Context,
    private val onItemClickListeners: OnItemClickListeners
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListeners {

        fun onItemClick(jobsDataBean: MessagesDataBean.Data?)

        fun onDeleteItem(jobsDataBean: MessagesDataBean.Data?, adapterPosition: Int)

    }


    var itemList = ArrayList<MessagesDataBean.Data>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_messages_item,
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
                .load(item.userDetails?.profileImage)
                .apply(RequestOptions().placeholder(ScreenUtils.getRandomPlaceHolderColor()))
                .into(holder.imageUser)

            holder.textUserName.text = item.userDetails?.userName


            if (item.lastMessageType == "Text") {
                holder.textMessages.text = item.lastMessage
                holder.textMessages.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            } else if (item.lastMessageType == "Image") {
                holder.textMessages.text = item.lastMessageType
                IconUtils.setLeftDrawableIconToTextWithTint(
                    context,
                    R.drawable.ic_picture,
                    holder.textMessages
                )
            } else {
                holder.textMessages.text = item.lastMessageType
                IconUtils.setLeftDrawableIconToTextWithTint(
                    context,
                    R.drawable.ic_folder,
                    holder.textMessages
                )
            }

            holder.textDate.text = TimeUtils.getChatTimes(context, item.lastUpdate.toString())

            if (item.totalUnread != "0") {
                holder.textMessageCount.text = item.totalUnread
                holder.textMessageCount.visibility = View.VISIBLE
            } else {
                holder.textMessageCount.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                onItemClickListeners.onItemClick(item)
            }


        }


    }


    fun addItemsList(list: ArrayList<MessagesDataBean.Data>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageUser = itemView.img_user
        val textDate = itemView.txt_date
        val textMessageCount = itemView.txt_message_count
        val textMessages = itemView.txt_messages
        val textUserName = itemView.txt_user_name
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}