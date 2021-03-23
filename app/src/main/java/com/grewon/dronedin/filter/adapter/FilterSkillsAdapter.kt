package com.grewon.dronedin.filter.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.server.JobInitBean
import com.grewon.dronedin.server.SkillsDataBean
import com.grewon.dronedin.utils.IconUtils
import kotlinx.android.synthetic.main.layout_find_pilot_jobs_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class FilterSkillsAdapter(
    val context: Context, val onFilterSkillsItemSelected: OnFilterSkillsItemSelected
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnFilterSkillsItemSelected {
        fun onFilterSkillsItemSelected()
    }

    var itemList = ArrayList<JobInitBean.Skill>()


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
            holder.categoryName.text = item.skill?.trim()

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

                onFilterSkillsItemSelected.onFilterSkillsItemSelected()
            }

        }


    }


    fun addItemsList(list: ArrayList<JobInitBean.Skill>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    fun addSelectedItems(selectedSkillsId: List<Int>) {
        for (item in itemList) {
            if (selectedSkillsId.contains(item.skillId?.toInt())) {
                item.isSelected = 1
            }
        }
        notifyDataSetChanged()
    }


    fun getSelectedItems(): List<JobInitBean.Skill> {
        return itemList.filter { it.isSelected == 1 }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val categoryName = itemView.txt_category_name
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }

    fun getSelectedItemsStrings(): String {
        return if (itemList.filter { it.isSelected == 1 }
                .map { it.skillId?.toInt()!! } != null) return itemList.filter { it.isSelected == 1 }
            .map { it.skillId?.toInt()!! }.joinToString(",") else ""
    }

}