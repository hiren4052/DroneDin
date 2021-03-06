package com.grewon.dronedin.clientjobs.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.server.JobsDataBean
import com.grewon.dronedin.utils.ListUtils
import com.grewon.dronedin.utils.ScreenUtils
import kotlinx.android.synthetic.main.layout_posted_client_jobs_item.view.txt_budget
import kotlinx.android.synthetic.main.layout_posted_client_jobs_item.view.txt_category_name
import kotlinx.android.synthetic.main.layout_posted_client_jobs_item.view.txt_job_title
import kotlinx.android.synthetic.main.layout_posted_client_jobs_item.view.txt_received_proposal
import kotlinx.android.synthetic.main.layout_posted_client_jobs_item.view.txt_sent_invitation


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class PostedJobsAdapter(
    val context: Context,
    private val onItemClickListeners: OnItemClickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListeners {

        fun onItemClick(jobsDataBean: JobsDataBean.Data?)

        fun onDeleteItem(jobsDataBean: JobsDataBean.Data?, adapterPosition: Int)

    }


    var itemList = ArrayList<JobsDataBean.Data>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_posted_client_jobs_item,
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

            holder.textCategory.text = item.categoryName
            holder.textJobTitle.text = item.jobTitle
            holder.textPrice.text = context.getString(R.string.price_string, item.totalPrice.toString())
            holder.textInvitationsSent.text = item.totalInvitation
            holder.textProposalReceived.text = item.totalProposal

            holder.itemView.setOnClickListener { onItemClickListeners.onItemClick(item) }


        }


    }


    fun addItemsList(list: ArrayList<JobsDataBean.Data>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCategory = itemView.txt_category_name
        val textJobTitle = itemView.txt_job_title
        val textPrice = itemView.txt_budget
        val textInvitationsSent = itemView.txt_sent_invitation
        val textProposalReceived = itemView.txt_received_proposal
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}