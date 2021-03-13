package com.grewon.dronedin.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import android.view.ViewGroup
import android.view.Window
import com.grewon.dronedin.R


class LoadingDialog(context: Context) : Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawable(AppCompatResources.getDrawable(context, R.color.transparent))
        setContentView(R.layout.dialog_loading)
    }

}