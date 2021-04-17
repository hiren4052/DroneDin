package com.grewon.dronedin.download

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.helper.FileValidationUtils
import com.grewon.dronedin.server.JobAttachmentsBean

/**
 * Created by social_jaydeep on 22/04/17.
 */
class NotificationHandler(private val context: Context) {


    private val builder: NotificationCompat.Builder =
        NotificationCompat.Builder(context, AppConstant.CHANNEL_ID)
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val fileHandler: FileHandler = FileHandler(context)


    fun sendNotification(attachment: JobAttachmentsBean, notification_id: Int) {
        builder.setContentTitle(
            FileValidationUtils.getName(attachment.attachment.toString())
        ).setContentText("1%")
            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setSmallIcon(R.drawable.icon_file_unknown)
            .setProgress(100, 1, false)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
        notificationManager.notify(
            notification_id, builder.build()
        )
    }

    fun updateNotification(notification_id: Int, fileSizeDownloaded: Double, fileSize: Double) {
        println("filesizeDownloaded $fileSizeDownloaded")
        builder.setContentText("" + (fileSizeDownloaded * 100 / fileSize).toInt() + "%")
        builder.setProgress(fileSize.toInt(), fileSizeDownloaded.toInt(), false)
        notificationManager.notify(notification_id, builder.build())
    }

    fun onDownloadComplete(notification_id: Int) {

        //notificationManager.cancel(notification_id);
        builder.setProgress(0, 0, false)
        builder.setContentText("File Downloaded")
        notificationManager.notify(notification_id, builder.build())
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

}