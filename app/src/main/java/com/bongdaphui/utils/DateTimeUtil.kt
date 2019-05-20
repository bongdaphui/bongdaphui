package com.bongdaphui.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Button
import com.bongdaphui.R
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class DateTimeUtil {

    enum class DateFormatDefinition(val format: String) {
        DD_MM_YYYY("dd/MM/yyyy"),
        DD_MM_YYYY_HH_MM("dd/MM/yyyy HH:mm"),
        HH_MM("HH:mm");


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
    fun getTimeInMilliseconds(date: String, type: String): Long {

        var timeInMilliseconds = 0
        val sdf = SimpleDateFormat(type)

        try {
            val mDate = sdf.parse(date)
            timeInMilliseconds = mDate.time.toInt()
        } catch (e: ParseException) {
            Log.d(Constant().TAG, "getTimeInMilliseconds : ${e.message}")
        }
        return timeInMilliseconds.toLong()
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

    @SuppressLint("SetTextI18n")
    fun dialogTimePickerLight(context: Activity, bt: Button) {
        val curCalender = Calendar.getInstance()
        val datePicker = TimePickerDialog.newInstance({ view, hourOfDay, minute, second ->
            bt.text = "$hourOfDay : $minute"
        }, curCalender.get(Calendar.HOUR_OF_DAY), curCalender.get(Calendar.MINUTE), true)
        //set dark light
        datePicker.isThemeDark = false
        datePicker.accentColor = context.resources.getColor(R.color.colorPrimary)
        datePicker.show(context.fragmentManager, "Timepickerdialog")
    }
}