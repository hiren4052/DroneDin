package com.grewon.dronedin.clientjobs.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.server.JobsDataBean
import com.grewon.dronedin.utils.ListUtils
import com.grewon.dronedin.utils.ScreenUtils
import kotlinx.android.synthetic.main.layout_posted_client_jobs_item.view.*


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class PostedJobsAdapter(
    val context: Context,
    private val onItemClickListeners: OnItemClickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListeners {

        fun onItemClick(jobsDataBean: JobsDataBean.Result?)

        fun onDeleteItem(jobsDataBean: JobsDataBean.Result?, adapterPosition: Int)

    }


    var itemList = ArrayList<JobsDataBean.Result>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_posted_client_jobs_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 10
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


    fun addItemsList(list: ArrayList<JobsDataBean.Result>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageUser = itemView.img_user
        val textCategory = itemView.txt_category_name
        val textJobLocation = itemView.txt_job_location
        val textJobTitle = itemView.txt_job_title
        val textDate = itemView.txt_date
        val textPrice = itemView.txt_price
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }


}