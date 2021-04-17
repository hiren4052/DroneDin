package com.grewon.dronedin.download

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.*
import java.text.DecimalFormat
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 * Created by social_jaydeep on 21/04/17.
 */
class FileHandler(private val context: Context) {
    private var PATH: String? = null
    private var file: File? = null
    private var filename: String? = null
    private var destination: File? = null
    private var filePath: String? = null
    private var subjectCode: String? = null
    private var quarterName: String? = null
    private var degreeName: String? = null
    private val cacheDir: File? = null

    //Creating a file path
    fun createFilePath(folderName: String, subfolderName: String, subjectName: String?): File? {
        PATH = Environment.getExternalStorageDirectory()
            .toString() + "/GTUNotifier/Papers/" + folderName + "/" + subfolderName + "/"
        file = File(PATH)
        if (!file!!.exists()) {
            if (file!!.mkdirs()) {
                PATH =
                    Environment.getExternalStorageDirectory().absolutePath + "/GTUNotifier/Papers/" + folderName + "/" + subfolderName + "/"
            }
            file = File(PATH)
        }
        return file
    }

    fun createTempFilePath(folderName: String): File? {
        PATH = Environment.getExternalStorageDirectory()
            .toString() + "/GTUNotifier/" + folderName + "/"
        file = File(PATH)
        if (!file!!.exists()) {
            if (file!!.mkdirs()) {
                PATH =
                    Environment.getExternalStorageDirectory().absolutePath + "/GTUNotifier/" + folderName + "/"
            }
            file = File(PATH)
        }
        return file
    }

    fun getTempFilePath(folderName: String): String {
        filePath = Environment.getExternalStorageDirectory()
            .toString() + "/GTUNotifier/" + folderName + "/"
        return filePath!!
    }

    fun getFilePath(folderName: String, subfolderName: String): String {
        filePath = Environment.getExternalStorageDirectory()
            .toString() + "/GTUNotifier/Papers/" + folderName + "/" + subfolderName + "/"
        return filePath!!
    }

    fun createFileName(subjectCode: String, quarterName: String, degreeName: String): String {
        this.subjectCode = formatteString(subjectCode)
        this.quarterName = formatteString(quarterName)
        this.degreeName = formatteString(degreeName)
        filename =
            (this.subjectCode + "_" + this.quarterName + "_" + this.degreeName + ".zip").trim { it <= ' ' }
        return filename!!
    }

    fun createPDFFileName(subjectCode: String, quarterName: String, degreeName: String): String {
        this.subjectCode = formatteString(subjectCode)
        this.quarterName = formatteString(quarterName)
        this.degreeName = formatteString(degreeName)
        filename =
            (this.subjectCode + "_" + this.quarterName + "_" + this.degreeName + ".pdf").trim { it <= ' ' }
        return filename!!
    }

    fun unPackZip(zipfilename: String, renamedFileName: String): Boolean {
        val `is`: InputStream
        val zis: ZipInputStream
        var filename: String? = null
        try {
            `is` = FileInputStream(PATH + zipfilename)
            zis = ZipInputStream(BufferedInputStream(`is`))
            var ze: ZipEntry
            val buffer = ByteArray(1024)
            var count: Int
            while (zis.nextEntry.also { ze = it } != null) {
                filename = ze.name
                Log.e("FILENAME", filename)
                Log.e("UnZipping", filename)
                if (ze.isDirectory) {
                    val fmd = File(PATH + filename)
                    fmd.mkdirs()
                    continue
                }
                val fout = FileOutputStream(PATH + filename)
                while (zis.read(buffer).also { count = it } != -1) {
                    fout.write(buffer, 0, count)
                }
                fout.close()
                zis.closeEntry()
            }
            zis.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } finally {
            val source = File(PATH + filename)
            destination = File(PATH + renamedFileName)
            val renamed = source.renameTo(destination)
            Log.e("is file renamed?", renamed.toString())
            Log.e("renamed file name - ", destination.toString())
            val file = File(PATH + zipfilename)
            val deleted = file.delete()
            Log.e("is File Deleted?", deleted.toString())
            Log.e("File Deleted - ", file.toString())
        }
        return true
    }

    val finalFilePath: String
        get() = destination.toString()

    fun formatteString(fileName: String): String {
        return fileName.replace("/".toRegex(), "_").replace(",".toRegex(), "_")
            .replace("\\s+".toRegex(), "").replace("-".toRegex(), "_").trim { it <= ' ' }
    }

    fun getFileSize(size: Long): String {
        var hrSize = ""
        val k = size / 1024.0
        val m = k / 1024.0
        val g = m / 1024.0
        val t = g / 1024.0
        val dec = DecimalFormat("0.00")
        hrSize = if (t > 1) {
            dec.format(t) + " TB"
        } else if (g > 1) {
            dec.format(g) + " GB"
        } else if (m > 1) {
            dec.format(m) + " MB"
        } else if (k > 1) {
            dec.format(k) + " KB"
        } else {
            dec.format(size) + " Bytes"
        }
        return hrSize
    }
}