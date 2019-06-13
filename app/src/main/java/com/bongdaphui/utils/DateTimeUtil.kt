package com.bongdaphui.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.bongdaphui.R
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class DateTimeUtil {

    enum class DateFormatDefinition(val format: String) {
        DD_MM_YYYY("dd/MM/yyyy"),
        DD_MM_YYYY_("dd-MM-yyyy"),
        DD_MM_YYYY_HH_MM("dd/MM/yyyy HH:mm"),
        HH_MM("HH:mm"),
        EEEE("EEEE");


        override fun toString(): String {
            return format
        }
    }

    fun getAge(dateString: String, type: String): String {

        val sdf = SimpleDateFormat(type, Locale.US)
        val dateDob = sdf.parse(dateString)

        val dob = Calendar.getInstance()
        dob.time = dateDob


        val today = Calendar.getInstance()

        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        val ageInt = age

        return ageInt.toString()
    }

    fun getFormat(dateTime: Long?, type: String): String {
        @SuppressLint("SimpleDateFormat")
        val newFormat = SimpleDateFormat(type)
        return newFormat.format(Date(dateTime!!))
    }

    @SuppressLint("SimpleDateFormat")
    fun getFormatDate(date: String, type: String): String {

        var timeInMilliseconds = getTimeInMilliseconds(date, type)

        val hour = getFormat(timeInMilliseconds, DateFormatDefinition.HH_MM.format)

        val day = getFormat(timeInMilliseconds, DateFormatDefinition.DD_MM_YYYY_.format)

        val date = SimpleDateFormat(type).parse(date)

        var dayOfWeek = ""

        if (date != null) {
            when (date.day) {
                0 -> dayOfWeek = "Chủ nhật"
                1 -> dayOfWeek = "Thứ 2"
                2 -> dayOfWeek = "Thứ 3"
                3 -> dayOfWeek = "Thứ 4"
                4 -> dayOfWeek = "Thứ 5"
                5 -> dayOfWeek = "Thứ 6"
                6 -> dayOfWeek = "Thứ 7"
            }
        }

        return "$hour $dayOfWeek, $day"
    }

    @SuppressLint("SimpleDateFormat")
    fun getTimeInMilliseconds(date: String, type: String): Long? {

        var timeInMilliseconds = 0L
        val sdf = SimpleDateFormat(type)

        try {
            val mDate = sdf.parse(date)
            timeInMilliseconds = mDate.time
        } catch (e: ParseException) {
            Log.d(Constant().tag, "getTimeInMilliseconds : ${e.message}")
        }
        return timeInMilliseconds
    }

    fun dialogDatePickerLight(context: Activity, bt: Button, type: String) {
        val curCalender = Calendar.getInstance()
        val datePicker = DatePickerDialog.newInstance(
            { _, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateShipMillis = calendar.timeInMillis
                bt.text = getFormat(dateShipMillis, type)
            },
            curCalender.get(Calendar.YEAR),
            curCalender.get(Calendar.MONTH),
            curCalender.get(Calendar.DAY_OF_MONTH)
        )
        //set dark theme
        datePicker.isThemeDark = false
        datePicker.accentColor = context.resources.getColor(R.color.colorPrimary)
        datePicker.minDate = curCalender
        datePicker.show(context.fragmentManager, "Datepickerdialog")
    }

    fun dialogDatePickerLight(context: Activity, textView: TextView, type: String) {
        val curCalender = Calendar.getInstance()
        val datePicker = DatePickerDialog.newInstance(
            { _, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateShipMillis = calendar.timeInMillis
                textView.text = getFormat(dateShipMillis, type)
            },
            curCalender.get(Calendar.YEAR),
            curCalender.get(Calendar.MONTH),
            curCalender.get(Calendar.DAY_OF_MONTH)
        )
        //set dark theme
        datePicker.isThemeDark = false
        datePicker.accentColor = context.resources.getColor(R.color.colorPrimary)
//        datePicker.minDate = curCalender
        datePicker.show(context.fragmentManager, "Datepickerdialog")
    }

    @SuppressLint("SetTextI18n")
    fun dialogTimePickerLight(context: Activity, bt: Button) {
        val curCalender = Calendar.getInstance()
        val datePicker = TimePickerDialog.newInstance({ view, hourOfDay, minute, second ->
            val mMinute = if (10 > minute) "0$minute" else "$minute"
            bt.text = "$hourOfDay:$mMinute"
        }, curCalender.get(Calendar.HOUR_OF_DAY), curCalender.get(Calendar.MINUTE), true)
        //set dark light
        datePicker.isThemeDark = false
        datePicker.accentColor = context.resources.getColor(R.color.colorPrimary)
        datePicker.show(context.fragmentManager, "Timepickerdialog")
    }
}