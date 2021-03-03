package com.grewon.dronedin.earnings.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.grewon.dronedin.R
import com.grewon.dronedin.server.EarningsDataBean
import kotlinx.android.synthetic.main.layout_earnings_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class EarningsAdapter(
    val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var itemList = ArrayList<EarningsDataBean.Result>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_earnings_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //   val item = itemList[position]

        if (holder is ItemViewHolder) {

            if (position % 2 == 0) {
                holder.textAmount.setTextColor(ContextCompat.getColor(context,R.color.top_green))
            }else{
                holder.textAmount.setTextColor(ContextCompat.getColor(context,R.color.cancelled_text_color))
                holder.textProjectName.visibility=View.GONE
                holder.txtTitle.text=context.getString(R.string.withdraw)
                holder.textAmount.text=context.getString(R.string.minus_price_string,"50")
            }
        }


    }


    fun addItemsList(list: ArrayList<EarningsDataBean.Result>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      val textAmount=itemView.txt_amount
      val textProjectName=itemView.txt_project_name
      val txtTitle=itemView.txt_title
      val txtDate=itemView.txt_date

    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}