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
import kotlinx.android.synthetic.main.single_alert_dialog.*


class SingleAlertViewDialog : Dialog {

    private var okListener: View.OnClickListener? = null
    private var message: String? = null
    private var positiveBtnTxt: String? = null
    private var negativeBtnTxt: String? = null
    private var title: String? = null

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
        setContentView(R.layout.single_alert_dialog)
        txt_title.text = title
        yes.text = positiveBtnTxt
        yes.setOnClickListener(okListener)
    }

    fun setTitle(title: String) {
        this.title = title
    }


    fun setMessage(message: String) {
        this.message = message
    }

    fun setPositiveBtnTxt(positiveBtnTxt: String) {
        this.positiveBtnTxt = positiveBtnTxt
    }

    fun setNegativeBtnTxt(negativeBtnTxt: String) {
        this.negativeBtnTxt = negativeBtnTxt
    }

    fun setOkListener(onClickListener: View.OnClickListener) {
        dismiss()
        this.okListener = onClickListener
    }

}
