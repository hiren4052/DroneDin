package com.grewon.dronedin.milestone.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grewon.dronedin.R
import com.grewon.dronedin.server.MilestonesDataBean
import com.grewon.dronedin.utils.TextUtils
import kotlinx.android.synthetic.main.layout_mile_stone_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class MileStoneAdapter(
    val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var itemList = ArrayList<MilestonesDataBean.Result>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_mile_stone_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //   val item = itemList[position]


        if (holder is ItemViewHolder) {

            holder.txtCount.text = (position + 1).toString() + "."
            holder.txtPrice.text = context.getString(R.string.price_string, (position * 10).toString())
            TextUtils.addExpandText(context,holder.txtMilstoneDesc,context.getString(R.string.lorem_ipsum_is_simply_dummy_text_of_the_printing_and_typesetting_industry_lorem_ipsum_has_been_the_industry_s_standard_dummy_text_ever_since_the_1500s_when_an_unknown_printer_took_a_galley_of_type_and_scrambled_it_to_make_a_type_specimen_book_it_has_survived_not_only_five_centuries_but_also_the_leap_into_electronic_typesetting_remaining_essentially_unchanged_it_was_popularised_in_the_1960s_with_the_release_of_letraset_sheets_containing_lorem_ipsum_passages_and_more_recently_with_desktop_publishing_software_like_aldus_pagemaker_including_versions_of_lorem_ipsum))

        }


    }


    fun addItemsList(list: ArrayList<MilestonesDataBean.Result>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val txtCount = itemView.txt_count
        val txtMilstoneDesc = itemView.txt_milestone_desc
        val txtPrice = itemView.txt_price
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}