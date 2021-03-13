package com.grewon.dronedin.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.grewon.dronedin.R
import kotlinx.android.synthetic.main.success_dialog.*


class SuccessDialog : Dialog {

    private var okListener: View.OnClickListener? = null
    private var message: String? = null
    private var positiveBtnTxt: String? = null
    private var title: String? = null
    private var iconId: Int? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener
    ) : super(context, cancelable, cancelListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window!!.setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.transparent))
        setContentView(R.layout.success_dialog)
        iconId?.let { icon.setImageResource(it) }
        txt_title.text = title

        yes.setOnClickListener(okListener)

    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setIcon(iconId: Int) {
        this.iconId = iconId
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun setPositiveBtnTxt(positiveBtnTxt: String) {
        this.positiveBtnTxt = positiveBtnTxt
    }

    fun setOkListener(onClickListener: View.OnClickListener) {
        dismiss()
        this.okListener = onClickListener
    }

}
