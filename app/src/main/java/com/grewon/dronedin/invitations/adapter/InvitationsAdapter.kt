package com.grewon.dronedin.invitations.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grewon.dronedin.R
import com.grewon.dronedin.server.InvitationsDataBean
import com.grewon.dronedin.utils.MapUtils
import com.grewon.dronedin.utils.TimeUtils
import kotlinx.android.synthetic.main.layout_invitations_item.view.*
import kotlinx.android.synthetic.main.layout_invitations_item.view.txt_category_name
import kotlinx.android.synthetic.main.layout_invitations_item.view.txt_client_name
import kotlinx.android.synthetic.main.layout_invitations_item.view.txt_date
import kotlinx.android.synthetic.main.layout_invitations_item.view.txt_job_location
import kotlinx.android.synthetic.main.layout_invitations_item.view.txt_job_title
import kotlinx.android.synthetic.main.layout_proposals_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class InvitationsAdapter(
    val context: Context,
    private val onItemClickListeners: OnItemClickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListeners {

        fun onInvitationsItemClick(jobsDataBean: InvitationsDataBean.Data?)


    }


    var itemList = ArrayList<InvitationsDataBean.Data>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_invitations_item,
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
            holder.textClientName.text = item.userName
            holder.textBudget.text = item.totalPrice
            holder.textJobLocation.text =
                MapUtils.getStateName(
                    context, item.jobLatitude?.toDouble()!!,
                    item.jobLongitude?.toDouble()!!
                )


            holder.textDate.text = TimeUtils.getLocalTimes(context, item.jobInvitationDatecreated.toString())

            holder.itemView.setOnClickListener { onItemClickListeners.onInvitationsItemClick(item) }
        }


    }


    fun addItemsList(list: ArrayList<InvitationsDataBean.Data>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCategory = itemView.txt_category_name
        val textJobTitle = itemView.txt_job_title
        val textClientName = itemView.txt_client_name
        val textJobLocation = itemView.txt_job_location
        val textBudget = itemView.txt_budget
        val textDate = itemView.txt_date
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}