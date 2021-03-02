package com.grewon.dronedin.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.devs.readmoreoption.ReadMoreOption
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.web.WebActivity
import java.text.DecimalFormat

/**
 * Created by Hiren Gabani on 5/4/20.
 */
class TextUtils {
    companion object {
        fun signUpSpannableString(
            context: Context
        ): SpannableString {

            val txtString = context.getString(R.string.new_to_dronedin_sign_up)

            val spannableString = SpannableString(txtString)
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                txtString.length - 8,
                txtString.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )


            return spannableString
        }

        fun signUpColorSpannableString(
            context: Context
        ): SpannableString {

            val txtString = context.getString(R.string.new_to_dronedin_sign_up)

            val spannableString = SpannableString(txtString)
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                txtString.length - 8,
                txtString.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.span_text_color)),
                txtString.length - 8,
                txtString.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )

            return spannableString
        }

        fun signInColorSpannableString(
            context: Context
        ): SpannableString {

            val txtString = context.getString(R.string.already_have_an_account_log_in)

            val spannableString = SpannableString(txtString)
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                txtString.length - 6,
                txtString.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.span_text_color)),
                txtString.length - 6,
                txtString.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )

            return spannableString
        }
        fun receiveCodeColorSpannableString(
            context: Context
        ): SpannableString {

            val txtString = context.getString(R.string.didn_t_receive_the_code_resend)

            val spannableString = SpannableString(txtString)
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                txtString.length - 7,
                txtString.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.span_text_color)),
                txtString.length - 7,
                txtString.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )

            return spannableString
        }

        fun appTermsSpannableString(
            context: Context,
            txtString: String
        ): SpannableString? {

            val spannableString = SpannableString(txtString)
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                txtString.length - 11,
                txtString.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                ), txtString.length - 11, txtString.length, 0
            )

            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    context.startActivity(
                        Intent(context, WebActivity::class.java).putExtra(AppConstant.WEB_URL, AppConstant.TERMS_OF_SERVICE_URL)

                    )
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = true
                }
            }

            spannableString.setSpan(
                clickableSpan,
                txtString.length - 11,
                txtString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )



            return spannableString
        }


        fun addExpandText(
            context: Context?,
            textView: TextView?,
            text: String?
        ) {
            // OR using options to customize
            val readMoreOption = ReadMoreOption.Builder(context)
                .textLength(100, ReadMoreOption.TYPE_CHARACTER)
                .moreLabel("See More")
                .lessLabel("See Less")
                .moreLabelColor(Color.BLACK)
                .lessLabelColor(Color.BLACK)
                .labelUnderLine(false)
                .expandAnimation(false)
                .build()
            readMoreOption.addReadMoreTo(textView, text)

        }


        fun convertDecimalFormat(value: Double): String? {
            val precision = DecimalFormat("0.00")
            return precision.format(value)
        }

        fun convertDecimalFormatToDouble(value: Double): String? {
            val precision = DecimalFormat("0.00")
            return precision.format(value)
        }

        fun convertSingleDecimalFormat(value: Double): String? {
            val precision = DecimalFormat("0.0")
            return precision.format(value)
        }


    }


}