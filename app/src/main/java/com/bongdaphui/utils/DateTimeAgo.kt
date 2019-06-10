package com.bongdaphui.utils

import android.annotation.SuppressLint
import android.content.Context
import com.bongdaphui.R
import java.util.*

class DateTimeAgo {

    @SuppressLint("StringFormatInvalid")
    fun timeAgo(context: Context, millis: Long): String {
        val diff = Date().time - millis

        val r = context.resources

        val prefix = r.getString(R.string.time_ago_prefix)
        var suffix: String? = r.getString(R.string.time_ago_suffix)

        val seconds = (Math.abs(diff) / 1000).toDouble()
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val years = days / 365

        val words: String

        when {
            seconds < 45 -> {
                words = r.getString(R.string.time_ago_seconds, Math.round(seconds))
                suffix = ""
            }
            seconds < 90 -> words = r.getString(R.string.time_ago_minute, 1)
            minutes < 45 -> words = r.getString(R.string.time_ago_minutes, Math.round(minutes))
            minutes < 90 -> words = r.getString(R.string.time_ago_hour, 1)
            hours < 24 -> words = r.getString(R.string.time_ago_hours, Math.round(hours))
            hours < 42 -> {
                // words = r.getString(R.string.time_ago_day, 1);
                words = r.getString(R.string.time_ago_day)
                suffix = ""
            }
            days < 30 -> words = r.getString(R.string.time_ago_days, Math.round(days))
            days < 45 -> words = r.getString(R.string.time_ago_month, 1)
            days < 365 -> words = r.getString(R.string.time_ago_months, Math.round(days / 30))
            years < 1.5 -> words = r.getString(R.string.time_ago_year, 1)
            else -> words = r.getString(R.string.time_ago_years, Math.round(years))
        }

        val sb = StringBuilder()

        /*if (prefix.isNotEmpty()) {
            sb.append(prefix).append(" ")
        }*/

        sb.append(words)

        if (suffix != null && suffix.isNotEmpty()) {

            sb.append(" ").append(suffix)
        }


        return sb.toString().trim { it <= ' ' }
    }
}