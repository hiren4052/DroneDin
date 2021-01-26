package com.grewon.dronedin.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.Window
import android.view.WindowManager


/**
 * Created by Jeff Klima on 2019-08-30.
 */
class ScreenUtils {
    companion object {

        fun changeStatusBarColor(activity: Activity,colorId:Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window: Window = activity.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor =colorId
            }
        }

    }
}