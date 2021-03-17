package com.grewon.dronedin.mapscreen

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder
import java.util.*

/**
 * Created by Hiren Gabani on 2019-09-18.
 */
class PlaceAPI {

    internal lateinit var language: String
    private val TAG = PlaceAPI::class.java.simpleName

    private val PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place"
    private val TYPE_AUTOCOMPLETE = "/autocomplete"
    private val OUT_JSON = "/json"

    private val API_KEY = "AIzaSyDfUrzLDCCjsyqhNdlaY3w06dDxQ0R214o"

    fun autocomplete(input: String, mContext: Context): ArrayList<String>? {
        var resultList: ArrayList<String>? = null
        language = Locale.getDefault().language

        var conn: HttpURLConnection? = null
        val jsonResults = StringBuilder()

        try {

            Log.e("Langauge", "&language=$language")

            val sb = StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON)
            //sb.append("?key=" + mContext.getString(R.string.google_maps_key))
            sb.append("?key=" + API_KEY)
            sb.append("&components")
            sb.append("&types=geocode")
            // sb.append("&language=").append(language);
            sb.append("&input=").append(URLEncoder.encode(input, "utf8"))

            val url = URL(sb.toString())
            Log.e(TAG, "" + url.toString())
            conn = url.openConnection() as HttpURLConnection
            val inp = InputStreamReader(conn.inputStream)

            // Load the results into a StringBuilder
            var read = 0
            val buff = CharArray(1024)

            while ({ read = inp.read(buff); read }() != -1) jsonResults.append(buff, 0, read)
        } catch (e: MalformedURLException) {
            Log.e(TAG, "Error processing Places API URL", e)
            return resultList
        } catch (e: IOException) {
            Log.e(TAG, "Error connecting to Places API", e)
            return resultList
        } catch (e: Exception) {
            e.printStackTrace()
            return resultList
        } finally {
            conn?.disconnect()
        }

        try {
            // Log.d(TAG, jsonResults.toString());

            // Create a JSON object hierarchy from the results
            val jsonObj = JSONObject(jsonResults.toString())
            val predsJsonArray = jsonObj.getJSONArray("predictions")

            // Extract the Place descriptions from the results
            resultList = ArrayList(predsJsonArray.length())
            for (i in 0 until predsJsonArray.length()) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Cannot process JSON results", e)
        }

        return resultList
    }

}