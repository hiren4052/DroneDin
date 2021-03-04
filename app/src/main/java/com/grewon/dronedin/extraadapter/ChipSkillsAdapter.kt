package com.grewon.dronedin.extraadapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.grewon.dronedin.R
import com.grewon.dronedin.server.SkillsDataBean
import kotlinx.android.synthetic.main.layout_chip_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class ChipSkillsAdapter(
    val context: Context, val backgroundColor: Int, val textColor: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList = ArrayList<SkillsDataBean.Result>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_chip_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position <= 5) {
            val item = itemList[position]


            if (holder is ItemViewHolder) {
                holder.txtChipName.text = item.userProfileName

                holder.txtChipName.setTextColor(ContextCompat.getColor(context, textColor))

                when (val background: Drawable = holder.txtChipName.background) {
                    is ShapeDrawable -> {
                        background.paint.color = ContextCompat.getColor(context, backgroundColor)
                    }
                    is GradientDrawable -> {
                        background.setColor(ContextCompat.getColor(context, backgroundColor))
                    }
                    is ColorDrawable -> {
                        background.color = ContextCompat.getColor(context, backgroundColor)
                    }
                }

            }
        }

    }


    fun addItemsList(list: ArrayList<SkillsDataBean.Result>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtChipName = itemView.txt_chip_name
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}