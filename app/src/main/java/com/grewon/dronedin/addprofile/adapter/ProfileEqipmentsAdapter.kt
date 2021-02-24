package com.grewon.dronedin.addprofile.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.grewon.dronedin.R
import com.grewon.dronedin.server.EquipmentsDataBean
import com.grewon.dronedin.utils.IconUtils
import kotlinx.android.synthetic.main.layout_find_pilot_jobs_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class ProfileEqipmentsAdapter(
    val context: Context, val onFilterSkillsItemSelected: OnFilterSkillsItemSelected
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnFilterSkillsItemSelected {
        fun onFilterSkillsItemSelected()
    }

    var itemList = ArrayList<EquipmentsDataBean.Result>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_filter_category_item,
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
            holder.categoryName.text = item.userProfileName

            if (item.isSelected == 1) {
                holder.categoryName.setTextColor(ContextCompat.getColor(context, R.color.white))
                IconUtils.setFilterBackground(
                    context,
                    holder.categoryName,
                    R.drawable.ic_rectangle_spann_color,
                    R.drawable.ic_close_white
                )

            } else {
                holder.categoryName.setTextColor(ContextCompat.getColor(context, R.color.gray_b2))
                IconUtils.setFilterBackground(
                    context,
                    holder.categoryName,
                    R.drawable.ic_rectangle_light_colorprimary,
                    R.drawable.ic_plus_white
                )

            }

            holder.itemView.setOnClickListener {
                item.isSelected = if (item.isSelected == 1) 0 else 1
                if (item.isSelected == 1) {
                    holder.categoryName.setTextColor(ContextCompat.getColor(context, R.color.white))
                    IconUtils.setFilterBackground(
                        context,
                        holder.categoryName,
                        R.drawable.ic_rectangle_spann_color,
                        R.drawable.ic_close_white
                    )

                } else {
                    holder.categoryName.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray_b2
                        )
                    )
                    IconUtils.setFilterBackground(
                        context,
                        holder.categoryName,
                        R.drawable.ic_rectangle_light_colorprimary,
                        R.drawable.ic_plus_white
                    )

                }

                onFilterSkillsItemSelected.onFilterSkillsItemSelected()
            }

        }


    }


    fun addItemsList(list: ArrayList<EquipmentsDataBean.Result>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    fun getSelectedItems(): List<EquipmentsDataBean.Result> {
        return itemList.filter { it.isSelected == 1 }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val categoryName = itemView.txt_category_name
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}