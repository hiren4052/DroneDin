package com.grewon.dronedin.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.Window
import android.view.WindowManager
import com.grewon.dronedin.R
import kotlin.random.Random


/**
 * Created by Jeff Klima on 2019-08-30.
 */
class ScreenUtils {
    companion object {

        fun changeStatusBarColor(activity: Activity, colorId: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window: Window = activity.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = colorId
            }
        }


        fun getRandomPlaceHolderColor(): Int {
            val randomColorList = ArrayList<Int>()
            randomColorList.add(R.color.random_one)
            randomColorList.add(R.color.random_two)
            randomColorList.add(R.color.random_three)
            randomColorList.add(R.color.random_four)
            randomColorList.add(R.color.random_five)
            return randomColorList[Random.nextInt(randomColorList.size)]
        }

        fun getRandomNotificationImage(): Int {
            val randomColorList = ArrayList<Int>()
            randomColorList.add(R.drawable.ic_email)
            randomColorList.add(R.drawable.ic_price_tag)
            randomColorList.add(R.drawable.ic_job_accepted)
            return randomColorList[Random.nextInt(randomColorList.size)]
        }

    }
}