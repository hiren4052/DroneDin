package com.grewon.dronedin.download

import android.content.Context
import android.os.AsyncTask
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.server.JobAttachmentsBean
import com.grewon.dronedin.server.params.UploadAttachmentsParams
import java.io.*
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Jeff Klima on 6/21/19.
 */
class DownloadFileFromURL(
    var context: Context,
    attachment: JobAttachmentsBean,
    downloadView: ImageView,
    adapterPosition: Int
) : AsyncTask<String?, String?, String?>() {
    private val attachment: JobAttachmentsBean
    var downloadView: ImageView
    private val adapterPosition: Int
    var mediaStorageDir: File? = null
    var notificationHandler: DownloadNotificationHandler? = null
    private var fileSize = 0
    private var fileSizeDownloaded = 0
    private val atomicInteger: AtomicInteger? = null
    private val download_file_name: String? = null


    private fun writeFileToDisk(body: String, attachment: JobAttachmentsBean): Boolean {
        try {
            val url = URL(body)
            val conection = url.openConnection()
            conection.connect()
            val sdCard = Environment.getExternalStorageDirectory()
            mediaStorageDir =
                File(sdCard.absolutePath + "/" + AppConstant.APP_NAME + "/Download/Video")
            if (!mediaStorageDir!!.exists()) {
                mediaStorageDir!!.mkdirs()
            }
            val file =
                File(mediaStorageDir!!.absolutePath, File(attachment.attachment).name)
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            var outputStreamcache: OutputStream? = null
            val tempFile: File
            var read: Int
            var readCache: Int
            try {
                val fileReader = ByteArray(4096)
                fileSize = conection.contentLength
                inputStream = BufferedInputStream(
                    url.openStream(),
                    fileSize
                )
                outputStream = FileOutputStream(file)
                val cDir = context.cacheDir
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
                    fileSizeDownloaded += read
                    notificationHandler!!.updateNotification(
                        adapterPosition,
                        fileSizeDownloaded / 1024.0,
                        fileSize / 1024.0
                    )
                    //Thread.sleep(2000);
                    Log.d("writeFileToDisk", "file download: $fileSizeDownloaded of $fileSize")
                }
                outputStream.flush()
                outputStreamcache.close()
                return true
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            } finally {
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return false
    }

    init {
        this.attachment = attachment
        this.downloadView = downloadView
        this.adapterPosition = adapterPosition
    }

    override fun doInBackground(vararg params: String?): String? {
        val writtenToDisk = writeFileToDisk(params[0]!!, attachment)
        if (writtenToDisk) {
            Handler().postDelayed(
                { notificationHandler!!.onDownloadComplete(adapterPosition) },
                4000
            )
        } else {

            //Toast.makeText(DownloadService.this, "Download failed", Toast.LENGTH_SHORT).show();
        }
        return null
    }
}