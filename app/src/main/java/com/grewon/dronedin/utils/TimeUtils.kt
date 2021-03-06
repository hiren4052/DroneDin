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
import com.grewon.dronedin.R
import com.grewon.dronedin.helper.LogX
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.PeriodType


/**
 * Created by Jeff Klima on 2019-08-20.
 */
class TimeUtils {

    companion object {


        fun getServerToAppDate(server_time: String): String {
            return convertDate(
                server_time,
                "yyyy-MM-dd HH:mm:ss",
                "MMM dd yyyy, hh:mm a",
                true
            )


        }


        fun getMessageHeaderDisplayDate(server_time: String): String {
            return convertDate(
                server_time,
                "yyyy-MM-dd HH:mm:ss",
                "d MMMM, yyyy",
                true
            )
        }

        fun getMessageHeaderConvertDate(server_time: String): String {
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("d MMMM, yyyy", Locale.getDefault())

            if (server_time == sdf.format(calendar.time)) {
                return "Today"
            }
            LogX.E(sdf.format(calendar.time))
            calendar.add(Calendar.DATE, -1)
            LogX.E(sdf.format(calendar.time))
            if (server_time == sdf.format(calendar.time)) {
                return "Yesterday"
            }

            return server_time
        }


        fun getMessageTime(server_time: String): String {
            return convertDate(
                server_time,
                "yyyy-MM-dd HH:mm:ss",
                "hh:mm a",
                true
            )


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


        fun convertDate(tempDate: String, from: String, to: String, isUTC: Boolean): String {
            val oldFormat = SimpleDateFormat(from, Locale.getDefault())
            if (isUTC) {
                oldFormat.timeZone = TimeZone.getTimeZone("UTC")
            }
            val newFormat = SimpleDateFormat(to, Locale.getDefault())
            newFormat.timeZone = TimeZone.getDefault()

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

                        editText.text = SimpleDateFormat("yyyy-MM-dd").format(d!!)

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

                        editText.text = SimpleDateFormat("yyyy-MM-dd").format(d!!)

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


        fun getLocalTimes(context: Context, product_datetime: String): String? {

            val language = Locale.getDefault().language
            val numeric = false
            var UTCDate: Date? = null
            try {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
                UTCDate = simpleDateFormat.parse(product_datetime)

                simpleDateFormat.timeZone = TimeZone.getDefault()
                val formattedDate = simpleDateFormat.format(UTCDate)
                val LocalDate = simpleDateFormat.parse(formattedDate)

                val sDate = DateTime()
                val eDate = DateTime(LocalDate)

                val period = Period(eDate, sDate, PeriodType.yearMonthDayTime())
                val seconds = period.seconds
                val minutes = period.minutes
                val hours = period.hours
                val days = period.days
                val weeks = period.weeks
                val months = period.months
                val years = period.years


                if (years > 0) {
                    if (years == 1)
                        return if (numeric)
                            context.getString(R.string.year_ago, 1)
                        else
                            context.getString(R.string.last_year)
                    else if (years > 1)
                        return context.getString(R.string.years_ago, years)

                } else if (months > 0) {

                    if (months == 1)
                        return if (numeric)
                            context.getString(R.string.month_ago, 1)
                        else
                            context.getString(R.string.last_month)
                    else if (months > 1)
                        return context.getString(R.string.months_ago, months)

                } else if (days > 0) {

                    return if (days == 1)
                        if (numeric)
                            context.getString(R.string.day_ago, 1)
                        else
                            context.getString(R.string.yesterday)
                    else if (days in 7..14)
                        if (numeric)
                            context.getString(R.string.week_ago, 1)
                        else
                            context.getString(R.string.last_week)
                    else if (days in 15..21)
                        context.getString(R.string.weeks_ago, 2)
                    else if (days in 22..28)
                        context.getString(R.string.weeks_ago, 3)
                    else
                        context.getString(R.string.days_ago, days)

                } else if (hours > 0) {

                    if (hours == 1)
                        return if (numeric)
                            context.getString(R.string.hour_ago, 1)
                        else
                            context.getString(R.string.one_hour)
                    else if (hours > 1)
                        return context.getString(R.string.hours_ago, hours)

                } else if (minutes > 0) {

                    if (minutes == 1)
                        return if (numeric)
                            context.getString(R.string.minute_ago, 1)
                        else
                            context.getString(R.string.one_minute)
                    else if (minutes > 1)
                        return context.getString(R.string.minutes_ago, minutes)

                } else if (seconds > 0) {

                    if (seconds == 1)
                        return context.getString(R.string.just_now)
                    else if (seconds > 1)
                        return context.getString(R.string.seconds_ago, seconds)
                } else {

                    return context.getString(R.string.just_now)
                }

            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return null
        }


        fun getChatTimes(context: Context, product_datetime: String): String? {

            val language = Locale.getDefault().language
            val numeric = false
            var UTCDate: Date? = null
            try {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
                UTCDate = simpleDateFormat.parse(product_datetime)

                simpleDateFormat.timeZone = TimeZone.getDefault()
                val formattedDate = simpleDateFormat.format(UTCDate)
                val LocalDate = simpleDateFormat.parse(formattedDate)

                val sDate = DateTime()
                val eDate = DateTime(LocalDate)

                val period = Period(eDate, sDate, PeriodType.yearMonthDayTime())
                val seconds = period.seconds
                val minutes = period.minutes
                val hours = period.hours
                val days = period.days
                val weeks = period.weeks
                val months = period.months
                val years = period.years


                if (years > 0) {
                    return convertDate(
                        product_datetime,
                        "yyyy-MM-dd HH:mm:ss",
                        "yyyy/MM/dd",
                        true
                    )
                } else if (months > 0) {

                    return convertDate(
                        product_datetime,
                        "yyyy-MM-dd HH:mm:ss",
                        "yyyy/MM/dd",
                        true
                    )

                } else if (days > 0) {

                    return if (days == 1)
                        if (numeric)
                            context.getString(R.string.day_ago, 1)
                        else
                            context.getString(R.string.yesterday)
                    else if (days in 7..14)
                        if (numeric)
                            context.getString(R.string.week_ago, 1)
                        else
                            context.getString(R.string.last_week)
                    else if (days in 15..21)
                        context.getString(R.string.weeks_ago, 2)
                    else if (days in 22..28)
                        context.getString(R.string.weeks_ago, 3)
                    else
                        context.getString(R.string.days_ago, days)

                } else if (hours > 0) {

                    if (hours == 1)
                        return if (numeric)
                            context.getString(R.string.hour_ago, 1)
                        else
                            context.getString(R.string.one_hour)
                    else if (hours > 1)
                        return context.getString(R.string.hours_ago, hours)

                } else if (minutes > 0) {

                    if (minutes == 1)
                        return if (numeric)
                            context.getString(R.string.minute_ago, 1)
                        else
                            context.getString(R.string.one_minute)
                    else if (minutes > 1)
                        return context.getString(R.string.minutes_ago, minutes)

                } else if (seconds > 0) {

                    if (seconds == 1)
                        return context.getString(R.string.just_now)
                    else if (seconds > 1)
                        return context.getString(R.string.seconds_ago, seconds)
                } else {

                    return context.getString(R.string.just_now)
                }

            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return null
        }


    }
}