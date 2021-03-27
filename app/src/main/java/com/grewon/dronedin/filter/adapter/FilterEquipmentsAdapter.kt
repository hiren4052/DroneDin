package com.grewon.dronedin.filter.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grewon.dronedin.R
import com.grewon.dronedin.server.JobInitBean
import com.grewon.dronedin.utils.IconUtils
import kotlinx.android.synthetic.main.layout_find_pilot_jobs_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class FilterEquipmentsAdapter(
    val context: Context, val onFilterEquipmentsItemSelected: OnFilterEquipmentsItemSelected
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnFilterEquipmentsItemSelected {
        fun onFilterEquipmentsItemSelected()
    }

    var itemList = ArrayList<JobInitBean.Equipment>()


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
            holder.categoryName.text = item.equipment?.trim()

            if (item.isSelected == 1) {
                IconUtils.setFilterBackground(
                    context,
                    holder.categoryName,
                    R.drawable.ic_rectangle_filter,
                    R.drawable.ic_close
                )

            } else {
                IconUtils.setFilterBackground(
                    context,
                    holder.categoryName,
                    R.drawable.ic_border_rectangle_gray_color,
                    R.drawable.ic_plus_gray
                )

            }

            holder.itemView.setOnClickListener {
                item.isSelected = if (item.isSelected == 1) 0 else 1
                if (item.isSelected == 1) {
                    IconUtils.setFilterBackground(
                        context,
                        holder.categoryName,
                        R.drawable.ic_rectangle_filter,
                        R.drawable.ic_close
                    )

                } else {
                    IconUtils.setFilterBackground(
                        context,
                        holder.categoryName,
                        R.drawable.ic_border_rectangle_gray_color,
                        R.drawable.ic_plus_gray
                    )

                }

                onFilterEquipmentsItemSelected.onFilterEquipmentsItemSelected()
            }

        }


    }

    fun getSelectedItems(): List<JobInitBean.Equipment> {
        return itemList.filter { it.isSelected == 1 }
    }


    fun addItemsList(list: ArrayList<JobInitBean.Equipment>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val categoryName = itemView.txt_category_name
    }

    fun addSelectedIntItems(selectedSkillsId: List<Int>) {
        for (item in itemList) {
            if (selectedSkillsId.contains(item.equipmentId?.toInt())) {
                item.isSelected = 1
            }
        }
        notifyDataSetChanged()
    }


    fun addSelectedItems(selectedEquipmentsId: ArrayList<String>) {
        for (item in itemList) {
            if (selectedEquipmentsId.contains(item.equipmentId)) {
                item.isSelected = 1
            }
        }
        notifyDataSetChanged()
    }

    fun getSelectedIdsItems(): List<String> {
        return itemList.filter { it.isSelected == 1 }.map { it.equipmentId.toString() }
    }


    fun getSelectedItemsStrings(): String {
        return if (itemList.filter { it.isSelected == 1 }.map { it.equipmentId?.toInt()!! } != null) return itemList.filter { it.isSelected == 1 }.map { it.equipmentId?.toInt()!! }.joinToString(",") else ""
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}