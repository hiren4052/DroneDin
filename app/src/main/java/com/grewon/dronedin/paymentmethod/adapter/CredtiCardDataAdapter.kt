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

    var itemList = ArrayList<CardDataBean.Result>()

    interface OnCardItemCLickListeners {

        fun onDeleteCardItem(itemView: CardDataBean.Result, adapterPosition: Int)

        fun onDefaultCardSelect(itemView: CardDataBean.Result)
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
        return 3
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // val item = itemList[position]
        if (holder is ItemViewHolder) {

            if (position == 1) {
               holder.cardType.setImageResource(R.drawable.ic_visa)
            }


            holder.data_select.setOnClickListener {
                // onItemCLickListeners.onDefaultSelect(item)
                //  setAllSelectedFalse(item.id!!)
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


    fun addItemsList(list: ArrayList<CardDataBean.Result>) {
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