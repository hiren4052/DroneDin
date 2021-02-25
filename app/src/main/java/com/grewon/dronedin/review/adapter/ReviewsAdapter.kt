package com.grewon.dronedin.review.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.server.ReviewsDataBean
import com.grewon.dronedin.utils.ListUtils
import com.grewon.dronedin.utils.ScreenUtils
import kotlinx.android.synthetic.main.layout_reviews_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class ReviewsAdapter(
    val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var itemList = ArrayList<ReviewsDataBean.Result>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_reviews_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //  val item = itemList[position]

        if (holder is ItemViewHolder) {

            Glide.with(context)
                .load(AppConstant.ORIGINAL_IMAGE_URL + "")
                .apply(RequestOptions().placeholder(ScreenUtils.getRandomPlaceHolderColor()))
                .into(holder.imageUser)


        }


    }


    fun addItemsList(list: ArrayList<ReviewsDataBean.Result>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageUser = itemView.img_user
        val textDescription = itemView.txt_description
        val textUserName = itemView.txt_user_name
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}