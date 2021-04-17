package com.grewon.dronedin.download

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.helper.FileValidationUtils
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.JobAttachmentsBean
import com.grewon.dronedin.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.*
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

/**
 * Created by Jeff Klima on 6/28/18.
 */
class DownloadService : Service() {

    @Inject
    lateinit var preferenceUtils: PreferenceUtils


    var fileSizeDownloaded: Long = 0
    var notification_id = 0
    var fileSize: Long = 0
    var notificationHandler: NotificationHandler? = null
    private val download_file_name: String? = null
    private var attachmentBean: JobAttachmentsBean? = null


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        notificationHandler = NotificationHandler(this@DownloadService)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.extras != null) {
            DroneDinApp.getAppInstance().getAppComponent().inject(this)
            attachmentBean = intent.getParcelableExtra(AppConstant.BEAN)
            startDownload()


        }
        return START_NOT_STICKY
    }

    private fun startDownload() {
        notification_id = NotificationID.iD
        notificationHandler!!.sendNotification(attachmentBean!!, notification_id)

        DroneDinApp.getAppInstance().getApi(preferenceUtils)
            .downloadFile(attachmentBean?.attachment)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<ResponseBody>() {

                override fun onSubscribeCall(disposable: Disposable) {

                }

                override fun onSuccessResponse(dataBean: ResponseBody) {
                    val writtenToDisk = writeFileToDisk(dataBean)
                    if (writtenToDisk) {
                        sendSuccessMessageToActivity()
                    } else {
                        notificationHandler!!.onDownloadFailed(notification_id)
                    }
                }

                override fun onFailedResponse(errorBean: Any?) {
                    notificationHandler!!.onDownloadFailed(notification_id)
                }

                override fun onException(throwable: Throwable?) {
                    notificationHandler!!.onDownloadFailed(notification_id)
                }


            })

    }


    private fun writeFileToDisk(body: ResponseBody): Boolean {

        val outputFile = FileValidationUtils.fileGenerateWithQVersion(
            this,
            FileValidationUtils.getName(attachmentBean?.attachment).toString()
        )


        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        var outputStreamcache: OutputStream? = null
        val tempFile: File
        var read: Int
        var readCache: Int
        try {
            val fileReader = ByteArray(4096)
            fileSize = body.contentLength()
            inputStream = body.byteStream()
            outputStream = FileOutputStream(outputFile)
            val cDir: File = baseContext.cacheDir
            /** Getting a reference to temporary file, if created earlier  */
            tempFile = File(cDir.path + "/" + download_file_name)
            outputStreamcache = FileOutputStream(tempFile)
            while (true) {
                read = inputStream.read(fileReader)
                if (read == -1) {
                    break
                }
                outputStream.write(fileReader, 0, read)

                //outputStreamcache.write(fileReader, 0, read);
                fileSizeDownloaded += read.toLong()
                notificationHandler!!.updateNotification(
                    notification_id,
                    fileSizeDownloaded / 1024.0,
                    fileSize / 1024.0
                )
                //Thread.sleep(2000);
                Log.d("writeFileToDisk", "file download: $fileSizeDownloaded of $fileSize")
            }
            notificationHandler!!.onDownloadComplete(notification_id)
            outputStream.flush()
            outputStreamcache.close()
            return true
        } catch (e: FileNotFoundException) {
            notificationHandler!!.onDownloadFailed(notification_id)
            e.printStackTrace()
        } catch (e: IOException) {
            notificationHandler!!.onDownloadFailed(notification_id)
            return false
        }
        return false
    }

    private fun sendSuccessMessageToActivity() {
        val intent = Intent(AppConstant.FILE_DOWNLOAD_SUCCESS)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    object NotificationID {
        private val c = AtomicInteger(0)
        val iD: Int
            get() = c.incrementAndGet()
    }
}