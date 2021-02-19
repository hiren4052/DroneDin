package com.grewon.dronedin.message.dapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.server.ChatDataBean
import com.grewon.dronedin.utils.ListUtils
import com.grewon.dronedin.utils.ScreenUtils
import kotlinx.android.synthetic.main.layout_chat_date_item.view.*
import kotlinx.android.synthetic.main.layout_chat_my_image_messages_item.view.*
import kotlinx.android.synthetic.main.layout_chat_my_messages_item.view.*
import kotlinx.android.synthetic.main.layout_chat_other_image_messages_item.view.*
import kotlinx.android.synthetic.main.layout_chat_other_messages_item.view.*
import kotlinx.android.synthetic.main.layout_chat_other_messages_item.view.txt_other_date


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class ChatAdapter(
    val context: Context,
    private val onItemClickListeners: OnItemClickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val MY_MESSAGE = 0
    val OTHER_MESSAGE = 1
    val OTHER_IMAGE_MESSAGE = 3
    val MY_IMAGE_MESSAGE = 4
    val DATE_VIEW_ITEM = 5

    interface OnItemClickListeners {

        fun onImageItemClick(jobsDataBean: ChatDataBean.Result?)

        fun onImageDownloadClick(jobsDataBean: ChatDataBean.Result?)

    }


    var itemList = ArrayList<ChatDataBean.Result>()

    override fun getItemViewType(position: Int): Int {
        if (itemList[position].messageType != "date") {
            if (itemList[position].sendrId == 0.toLong()) {
                if (itemList[position].messageType == "message") {
                    return MY_MESSAGE
                } else if (itemList[position].messageType == "image") {
                    return MY_IMAGE_MESSAGE
                }
            } else {
                if (itemList[position].messageType == "message") {
                    return OTHER_MESSAGE
                } else if (itemList[position].messageType == "image") {
                    return OTHER_IMAGE_MESSAGE
                }
            }
        } else {
            return DATE_VIEW_ITEM
        }
        return MY_MESSAGE
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == MY_MESSAGE) {
            return MyMessageViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.layout_chat_my_messages_item,
                    parent,
                    false
                )
            )
        } else if (viewType == MY_IMAGE_MESSAGE) {
            return MyImageMessageViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.layout_chat_my_image_messages_item,
                    parent,
                    false
                )
            )
        } else if (viewType == OTHER_MESSAGE) {
            return OtherMessageViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.layout_chat_other_messages_item,
                    parent,
                    false
                )
            )
        } else if (viewType == OTHER_IMAGE_MESSAGE) {
            return OtherImageMessageViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.layout_chat_other_image_messages_item,
                    parent,
                    false
                )
            )
        }
        else if (viewType == DATE_VIEW_ITEM) {
            return DateViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.layout_chat_date_item,
                    parent,
                    false
                )
            )
        }
        return MyMessageViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_chat_my_messages_item,
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

        if (holder is MyMessageViewHolder) {


        } else if (holder is MyImageMessageViewHolder) {
            Glide.with(context)
                .load( "https://i.pinimg.com/originals/7d/85/43/7d854312c50a516d88dfda706245d67b.jpg")
                .apply(RequestOptions().placeholder(ScreenUtils.getRandomPlaceHolderColor()))
                .into(holder.myImage)
        } else if (holder is OtherMessageViewHolder) {

        } else if (holder is OtherImageMessageViewHolder) {
            Glide.with(context)
                .load("https://i.pinimg.com/originals/7d/85/43/7d854312c50a516d88dfda706245d67b.jpg")
                .apply(RequestOptions().placeholder(ScreenUtils.getRandomPlaceHolderColor()))
                .into(holder.otherImage)
        }


    }

    fun addMessageItem(chatBean: ChatDataBean.Result) {
        itemList.add(0, chatBean)
        notifyItemInserted(itemList.size - 1)
    }


    fun addMessageItemsList(list: ArrayList<ChatDataBean.Result>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun addOffsetMessageItemsList(list: ArrayList<ChatDataBean.Result>) {
        itemList.addAll(0, list)
        notifyItemRangeChanged(itemList.size - list.size, itemList.size)
    }


    class MyMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMyMessage = itemView.txt_my_message
        val textMyDate = itemView.txt_my_date

    }

    class MyImageMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val myImage = itemView.image_my_message
        val textMyImageDate = itemView.txt_my_image_date
    }

    class OtherMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMyMessage = itemView.txt_other_message
        val textMyDate = itemView.txt_other_date

    }

    class OtherImageMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val otherImage = itemView.image_other_message
        val textOtherImageDate = itemView.txt_image_other_date

    }

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtMessageDate = itemView.txt_messages_date

    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}