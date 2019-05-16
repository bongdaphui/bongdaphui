package com.bongdaphui.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Button
import com.bongdaphui.R
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {

     fun getAge(dateString: String): String {

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
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

    fun dialogDatePickerLight(context: Activity, bt: Button) {
        val curCalender = Calendar.getInstance()
        val datePicker = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
            { _, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateShipMillis = calendar.timeInMillis
                bt.text = Tools.getFormattedDateSimple(dateShipMillis)
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