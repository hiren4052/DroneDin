package com.grewon.dronedin.paymentmethod.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grewon.dronedin.R
import com.grewon.dronedin.server.BankDataBean
import kotlinx.android.synthetic.main.layout_bank_data_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class BankDataAdapter(
    private val context: Context,
    private var defaultCard: String,
    private val onItemCLickListeners: OnItemCLickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList = ArrayList<BankDataBean.Data.Bank>()

    interface OnItemCLickListeners {

        fun onDeleteItem(itemView: BankDataBean.Data.Bank?, adapterPosition: Int)

        fun onDefaultSelect(itemView: BankDataBean.Data.Bank?)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_bank_data_item,
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

            holder.txtBankName.text = item.bankName
            holder.txtName.text = item.accountHolderName
            holder.txtBankNumber.text = context.getString(R.string.card_number_string, item.last4)

            holder.data_select.isChecked = item.id == defaultCard

            if (!holder.data_select.isChecked) {
                holder.itemView.setOnLongClickListener {
                    onItemCLickListeners.onDeleteItem(item, holder.adapterPosition)

                    true
                }

            }else{
                holder.itemView.setOnLongClickListener(null)
            }

            holder.data_select.setOnClickListener {
                onItemCLickListeners.onDefaultSelect(item)
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


    fun addItemsList(list: ArrayList<BankDataBean.Data.Bank>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun removeBankData(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtBankName = itemView.txt_bank_name!!
        val txtName = itemView.txt_name!!
        val txtBankNumber = itemView.txt_bank_number!!
        val data_select = itemView.data_select!!

    }


}