package com.grewon.dronedin.message.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grewon.dronedin.R
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.helper.FileValidationUtils
import com.grewon.dronedin.server.ChatDataBean
import com.grewon.dronedin.utils.ListUtils
import com.grewon.dronedin.utils.ScreenUtils
import com.grewon.dronedin.utils.TimeUtils
import kotlinx.android.synthetic.main.layout_chat_date_item.view.*
import kotlinx.android.synthetic.main.layout_chat_my_image_messages_item.view.*
import kotlinx.android.synthetic.main.layout_chat_my_messages_item.view.*
import kotlinx.android.synthetic.main.layout_chat_other_image_messages_item.view.*
import kotlinx.android.synthetic.main.layout_chat_other_messages_item.view.*
import java.util.*
import kotlin.collections.ArrayList


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
    val OTHER_IMAGE_MESSAGE = 2
    val MY_IMAGE_MESSAGE = 3
    val DATE_VIEW_ITEM = 4

    interface OnItemClickListeners {

        fun onImageItemClick(jobsDataBean: ChatDataBean.Data?)

        fun onImageDownloadClick(jobsDataBean: ChatDataBean.Data?)

    }


    var itemList = ArrayList<ChatDataBean.Data>()

    override fun getItemViewType(position: Int): Int {
        if (itemList[position].msgType != "Date") {
            if (itemList[position].senderId == DroneDinApp.getAppInstance().getUserId()) {
                if (itemList[position].msgType == "Text") {
                    return MY_MESSAGE
                } else if (itemList[position].msgType == "Image" || itemList[position].msgType == "File") {
                    return MY_IMAGE_MESSAGE
                }
            } else {
                if (itemList[position].msgType == "Text") {
                    return OTHER_MESSAGE
                } else if (itemList[position].msgType == "Image" || itemList[position].msgType == "File") {
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
        } else if (viewType == DATE_VIEW_ITEM) {
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
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]

        when (holder) {
            is MyMessageViewHolder -> {

                holder.textMyMessage.text = item.msg?.trim()
                holder.textMyDate.text = TimeUtils.getMessageTime(item.chatMsgDatecreated.toString())

            }
            is MyImageMessageViewHolder -> {
                if (ListUtils.getImageExtensionList()
                        .contains(
                            FileValidationUtils.getExtension(item.msg).toString().toLowerCase(
                                Locale.ROOT
                            )
                        )
                ) {
                    Glide.with(context)
                        .load(item.msg)
                        .apply(RequestOptions().placeholder(ScreenUtils.getRandomPlaceHolderColor()))
                        .into(holder.myImage)
                } else {
                    Glide.with(context)
                        .load(R.drawable.ic_attachment_background)
                        .apply(RequestOptions().placeholder(ScreenUtils.getRandomPlaceHolderColor()))
                        .into(holder.myImage)
                }

                holder.itemView.setOnClickListener { onItemClickListeners.onImageItemClick(item) }
                holder.textMyImageDate.text =
                    TimeUtils.getMessageTime(item.chatMsgDatecreated.toString())
            }
            is OtherMessageViewHolder -> {
                holder.textMyMessage.text = item.msg?.trim()
                holder.textMyDate.text = TimeUtils.getMessageTime(item.chatMsgDatecreated.toString())
            }
            is OtherImageMessageViewHolder -> {
                if (ListUtils.getImageExtensionList()
                        .contains(
                            FileValidationUtils.getExtension(item.msg).toString().toLowerCase(
                                Locale.ROOT
                            )
                        )
                ) {
                    Glide.with(context)
                        .load(item.msg)
                        .apply(RequestOptions().placeholder(ScreenUtils.getRandomPlaceHolderColor()))
                        .into(holder.otherImage)
                } else {

                    Glide.with(context)
                        .load(R.drawable.ic_attachment_background)
                        .apply(RequestOptions().placeholder(ScreenUtils.getRandomPlaceHolderColor()))
                        .into(holder.otherImage)

                }
                holder.itemView.setOnClickListener { onItemClickListeners.onImageItemClick(item) }
                holder.textOtherImageDate.text =
                    TimeUtils.getMessageTime(item.chatMsgDatecreated.toString())
            }
            is DateViewHolder -> {
                holder.txtMessageDate.text = TimeUtils.getMessageHeaderConvertDate(item.chatMsgDatecreated.toString())
            }
        }


    }

    fun addMessageItem(chatBean: ChatDataBean.Data) {
        itemList.add(0, chatBean)
        notifyItemInserted(0)
    }


    fun addMessageItemsList(list: ArrayList<ChatDataBean.Data>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun addOldMessageItemsList(list: ArrayList<ChatDataBean.Data>) {
        itemList.addAll(itemList.size, list)
        notifyItemRangeChanged(itemList.size - list.size, itemList.size)
    }


    fun addOffsetMessageItemsList(list: ArrayList<ChatDataBean.Data>) {
        itemList.addAll(0, list)
        notifyItemRangeChanged(0, list.size)
    }


    fun getLastBottomId(): String {
        return if (itemList.size == 0) {
            "0"
        } else {
            itemList[itemList.size - 1].chatMsgId.toString()
        }
    }

    fun getTopId(): String {
        return if (itemList.size == 0) {
            "0"
        } else {
            itemList[0].chatMsgId.toString()
        }
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