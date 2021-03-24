package com.grewon.dronedin.fcm

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.splash.SplashActivity


/**
 * Created by Hiren Gabani on 2019-09-24.
 */

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {

//            val intent = Intent(AppConstant.NOTIFICATION_BROADCAST) //action: "msg"
//            intent.putExtra(AppConstant.TAG, "received")
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            LogX.E(remoteMessage.notification.toString())
            sendInfoNotification(remoteMessage.notification!!)

        }

    }


    private fun sendInfoNotification(notificationBean: RemoteMessage.Notification) {

        val bigText = NotificationCompat.BigTextStyle()
        bigText.bigText(notificationBean.body)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(this, AppConstant.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_splash)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setAutoCancel(true)
            .setContentTitle(notificationBean.title)
            .setStyle(bigText)
        var viewintent: Intent? = null
        viewintent = Intent(this, SplashActivity::class.java)
        viewintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent =
            PendingIntent.getActivity(this, 10000, viewintent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())

    }


    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

}