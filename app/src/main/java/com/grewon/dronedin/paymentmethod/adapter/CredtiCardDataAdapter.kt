package com.grewon.dronedin.paymentmethod.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grewon.dronedin.R
import com.grewon.dronedin.server.CardDataBean
import kotlinx.android.synthetic.main.layout_card_data_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class CredtiCardDataAdapter(
    private val context: Context,
    private var defaultCard: String,
    private val onItemCLickListeners: OnCardItemCLickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList = ArrayList<CardDataBean.Data.Card>()

    interface OnCardItemCLickListeners {

        fun onDeleteCardItem(itemView: CardDataBean.Data.Card, adapterPosition: Int)

        fun onDefaultCardSelect(itemView: CardDataBean.Data.Card)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_card_data_item,
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

            holder.txtName.text = item.billingDetails?.name
            holder.txtCardNumber.text =
                context.getString(R.string.card_number_string, item.card?.last4)
            holder.txtExpiryDate.text = context.getString(
                R.string.expiry_date_string,
                item.card?.expMonth.toString(), item.card?.expYear.toString()
            )

            if (position == 1) {
                holder.cardType.setImageResource(R.drawable.ic_visa)
            }

            holder.data_select.isChecked = item.id == defaultCard

            if (!holder.data_select.isChecked) {
                holder.itemView.setOnLongClickListener {
                    onItemCLickListeners.onDeleteCardItem(item, holder.adapterPosition)

                    true
                }

            }else{
                holder.itemView.setOnLongClickListener(null)
            }
            holder.data_select.setOnClickListener {
                onItemCLickListeners.onDefaultCardSelect(item)
                setAllSelectedFalse(item.id!!)
            }


        }

    }

    private fun setAllSelectedFalse(id: String) {
        defaultCard = id
        notifyDataSetChanged()
    }

    fun setDefaultId(defaultCard: String) {
        this.defaultCard = defaultCard
        notifyDataSetChanged()
    }

    fun removeCardData(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


    fun addItemsList(list: ArrayList<CardDataBean.Data.Card>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.txt_name!!
        val txtExpiryDate = itemView.txt_expiry_date!!
        val txtCardNumber = itemView.txt_card_number!!
        val data_select = itemView.data_select!!
        val cardType = itemView.card_type!!

    }


}