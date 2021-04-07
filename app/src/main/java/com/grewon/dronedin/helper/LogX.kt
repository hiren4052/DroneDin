package com.grewon.dronedin.helper

import android.util.Log
import com.grewon.dronedin.BuildConfig

class LogX {

    companion object {

        fun E(message: String) {
            if (BuildConfig.DEBUG) {
                Log.e("LogX", "" + message)
            }
        }

        fun D(message: String) {
            if (BuildConfig.DEBUG) {
                Log.e("LogX", "" + message)
            }
        }

        fun V(message: String) {
            if (BuildConfig.DEBUG) {
                Log.e("LogX", "" + message)
            }
        }

        fun W(message: String) {
            if (BuildConfig.DEBUG) {
                Log.e("LogX", "" + message)
            }
        }
    }
}