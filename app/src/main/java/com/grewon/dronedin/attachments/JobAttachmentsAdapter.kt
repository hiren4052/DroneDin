package com.grewon.dronedin.attachments

import android.content.ActivityNotFoundException
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.grewon.dronedin.R
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.helper.FileValidationUtils
import com.grewon.dronedin.server.JobAttachmentsBean
import com.grewon.dronedin.utils.ListUtils
import kotlinx.android.synthetic.main.layout_jobs_attachments_item.view.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class JobAttachmentsAdapter(
    val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var itemList = ArrayList<JobAttachmentsBean>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_jobs_attachments_item,
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

            if (ListUtils.getImageExtensionList()
                    .contains(
                        FileValidationUtils.getExtension(item.attachment).toString().toLowerCase(
                            Locale.ROOT)
                    )
            ) {
                Glide.with(context)
                    .load(item.attachment)
                    .into(holder.jobsImage)
            } else {
                Glide.with(context)
                    .load(R.drawable.ic_attachment_background)
                    .into(holder.jobsImage)
            }

            holder.itemView.setOnClickListener {
                try {
                    context.startActivity(
                        FileValidationUtils.getViewIntent(
                            context,
                            File(item.attachment!!)
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    DroneDinApp.getAppInstance()
                        .showToast(context.getString(R.string.no_application_found_to_handle_this_file))
                }

            }



        }


    }


    fun addItemsList(list: ArrayList<JobAttachmentsBean>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun addItems(list: JobAttachmentsBean) {
        itemList.add(0,list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val jobsImage = itemView.jobs_image
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}