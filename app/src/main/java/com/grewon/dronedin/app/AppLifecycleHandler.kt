package com.grewon.dronedin.app

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.os.Bundle

class AppLifecycleHandler internal constructor(private val lifeCycleDelegate: LifeCycleDelegate) :
    ActivityLifecycleCallbacks, ComponentCallbacks2 {
    private var appInForeground = false
    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {
        if (!appInForeground) {
            appInForeground = true
            lifeCycleDelegate.onAppForegrounded()
        }
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
    override fun onTrimMemory(level: Int) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            appInForeground = false
            lifeCycleDelegate.onAppBackgrounded()
        }
    }

    override fun onConfigurationChanged(configuration: Configuration) {}
    override fun onLowMemory() {}
    internal interface LifeCycleDelegate {
        fun onAppBackgrounded()
        fun onAppForegrounded()
    }
}