package com.grewon.dronedin.app

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.grewon.dronedin.R
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class DroneDinApp : MultiDexApplication() {

    companion object {

        lateinit var instance: DroneDinApp


        fun getAppInstance(): DroneDinApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        MultiDex.install(this)

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath(AppConstant.FONT_FAMILY)
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

    }

}