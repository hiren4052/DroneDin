package com.grewon.dronedin.utils

import android.content.Context
import android.graphics.Point
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.text.DecimalFormat
import java.util.*
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin


/**
 * Created by Hiren Gabani on 2019-09-18.
 */
class MapUtils {
    companion object {

        fun getLocationFormAddress(context: Context, address: String): LatLng? {

            val coder = Geocoder(context)
            val addresses: List<Address>?

            try {
                addresses = coder.getFromLocationName(address, 5)
                if (addresses == null) {
                    Toast.makeText(context, "address not found", Toast.LENGTH_SHORT).show()
                    return null
                }

                if (addresses.size == 0) {
                    Toast.makeText(context, "address not found", Toast.LENGTH_SHORT).show()
                    return null
                }

                val location = addresses[0]
                if (location != null) {
                    return LatLng(location.latitude, location.longitude)
                }

            } catch (ex: IOException) {
                ex.printStackTrace()
            }

            return LatLng(0.0, 0.0)
        }

        fun getCompleteAddressString(
            context: Context,
            LATITUDE: Double,
            LONGITUDE: Double
        ): String {
            var strAdd = ""
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
                if (addresses != null) {
                    val returnedAddress = addresses[0]
                    val strReturnedAddress = StringBuilder("")


                    for (i in 0..returnedAddress.maxAddressLineIndex) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                    }
                    strAdd = strReturnedAddress.toString()
                    val address = strReturnedAddress.toString()
                    Log.d("loction address", strReturnedAddress.toString())
                } else {
                    Log.d("loction address", "No Address returned!")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("loction address", "Canont get Address!")
            }

            return strAdd
        }


        fun getCountryName(context: Context, latitude: Double, longitude: Double): String {
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)

                val address =
                    if (addresses[0].getAddressLine(0) != null) addresses[0].getAddressLine(0) else "" // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                val city = if (addresses[0].locality != null) addresses[0].locality else ""
                val state = if (addresses[0].adminArea != null) addresses[0].adminArea else ""
                val country = if (addresses[0].countryName != null) addresses[0].countryName else ""
                val postalCode =
                    if (addresses[0].postalCode != null) addresses[0].postalCode else ""
                val knownName =
                    if (addresses[0].featureName != null) addresses[0].featureName else ""
                val countryCode = if (addresses[0].countryCode != null) addresses[0].countryCode else ""

                return country
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }


        fun calculationByDistance(startLatLong: LatLng, endLatLong: LatLng): Int {
            val newFormat = DecimalFormat("##")
            val Radius = 6371// radius of earth in Km
            val lat1 = startLatLong.latitude
            val lat2 = endLatLong.latitude
            val lon1 = startLatLong.longitude
            val lon2 = endLatLong.longitude
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = sin(dLat / 2) * sin(dLat / 2) + (cos(Math.toRadians(lat1))
                    * cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                    * sin(dLon / 2))
            val c = 2 * asin(Math.sqrt(a))
            val valueResult = Radius * c
            val km = valueResult / 1
            val kmInDec = Integer.valueOf(newFormat.format(km))
            val meter = valueResult % 1000
            val meterInDec = Integer.valueOf(newFormat.format(meter))
            val mile = kmInDec * 0.621371
            val mileInDec = Integer.valueOf(newFormat.format(mile))
            Log.e(
                "Radius Value", "" + valueResult + "   KM  " + kmInDec
                        + " ,Meter   " + meterInDec + " ,Mile   " + mileInDec
            )
            return mileInDec
        }


        fun mapImageUrl(
            context: Context?,
            lon: Double,
            lat: Double
        ): String? {
            val X: Int = getScreenWidth(context)
            val Y = (X / 2.2).toInt()
            val url: String
            url = if (lat == 0.0 && lon == 0.0) {
                "https://maps.googleapis.com/maps/api/staticmap?&zoom=13&size=" + X + "x" + Y + "&maptype=roadmap&format=png"
            } else {
                "https://maps.googleapis.com/maps/api/staticmap?zoom=13&size=" + X + "x" + Y + "&maptype=roadmap&markers=color:red%7Clabel:R%7C" + lon + "," + lat + "&key=AIzaSyADt0Hd-zz2HSJAoz5ZaFVPXI4prO-FIis"
            }
            return url
        }

        fun getScreenWidth(c: Context?): Int {
            val wm =
                c?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.x
        }
    }
}