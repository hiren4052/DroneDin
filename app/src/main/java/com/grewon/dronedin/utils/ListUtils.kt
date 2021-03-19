package com.grewon.dronedin.utils

import com.grewon.dronedin.server.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Hiren Gabani on 5/4/20.
 */
class ListUtils {
    companion object {

        fun getIdentificationsDocumentBean(): ArrayList<IdentificationDocumentNameBean> {
            val identificationList = ArrayList<IdentificationDocumentNameBean>()
            identificationList.add(IdentificationDocumentNameBean("Driving License"))
            identificationList.add(IdentificationDocumentNameBean("Governments Documents"))
            return identificationList
        }

        fun getIdentificationsDocumentStrings(list: java.util.ArrayList<IdentificationBean.IdentificationBeanItem>?): List<String>? {
            return list?.map { it.proofName.toString() }
        }

        fun getCategoryBean(): ArrayList<String> {
            val identificationList = ArrayList<String>()

            return identificationList
        }


        fun getSkillsBean(): ArrayList<String> {
            val identificationList = ArrayList<String>()

            return identificationList
        }


        fun getEquipmentsBean(): ArrayList<String> {
            val identificationList = ArrayList<String>()

            return identificationList
        }

        fun getCategoryStrings(list: ArrayList<JobInitBean.Category>?): List<String>? {
            return list?.map { it.categoryName.toString() }
        }


        fun getMonthList(): ArrayList<String> {
            val montList = ArrayList<String>()
            for (item in 1..12) {
                montList.add("" + item)
            }
            return montList
        }


        fun getYearList(): ArrayList<String> {
            val calendar = Calendar.getInstance()
            val currentYear: Int = calendar.get(Calendar.YEAR)
            val montList = ArrayList<String>()
            for (item in currentYear..2050) {
                montList.add("" + item)
            }
            return montList
        }

        fun getImageExtensionList(): ArrayList<String> {
            val imageList = ArrayList<String>()
            imageList.add(".png")
            imageList.add(".jpg")
            imageList.add(".jpeg")
            imageList.add(".gif")
            return imageList
        }

    }


}