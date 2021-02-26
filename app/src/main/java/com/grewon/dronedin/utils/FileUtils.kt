package com.grewon.dronedin.utils

import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import okhttp3.ResponseBody
import java.io.*
import java.net.URLConnection

/**
 * Created by Jeff Klima on 6/4/19.
 */
class FileUtils {
    companion object{
        fun getPath(context: Context, uri: Uri): String? {
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), id.toLong()
                    )
                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(
                        split[1]
                    )
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {

                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                    context,
                    uri,
                    null,
                    null
                )
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
            return null
        }

        fun getDataColumn(
            context: Context, uri: Uri?, selection: String?,
            selectionArgs: Array<String>?
        ): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(
                column
            )
            try {
                cursor = context.contentResolver.query(
                    uri!!, projection, selection, selectionArgs,
                    null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }

        fun isImageFile(path: String?): Boolean {
            val mimeType = URLConnection.guessContentTypeFromName(path)
            return mimeType != null && mimeType.startsWith("image")
        }

        fun isAttachmentFileExist(filename: String): Boolean {
            val file = File(
                Environment.getExternalStorageDirectory().absolutePath
                        + "/" + AppConstant.APP_NAME + "/Download/Attachment/"
                        + filename
            )
            return file.exists()
        }

        fun getAttachmentFile(filename: String): File {
            return File(
                Environment.getExternalStorageDirectory().absolutePath
                        + "/" + AppConstant.APP_NAME + "/Download/Attachment/"
                        + filename
            )
        }

        fun isAudioFileExist(fileName: String): Boolean {
            val file = File(
                Environment.getExternalStorageDirectory().absolutePath
                        + "/" + AppConstant.APP_NAME + "/Download/Audios/"
                        + fileName
            )
            return file.exists()
        }

        fun isVideoFileExist(fileName: String): Boolean {
            val file = File(
                Environment.getExternalStorageDirectory().absolutePath
                        + "/" + AppConstant.APP_NAME + "/Download/Videos/"
                        + fileName
            )
            return file.exists()
        }

        fun isImageFileExist(fileName: String): Boolean {
            val file = File(
                Environment.getExternalStorageDirectory().absolutePath
                        + "/" + AppConstant.APP_NAME + "/Download/Images/"
                        + fileName
            )
            return file.exists()
        }

        fun isDocumentFileExist(fileName: String): Boolean {
            val file = File(
                Environment.getExternalStorageDirectory().absolutePath
                        + "/" + AppConstant.APP_NAME + "/Download/Documents/"
                        + fileName
            )
            return file.exists()
        }

        fun openFile(context: Context, url: File) {
            val uri = Uri.fromFile(url)
            val intent = Intent(Intent.ACTION_VIEW)
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword")
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf")
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel")
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav")
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf")
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav")
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif")
            } else if (url.toString().contains(".jpg") || url.toString()
                    .contains(".jpeg") || url.toString().contains(".png")
            ) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg")
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain")
            } else if (url.toString().contains(".3gp") || url.toString()
                    .contains(".mpg") || url.toString().contains(".mpeg") || url.toString()
                    .contains(".mpe") || url.toString().contains(".mp4") || url.toString()
                    .contains(".avi")
            ) {
                // Video files
                intent.setDataAndType(uri, "video/*")
            } else {
                //if you want you can also define the intent type for any other file
                //additionally use else clause below, to manage other unknown extensions
                //in this case, Android will show all applications installed on the device
                //so you can choose which application to use
                intent.setDataAndType(uri, "*/*")
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, R.string.no_app_found_to_handle, Toast.LENGTH_SHORT).show()
            }
        }

        fun writeFileToDisk(context: Context, body: ResponseBody, fileName: String?): Boolean {
            val file: File? = null
            val filename: String? = null
            var renamedFileName: String
            val sdCard = Environment.getExternalStorageDirectory()
            val mediaStorageDir =
                File(sdCard.absolutePath + "/" + AppConstant.APP_NAME + "/Download/Attachment")
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs()
            }
            val outputFile = File(mediaStorageDir.absolutePath, fileName)
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            var outputStreamcache: OutputStream? = null
            val tempFile: File
            var read: Int
            var readCache: Int
            var fileSizeDownloaded: Long = 0
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                inputStream = body.byteStream()
                outputStream = FileOutputStream(outputFile)
                val cDir = context.cacheDir
                /** Getting a reference to temporary file, if created earlier  */
                tempFile = File(cDir.path + "/" + "test")
                outputStreamcache = FileOutputStream(tempFile)
                while (true) {
                    read = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)

                    //outputStreamcache.write(fileReader, 0, read);
                    fileSizeDownloaded += read.toLong()

                    //Thread.sleep(2000);
                    Log.d("writeFileToDisk", "file download: $fileSizeDownloaded of $fileSize")
                }
                outputStream.flush()
                outputStreamcache.close()
                return true
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                return false
            }
            return false
        }
    }

}