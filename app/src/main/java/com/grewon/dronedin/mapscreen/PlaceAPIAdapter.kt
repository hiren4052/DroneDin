package com.grewon.dronedin.mapscreen

import android.app.Activity
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import com.grewon.dronedin.mapscreen.PlaceAPI


import java.util.ArrayList

/**
 * Created by Hiren Gabani on 2019-09-18.
 */
class PlaceAPIAdapter(val mContext: Context, mResource: Int,val activity: Activity) :
    ArrayAdapter<String>(mContext, mResource), Filterable {

    var resultList: ArrayList<String>? = null

    var mPlaceAPI = PlaceAPI()

    override fun getCount(): Int {
        // Last item will be the footer
        return if (resultList == null) 0 else resultList!!.size
    }

    override fun getItem(position: Int): String? {
        return resultList!![position]
    }

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                try {
                    val filterResults = FilterResults()
                    if (constraint != null) {
                        resultList = mPlaceAPI.autocomplete(constraint.toString(),mContext)

                        filterResults.values = resultList
                        filterResults.count = resultList!!.size

                        return filterResults

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return null
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                try {
                    if (results != null && results.count > 0) {

                        activity.runOnUiThread { notifyDataSetChanged() }

                    } else {
                        notifyDataSetInvalidated()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }
}
