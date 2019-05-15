package com.bongdaphui.utils

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
}