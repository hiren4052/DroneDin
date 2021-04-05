package com.grewon.dronedin.invitepilot.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.grewon.dronedin.R
import com.grewon.dronedin.extraadapter.ChipEquipmentsAdapter
import com.grewon.dronedin.extraadapter.ChipSkillsAdapter
import com.grewon.dronedin.server.PilotDataBean
import com.grewon.dronedin.utils.MapUtils
import kotlinx.android.synthetic.main.layout_invite_pilot_item.view.*


class InvitePilotAdapter(
    val context: Context,
    private val onItemClickListeners: OnItemClickListeners
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListeners {

        fun onPilotItemClick(jobsDataBean: PilotDataBean.Data)

        fun onLongClick(jobsDataBean: PilotDataBean.Data, adapterPosition: Int)


    }


    var itemList = ArrayList<PilotDataBean.Data>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_invite_pilot_item,
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

            if (item.isSelected) {
                holder.selectCheck.visibility = View.VISIBLE
            } else {
                holder.selectCheck.visibility = View.GONE
            }

            if (item.userName != null) {
                holder.txtUseName.text = item.userName
            }

            if (item.rate != null) {
                holder.txtRating.text = item.rate
            }

            if (item.userLatitude != null && item.userLongitude != null) {
                holder.textJobLocation.text = MapUtils.getStateName(
                    context,
                    item.userLatitude.toDouble(),
                    item.userLongitude.toDouble()
                )
            }

            if (item.profilePrice != null) {
                holder.textPrice.text = context.getString(R.string.price_string, item.profilePrice)
            }

            if (item.skill != null) {
                holder.chipSkills.visibility = View.VISIBLE
                val layoutManager = FlexboxLayoutManager(context)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                holder.chipSkills.layoutManager = layoutManager
                val skillsAdapter = ChipSkillsAdapter(context, R.color.gray_f4, R.color.gray_71)
                holder.chipSkills.adapter = skillsAdapter
                val myNodeList: ArrayList<String> = ArrayList<String>(item.skill.split(","))
                skillsAdapter.addItemsList(myNodeList)
            } else {
                holder.chipSkills.visibility = View.GONE
            }


            if (item.equipment != null) {
                holder.chipEquipments.visibility = View.VISIBLE
                val elayoutManager = FlexboxLayoutManager(context)
                elayoutManager.flexDirection = FlexDirection.ROW
                elayoutManager.justifyContent = JustifyContent.FLEX_START
                holder.chipEquipments.layoutManager = elayoutManager
                val equipmentsAdapter =
                    ChipEquipmentsAdapter(context, R.color.light_sky_blue, R.color.gray_71)
                holder.chipEquipments.adapter = equipmentsAdapter
                val myNodeList: ArrayList<String> = ArrayList<String>(item.equipment.split(","))
                equipmentsAdapter.addItemsList(myNodeList)
            } else {
                holder.chipEquipments.visibility = View.GONE
            }

            holder.itemView.setOnClickListener { onItemClickListeners.onPilotItemClick(item) }


            holder.itemView.setOnLongClickListener {
                item.isSelected = !item.isSelected
                notifyItemChanged(holder.adapterPosition)
                onItemClickListeners.onLongClick(item, holder.adapterPosition)
                true
            }


        }


    }


    fun addItemsList(list: ArrayList<PilotDataBean.Data>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val selectCheck = itemView.select_check
        val txtRating = itemView.txt_ratings
        val txtUseName = itemView.txt_user_name
        val textJobLocation = itemView.txt_job_location
        val textPrice = itemView.txt_price
        val chipSkills = itemView.chip_skills
        val chipEquipments = itemView.chip_equipments
    }


    fun removeItem(adapterPosition: Int) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }

    fun setAllSelected() {
        for (item in itemList) {
            item.isSelected = true
        }
        notifyDataSetChanged()
    }

    fun setAllUnSelected() {
        for (item in itemList) {
            item.isSelected = false
        }
        notifyDataSetChanged()
    }

    fun getSelectedPilotsId(): List<Int> {
        val idList = ArrayList<Int>()
        for (item in itemList) {
            if (item.isSelected) {
                idList.add(item.userId?.toInt()!!)
            }
        }
        return idList
    }


    fun isSelected(): Boolean {
        var isSelected = false
        for (item in itemList) {
            if (item.isSelected) {
                isSelected = true
            }
        }
        return isSelected
    }


}