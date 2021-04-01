package com.grewon.dronedin.portfolio.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.grewon.dronedin.R
import com.grewon.dronedin.pilotprofile.PilotProfileActivity
import com.grewon.dronedin.server.PilotProfileBean
import kotlinx.android.synthetic.main.layout_port_folio_item.view.*
import kotlinx.android.synthetic.main.layout_proposals_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class PortFolioAdapter(
    val context: Context,
    private val onItemClickListeners: OnItemClickListeners,
    isEdit: Boolean
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListeners {

        fun onEditItemClick(jobsDataBean: PilotProfileBean.Portfolio)

    }


    var itemList = ArrayList<PilotProfileBean.Portfolio>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_port_folio_item,
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

            holder.textTitle.text = item.title
            holder.textDescription.text = item.description

            if (item.attachment != null && item.attachment.isNotEmpty()) {
                if (item.attachment.size == 1) {
                    holder.l1.visibility = View.VISIBLE
                    holder.l1.weightSum = 1f
                    loadImage(item.attachment[0]?.attachment.toString(), holder.i1)
                } else if (item.attachment.size == 2) {

                    holder.l2.weightSum = 1f
                    holder.secondSquare.weightSum = 1f

                    loadImage(item.attachment[0]?.attachment.toString(), holder.i1)
                    loadImage(item.attachment[1]?.attachment.toString(), holder.i2)

                } else if (item.attachment.size == 3) {

                    holder.secondSquare.weightSum = 1f
                    loadImage(item.attachment[0]?.attachment.toString(), holder.i1)
                    loadImage(item.attachment[1]?.attachment.toString(), holder.i2)
                    loadImage(item.attachment[2]?.attachment.toString(), holder.i3)

                } else if (item.attachment.size == 4) {

                    holder.l3.weightSum = 0.9f

                    loadImage(item.attachment[0]?.attachment.toString(), holder.i1)
                    loadImage(item.attachment[1]?.attachment.toString(), holder.i2)
                    loadImage(item.attachment[2]?.attachment.toString(), holder.i3)
                    loadImage(item.attachment[3]?.attachment.toString(), holder.i4)
                } else if (item.attachment.size == 5) {
                    loadImage(item.attachment[0]?.attachment.toString(), holder.i1)
                    loadImage(item.attachment[1]?.attachment.toString(), holder.i2)
                    loadImage(item.attachment[2]?.attachment.toString(), holder.i3)
                    loadImage(item.attachment[3]?.attachment.toString(), holder.i4)
                    loadImage(item.attachment[4]?.attachment.toString(), holder.i5)
                }

            } else {
                holder.l1.visibility = View.GONE
            }


            holder.imEdit.setOnClickListener { onItemClickListeners.onEditItemClick(item) }


        }


    }

    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(context).load(imageUrl).into(imageView)
    }


    fun addItemsList(list: ArrayList<PilotProfileBean.Portfolio>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitle = itemView.txt_title
        val textDescription = itemView.txt_description
        val imEdit = itemView.im_edit
        val l1 = itemView.l1
        val l2 = itemView.l2
        val l3 = itemView.l3
        val c1 = itemView.c1
        val c2 = itemView.c2
        val c3 = itemView.c3
        val c4 = itemView.c4
        val i1 = itemView.i1
        val i2 = itemView.i2
        val i3 = itemView.i3
        val i4 = itemView.i4
        val i5 = itemView.i5
        val secondSquare = itemView.second_square

    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}