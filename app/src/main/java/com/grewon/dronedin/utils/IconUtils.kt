package com.grewon.dronedin.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.grewon.dronedin.R
import com.grewon.dronedin.enum.BADGE_TYPE
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_pilot_profile.*


class IconUtils {
    companion object {
        fun setLeftDrawableIcontoText(
            context: Context?,
            drawableId: Int,
            view: TextView
        ) {
            view.setCompoundDrawablesWithIntrinsicBounds(
                getDrawable(context, drawableId),
                null,
                null,
                null
            )
        }

        fun setRightDrawableIcontoText(
            context: Context?,
            drawableId: Int,
            view: TextView
        ) {
            view.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                getDrawable(context, drawableId),
                null
            )
        }

        fun setRightDrawableIconEditText(
            context: Context,
            drawable: Int,
            view: EditText,
            colorId: Int?
        ) {
            val wrapDrawable =
                DrawableCompat.wrap(getDrawable(context, drawable)!!)
            if (colorId != null)
                DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(context, colorId))
            view.setCompoundDrawablesWithIntrinsicBounds(null, null, wrapDrawable, null)
        }

        fun setCheckBoxButtonDrawable(
            context: Context?,
            drawableId: Int,
            view: CheckBox
        ) {
            view.buttonDrawable = getDrawable(context, drawableId)
        }

        fun getDrawable(
            context: Context?,
            drawableId: Int?
        ): Drawable? {
            return AppCompatResources.getDrawable(context!!, drawableId!!)
        }

        fun setImageViewBackGround(
            context: Context?,
            drawableId: Int,
            view: ImageView
        ) {
            view.background = getDrawable(context, drawableId)
        }

        fun setImageViewTint(
            context: Context?,
            color: Int,
            imageView: ImageView
        ) {
            imageView.setColorFilter(
                ContextCompat.getColor(context!!, color),
                PorterDuff.Mode.SRC_IN
            )
        }

        fun setLeftDrawableIconToTextWithTint(
            context: Context?,
            drawableId: Int?,
            view: TextView
        ) {
            val drawable = getDrawable(context, drawableId!!)
            DrawableCompat.setTint(
                drawable!!,
                ContextCompat.getColor(context!!, R.color.colorPrimary)
            )
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
            view.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }

        fun setFilterBackground(
            context: Context,
            textView: TextView,
            textBackground: Int,
            drawableEnd: Int
        ) {
            textView.background =
                ContextCompat.getDrawable(context, textBackground)
            setRightDrawableIcontoText(
                context,
                drawableEnd,
                textView
            )
        }

        fun setBadgeImage(
            context: Context,
            imageView: ImageView,
            badgeType: String
        ) {
            var badgeImage: Int =  R.drawable.ic_bronze


            if (badgeType == BADGE_TYPE.Bronze.name) {
                badgeImage = R.drawable.ic_bronze
            } else if (badgeType == BADGE_TYPE.Gold.name) {
                badgeImage = R.drawable.ic_gold
            } else if (badgeType == BADGE_TYPE.Diamond.name) {
                badgeImage = R.drawable.ic_diamond
            } else if (badgeType == BADGE_TYPE.Silver.name) {
                badgeImage = R.drawable.ic_silver
            }

            Glide.with(context)
                .load(badgeImage)
                .into(imageView)
        }

        fun bitmapDescriptorFromVector(
            context: Context,
            vectorResId: Int
        ): BitmapDescriptor? {
            val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
            vectorDrawable!!.setBounds(
                0,
                0,
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            )
            val bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

}