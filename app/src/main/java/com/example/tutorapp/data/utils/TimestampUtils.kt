package com.example.tutorapp.data.utils

/**
 * TimestampUtils
 * Class of "static" methods dedicated to the timestamp strings values returned by the API
 */
class TimestampUtils {

    companion object {

        /**
         * Split a timestamp string in a list to have the date on a side and the time on the other side
         */
        private fun splitTimestampToDateAndHour(timestamp: String): List<String> =
            timestamp.split('T')

        /**
         * Transforms a given timestamp string on a formatted date string
         * The withYear param indicates that we return the year or not in the result
         */
        fun timestampToDate(timestamp: String, withYear: Boolean = false): String {
            val dateElements = splitTimestampToDateAndHour(timestamp)[0].split('-')
            val month = dateElements[1]
            val day = dateElements[2]
            return if (withYear) {
                val year = dateElements[0]
                "$day/$month/$year"
            } else {
                "$day/$month"
            }
        }

        /**
         * Returns the day of the given string
         */
        fun getDayOfMonth(timestamp: String): Int =
            splitTimestampToDateAndHour(timestamp)[0].split('-')[2].toInt()

        /**
         * Returns the year of the given string
         */
        fun getYear(timestamp: String): Int =
            splitTimestampToDateAndHour(timestamp)[0].split('-')[0].toInt()

        /**
         * Returns the hour of the given string
         */
        fun timestampToHour(timestamp: String): String =
            splitTimestampToDateAndHour(timestamp)[1].split('.')[0].dropLast(3)
    }
}