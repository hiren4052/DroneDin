package com.grewon.dronedin.utils

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import android.widget.EditText
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.widget.TimePicker
import android.app.TimePickerDialog
import android.widget.TextView
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.PeriodType


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class TimeUtils {

    companion object {
        fun getChatTime(server_time: String): String {
            var ourdate: String
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                val value = formatter.parse(server_time)
                val dateFormatter =
                    SimpleDateFormat(
                        "MMM dd yyyy, hh:mm a",
                        Locale.getDefault()
                    ) //this format changeable
                ourdate = dateFormatter.format(value)

                //Log.d("OurDate", OurDate);
            } catch (e: Exception) {
                ourdate = "0000-00-00 00:00:00"
            }

            return ourdate
        }

        fun convertDateTimetoAppDateTime(serverdate: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("MMM dd yyyy, hh:mm a", Locale.getDefault())
            var date: Date? = null
            try {
                date = inputFormat.parse(serverdate)
                return outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return ""
        }

        fun chatDateTime(serverdate: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val outputFormat = SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.getDefault())
            var date: Date? = null
            try {
                date = inputFormat.parse(serverdate)
                return outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return ""
        }


        fun convertDateTimeTo12HrTime(serverdate: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            var date: Date? = null
            try {
                date = inputFormat.parse(serverdate)
                return outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return ""
        }


        fun getServerCurrentTime(): String {
            var ourdate: String
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val value = Date()
                ourdate = formatter.format(value)

                //Log.d("OurDate", OurDate);
            } catch (e: Exception) {
                ourdate = "0000-00-00 00:00:00"
            }

            return ourdate
        }

        fun getServerToAppDate(server_time: String): String {
            var ourdate: String
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                val value = formatter.parse(server_time)
                val dateFormatter =
                    SimpleDateFormat(
                        "MMM dd yyyy, hh:mm a",
                        Locale.getDefault()
                    ) //this format changeable
                dateFormatter.timeZone = TimeZone.getDefault()
                ourdate = dateFormatter.format(value)

                //Log.d("OurDate", OurDate);
            } catch (e: Exception) {
                ourdate = "0000-00-00 00:00:00"
            }

            return ourdate
        }

        fun convertNotificationDateTime(server_time: String): String {
            var ourdate: String
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                val value = formatter.parse(server_time)
                val dateFormatter =
                    SimpleDateFormat(
                        "hh.mma, dd MMMM yyyy",
                        Locale.getDefault()
                    ) //this format changeable
                dateFormatter.timeZone = TimeZone.getDefault()
                ourdate = dateFormatter.format(value)

                //Log.d("OurDate", OurDate);
            } catch (e: Exception) {
                ourdate = "0000-00-00 00:00:00"
            }

            return ourdate.replace("AM", "am").replace("PM", "pm");
        }


        fun convertDate(server_time: String): String {
            var ourdate: String
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                val value = formatter.parse(server_time)
                val dateFormatter =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) //this format changeable
                dateFormatter.timeZone = TimeZone.getDefault()
                ourdate = dateFormatter.format(value)

                //Log.d("OurDate", OurDate);
            } catch (e: Exception) {
                ourdate = "0000-00-00 00:00:00"
            }

            return ourdate
        }


        fun getMessageTime(server_time: String): String {
            var ourdate: String
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                val value = formatter.parse(server_time)
                val dateFormatter =
                    SimpleDateFormat("hh:mm a", Locale.getDefault()) //this format changeable
                dateFormatter.timeZone = TimeZone.getDefault()
                ourdate = dateFormatter.format(value)

                //Log.d("OurDate", OurDate);
            } catch (e: Exception) {
                ourdate = "0000-00-00 00:00:00"
            }

            return ourdate
        }

        fun convert24to12Time(server_time: String): String {
            var ourdate: String
            try {
                val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                val value = formatter.parse(server_time)
                val dateFormatter =
                    SimpleDateFormat("hh:mm a", Locale.getDefault()) //this format changeable
                dateFormatter.timeZone = TimeZone.getDefault()
                ourdate = dateFormatter.format(value)

                //Log.d("OurDate", OurDate);
            } catch (e: Exception) {
                ourdate = "0000-00-00 00:00:00"
            }

            return ourdate
        }

        fun convertCalendarStringToDate(strDate: String): Date? {
            var date: Date? = null
            val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            try {
                date = format.parse(strDate)
            } catch (e: ParseException) {
                date = currentDate()
                e.printStackTrace()
            }

            return date
        }


        fun convertStringToDate(strDate: String): Date? {
            var date: Date? = null
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            try {
                date = format.parse(strDate)
                println(date)
            } catch (e: ParseException) {
                date = currentDate()
                e.printStackTrace()
            }

            return date
        }


        fun getDateInMillis(srcDate: String): Long {
            val desiredFormat = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault()
            )

            var dateInMillis: Long = 0
            try {
                val date = desiredFormat.parse(srcDate)
                val calendar = Calendar.getInstance()
                calendar.time = date
                dateInMillis = calendar.timeInMillis
                return dateInMillis
            } catch (e: ParseException) {
                Log.d("Date", "Exception  parsing" + e.message)
                e.printStackTrace()
            }

            return 0
        }


        fun convertDate(tempDate: String, from: String, to: String): String {
            val oldFormat = SimpleDateFormat(from, Locale.ENGLISH)
            val newFormat = SimpleDateFormat(to, Locale.ENGLISH)

            val date: Date
            var str = tempDate

            try {
                date = oldFormat.parse(tempDate)
                str = newFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str


        }

        fun convertDateIntoCalendarInstance(calendar: Calendar): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(calendar.time)
        }

        fun getServerCurrentDateTime(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            return sdf.format(currentDate())
        }


        private fun currentDate(): Date {
            val calendar = Calendar.getInstance()
            return calendar.time
        }

        fun showDatePickerDialogForET(context: Context, editText: EditText) {

            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                context,
                DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                    try {

                        val date = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth

                        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val d = dateFormat.parse(date)

                        editText.setText(SimpleDateFormat("yyyy-MM-dd").format(d))

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)
            )
            datePickerDialog.show()
        }


        fun showTimePickerDialogForText(context: Context, editText: TextView) {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->


                    editText.text = convert24to12Time("$selectedHour:$selectedMinute")
                }, hour, minute, false
            )
            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }

        fun getTimeDifference(srcDate1: String, srcDate2: String): String {
            val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            var date1: Date? = null
            var date2: Date? = null
            try {
                date1 = simpleDateFormat.parse(srcDate1)
                date2 = simpleDateFormat.parse(srcDate2)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            if (date1 != null && date2 != null) {
                val difference = date2.time - date1.time
                val days = (difference / (1000 * 60 * 60 * 24)).toInt()
                val hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
                val min =
                    (difference - (1000 * 60 * 60 * 24 * days).toLong() - (1000 * 60 * 60 * hours).toLong()).toInt() / (1000 * 60)
                val seconds = (difference / 1000 % 60).toInt()
                val strHours = if (hours <= 9) "0$hours" else "" + hours
                val strMinutes = if (min <= 9) "0$min" else "" + min
                val strSeconds = if (seconds <= 9) "0$seconds" else "" + seconds
                Log.e("Time", "getTimeDifference: $strMinutes $strHours")
                return if (hours <= 0) {
                    "$strMinutes:$strSeconds"
                } else {
                    "$strHours:$strMinutes:$strSeconds"
                }

            }
            return "00:00"
        }

        fun showBirthDatePickerDialogForText(context: Context, editText: TextView) {

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, -18)
            val datePickerDialog = DatePickerDialog(
                context,
                DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                    try {

                        val date = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth

                        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val d = dateFormat.parse(date)

                        editText.text = SimpleDateFormat("dd-MM-yyyy").format(d!!)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)
            )
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            datePickerDialog.show()
        }


        fun showDatePickerDialogForText(context: Context, editText: TextView) {

            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                context,
                DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                    try {

                        val date = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth

                        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val d = dateFormat.parse(date)

                        editText.text = SimpleDateFormat("dd-MM-yyyy").format(d!!)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)
            )
            datePickerDialog.show()
        }


        fun isValidTime(srcDate1: String, srcDate2: String): Boolean {
            val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            var date1: Date? = null
            var date2: Date? = null
            try {
                date1 = simpleDateFormat.parse(srcDate1)
                date2 = simpleDateFormat.parse(srcDate2)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            var difference = 0.toLong()
            if (date1 != null && date2 != null) {
                difference = date2.time - date1.time

            }

            return difference > 0
        }


        fun convertOrderDateTime(serverdate: String): String {
            var ourdate: String
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                val value = formatter.parse(serverdate)
                val dateFormatter =
                    SimpleDateFormat(
                        "hh.mma, dd MMMM yyyy",
                        Locale.getDefault()
                    ) //this format changeable
                dateFormatter.timeZone = TimeZone.getDefault()
                ourdate = dateFormatter.format(value)

                //Log.d("OurDate", OurDate);
            } catch (e: Exception) {
                ourdate = "0000-00-00 00:00:00"
            }

            return ourdate.replace("AM", "am").replace("PM", "pm");
        }



    }
}