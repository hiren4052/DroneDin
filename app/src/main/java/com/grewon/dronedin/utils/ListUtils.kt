package com.grewon.dronedin.utils

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.server.CategoryDataBean
import com.grewon.dronedin.server.EquipmentsDataBean
import com.grewon.dronedin.server.IdentificationDocumentNameBean
import com.grewon.dronedin.server.SkillsDataBean
import com.grewon.dronedin.web.WebActivity
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

        fun getIdentificationsDocumentStrings(list: ArrayList<IdentificationDocumentNameBean>): List<String> {
            return list.map { it.documentName }
        }

        fun getCategoryBean(): ArrayList<CategoryDataBean.Result> {
            val identificationList = ArrayList<CategoryDataBean.Result>()
            identificationList.add(
                CategoryDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Real estate"
                )
            )
            identificationList.add(
                CategoryDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Photography"
                )
            )
            identificationList.add(
                CategoryDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Mapping"
                )
            )
            identificationList.add(
                CategoryDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Photogrammetry"
                )
            )
            identificationList.add(
                CategoryDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Geotechnical"
                )
            )
            identificationList.add(
                CategoryDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Solar"
                )
            )
            identificationList.add(
                CategoryDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Wind"
                )
            )
            identificationList.add(
                CategoryDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Agriculture"
                )
            )
            return identificationList
        }


        fun getSkillsBean(): ArrayList<SkillsDataBean.Result> {
            val identificationList = ArrayList<SkillsDataBean.Result>()
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Thermal"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Mapping"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Lidar"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Real estate"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Other"
                )
            )

            return identificationList
        }

        fun getEquipmentsBean(): ArrayList<EquipmentsDataBean.Result> {
            val identificationList = ArrayList<EquipmentsDataBean.Result>()
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Matrice 200"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Mavic Zoom"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Thermal camera"
                )
            )

            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "Other"
                )
            )

            return identificationList
        }

        fun getCategoryStrings(list: ArrayList<CategoryDataBean.Result>): List<String> {
            return list.map { it.userProfileName.toString() }
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

    }




}