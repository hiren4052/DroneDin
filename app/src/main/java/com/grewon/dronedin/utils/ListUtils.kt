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
                    userProfileName = "PHOTOGRAPHY"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "REAL ESTATE"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "FILM / MOVIES"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "THERMAL IMAGING"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "LIDAR"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "PHOTGRAMMETRY"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "ENERGY SECTOR"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "WIND TURBINE"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "SOLAR"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "MINING"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "INSURANCE"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "CONSTRUCTION"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "INSPECTION"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "AGRICULTURE"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "ORTHOMOSAIC"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "FILM EDITING"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "WEDDINGS"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "MISSION FLIGHTS"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "BASIC LICENSE"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "ADVANCED LICENSE"
                )
            )
            identificationList.add(
                SkillsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "FLIGHT REVIEWER"
                )
            )
            return identificationList
        }

        fun getEquipmentsBean(): ArrayList<EquipmentsDataBean.Result> {
            val identificationList = ArrayList<EquipmentsDataBean.Result>()
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "THERMAL CAMERA"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "ZOOM CAMERA"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "LIDAR"
                )
            )

            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "PHOTGRAMMETRY"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "MAPPING"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "PIX4D"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "MAVIC 2 ZOOM"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "MAVIC 2 PRO"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "MAVIC 2 ENTERPISE DUAL"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "MATRICE 210"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "INSPIRE"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "MATRICE 300"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "MAVIC MINI"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "ANAFI PARROT"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "AUTEL EVO"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "SKYDIO"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "RTK"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "PPK"
                )
            )
            identificationList.add(
                EquipmentsDataBean.Result(
                    isSelected = 0,
                    userProfileName = "MAVIC MINI 2"
                )
            )
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
            imageList.add("png")
            imageList.add("jpg")
            imageList.add("jpeg")
            imageList.add("gif")
            return imageList
        }

    }


}