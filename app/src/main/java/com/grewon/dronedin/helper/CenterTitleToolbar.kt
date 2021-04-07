package com.grewon.dronedin.helper

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.grewon.dronedin.R


class CenterTitleToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.toolbarStyle
) : Toolbar(context, attrs, defStyleAttr) {

    private val titleView: TextView = TextView(getContext())

    init {

        val textAppearanceStyleResId: Int
        val subTextAppearanceStyleResId: Int
        val a = context.theme.obtainStyledAttributes(
            attrs,
            intArrayOf(R.attr.titleTextAppearance), defStyleAttr, 0
        )

        try {
            textAppearanceStyleResId = a.getResourceId(0, 0)
        } finally {
            a.recycle()

        }
        if (textAppearanceStyleResId > 0) {
            titleView.setTextAppearance(context, textAppearanceStyleResId)
        }



        addView(
            titleView,
            LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
        )

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        titleView.x = ((width - titleView.width) / 2).toFloat()

    }

    override fun setTitle(title: CharSequence) {
        titleView.text = title
    }


}

