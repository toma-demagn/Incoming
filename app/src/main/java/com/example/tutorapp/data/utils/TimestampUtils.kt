package com.example.tutorapp.data.utils

class TimestampUtils {
    companion object {
        private fun splitTimestampToDayAndHour(timestamp: String): List<String> =
            timestamp.split('T')

        fun timestampToDate(timestamp: String, withYear: Boolean = false): String {
            val dateElements = splitTimestampToDayAndHour(timestamp)[0].split('-')
            val month = dateElements[1]
            val day = dateElements[2]
            return if (withYear) {
                val year = dateElements[0]
                "$day/$month/$year"
            } else {
                "$day/$month"
            }
        }

        fun getDayOfMonth(timestamp: String): Int =
            splitTimestampToDayAndHour(timestamp)[0].split('-')[2].toInt()

        fun getYear(timestamp: String): Int =
            splitTimestampToDayAndHour(timestamp)[0].split('-')[0].toInt()

        fun timestampToHour(timestamp: String): String =
            splitTimestampToDayAndHour(timestamp)[1].split('.')[0].drop(3)
    }
}