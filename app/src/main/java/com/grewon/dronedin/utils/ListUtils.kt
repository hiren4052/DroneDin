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
import com.grewon.dronedin.server.IdentificationDocumentNameBean
import com.grewon.dronedin.web.WebActivity

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

    }


}