package com.grewon.dronedin.download

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.grewon.dronedin.R
import com.grewon.dronedin.main.MainActivity
import com.grewon.dronedin.server.JobAttachmentsBean
import java.io.File

/**
 * Created by Jeff Klima on 6/21/19.
 */
class DownloadNotificationHandler(private val context: Context) {
    private val builder: NotificationCompat.Builder
    private val notificationManager: NotificationManager
    fun sendNotification(attachment: JobAttachmentsBean, notification_id: Int) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentTitle(File(attachment.attachment).name)
            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setSmallIcon(R.drawable.icon_file_unknown)
            .setProgress(100, 0, false)
            .setContentIntent(pIntent)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
        notificationManager.notify(notification_id, builder.build())
    }

    fun updateNotification(notification_id: Int, fileSizeDownloaded: Double, fileSize: Double) {
        println("filesizeDownloaded $fileSizeDownloaded")
        builder.setContentText("" + (fileSizeDownloaded * 100 / fileSize).toInt() + "%")
        builder.setProgress(100, (fileSizeDownloaded * 100 / fileSize).toInt(), false)
        notificationManager.notify(notification_id, builder.build())
    }

    fun onDownloadComplete(notification_id: Int) {

        //notificationManager.cancel(notification_id);
        builder.setProgress(0, 0, false)
        builder.setContentText("File Downloaded")
        notificationManager.notify(notification_id, builder.build())
        Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show()
    }

    fun onDownloadFailed(notification_id: Int) {

        //notificationManager.cancel(notification_id);
        builder.setProgress(0, 0, false)
        builder.setContentText("Download Failed - File not available")
        notificationManager.notify(notification_id, builder.build())
    }

    fun cancelNotification(notification_id: Int) {
        notificationManager.cancel(notification_id)
    }

    companion object {
        const val DOWNLOAD_FRAGMENT = "downloadFragment"
    }

    init {
        builder = NotificationCompat.Builder(context)
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}