package com.grewon.dronedin.earnings.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.grewon.dronedin.R
import com.grewon.dronedin.enum.TRANSACTION_STATUS
import com.grewon.dronedin.enum.TRANSACTION_TYPE
import com.grewon.dronedin.server.EarningsDataBean
import com.grewon.dronedin.utils.TimeUtils
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.layout_earnings_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class EarningsAdapter(
    val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DATA_VIEW = 0
    var VIEW_LOADING: Int = 1

    var itemList = ArrayList<EarningsDataBean.Data>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_LOADING) {
            return LoadingHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.view_more_progress,
                    parent,
                    false
                )
            )
        }
        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_earnings_item,
                parent,
                false
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        if (itemList[position].isLoading) {
            return VIEW_LOADING
        }
        return DATA_VIEW

    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]

        if (holder is ItemViewHolder) {

            if (item.transitionType == TRANSACTION_TYPE.deposit.name) {
                if (item.transactionStatus == TRANSACTION_STATUS.success.name) {
                    if (!ValidationUtils.isEmptyFiled(item.transactionTitle.toString())) {
                        holder.txtTitle.text = item.transactionTitle
                        holder.txtTitle.visibility = View.VISIBLE
                    } else {
                        holder.txtTitle.visibility = View.GONE
                    }

                    if (!ValidationUtils.isEmptyFiled(item.transactionMsg.toString())) {
                        holder.textProjectName.text = item.transactionMsg
                        holder.textProjectName.visibility = View.VISIBLE
                    } else {
                        holder.textProjectName.visibility = View.GONE
                    }
                    holder.textAmount.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.top_green
                        )
                    )
                    holder.textAmount.text = context.getString(
                        R.string.price_string,
                        item.walletTransactionAmt.toString()
                    )
                } else if (item.transactionStatus == TRANSACTION_STATUS.failed.name) {

                    if (!ValidationUtils.isEmptyFiled(item.transactionTitle.toString())) {
                        holder.txtTitle.text = item.transactionTitle
                        holder.txtTitle.visibility = View.VISIBLE
                    } else {
                        holder.txtTitle.visibility = View.GONE
                    }

                    if (!ValidationUtils.isEmptyFiled(item.transactionMsg.toString())) {
                        holder.textProjectName.text = item.transactionMsg
                        holder.textProjectName.visibility = View.VISIBLE
                    } else {
                        holder.textProjectName.visibility = View.GONE
                    }

                    holder.textAmount.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.cancelled_text_color
                        )
                    )

                    holder.textAmount.text = context.getString(
                        R.string.price_string,
                        item.walletTransactionAmt.toString()
                    )

                }
            } else if (item.transitionType == TRANSACTION_TYPE.withdraw.name) {

                if (item.transactionStatus == TRANSACTION_STATUS.success.name) {
                    holder.textAmount.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.cancelled_text_color
                        )
                    )
                    holder.textProjectName.visibility = View.GONE
                    holder.txtTitle.text = context.getString(R.string.withdraw)
                    holder.textAmount.text = context.getString(
                        R.string.minus_price_string,
                        item.walletTransactionAmt.toString()
                    )
                } else if (item.transactionStatus == TRANSACTION_STATUS.failed.name) {
                    holder.textAmount.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.cancelled_text_color
                        )
                    )
                    holder.textProjectName.visibility = View.GONE
                    holder.txtTitle.text = context.getString(R.string.withdraw)
                    holder.textAmount.text = context.getString(
                        R.string.price_string,
                        item.walletTransactionAmt.toString()
                    )
                }
            } else {
                if (item.transactionStatus == TRANSACTION_STATUS.success.name) {
                    if (!ValidationUtils.isEmptyFiled(item.transactionTitle.toString())) {
                        holder.txtTitle.text = item.transactionTitle
                        holder.txtTitle.visibility = View.VISIBLE
                    } else {
                        holder.txtTitle.visibility = View.GONE
                    }

                    if (!ValidationUtils.isEmptyFiled(item.transactionMsg.toString())) {
                        holder.textProjectName.text = item.transactionMsg
                        holder.textProjectName.visibility = View.VISIBLE
                    } else {
                        holder.textProjectName.visibility = View.GONE
                    }
                    holder.textAmount.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.top_green
                        )
                    )
                    holder.textAmount.text = context.getString(
                        R.string.price_string,
                        item.walletTransactionAmt.toString()
                    )
                } else if (item.transactionStatus == TRANSACTION_STATUS.failed.name) {

                    if (!ValidationUtils.isEmptyFiled(item.transactionTitle.toString())) {
                        holder.txtTitle.text = item.transactionTitle
                        holder.txtTitle.visibility = View.VISIBLE
                    } else {
                        holder.txtTitle.visibility = View.GONE
                    }

                    if (!ValidationUtils.isEmptyFiled(item.transactionMsg.toString())) {
                        holder.textProjectName.text = item.transactionMsg
                        holder.textProjectName.visibility = View.VISIBLE
                    } else {
                        holder.textProjectName.visibility = View.GONE
                    }

                    holder.textAmount.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.cancelled_text_color
                        )
                    )

                    holder.textAmount.text = context.getString(
                        R.string.price_string,
                        item.walletTransactionAmt.toString()
                    )

                }

            }

            holder.txtDate.text = TimeUtils.convertDate(
                item.walletStatementDatecreated.toString(),
                "yyyy-MM-dd HH:mm:ss",
                "dd/MM/yyyy",
                true
            )

        }


    }


    fun addItemsList(list: ArrayList<EarningsDataBean.Data>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun addLoadingData() {
        itemList.add(EarningsDataBean.Data(isLoading = true))
        notifyItemInserted(itemList.size - 1)
    }


    fun removeLoadingData() {
        var position = 0
        for ((index, item) in itemList.withIndex()) {
            if (item.isLoading!!) {
                itemList.removeAt(index)
                position = index
            }
        }

        notifyItemRemoved(position)
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textAmount = itemView.txt_amount
        val textProjectName = itemView.txt_project_name
        val txtTitle = itemView.txt_title
        val txtDate = itemView.txt_date

    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }



    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}