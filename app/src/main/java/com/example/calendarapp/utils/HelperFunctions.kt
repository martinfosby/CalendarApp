package com.example.calendarapp.utils

import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone


data class MonthInfo(val index: Int){
    val name: String
        get() = when(index) {
            1 -> "Januar"
            2 -> "Februar"
            3 -> "Mars"
            4 -> "April"
            5 -> "Mai"
            6 -> "Juni"
            7 -> "Juli"
            8 -> "August"
            9 -> "September"
            10 -> "Oktober"
            11 -> "November"
            12 -> "Desember"
            else -> "Ukjent"
        }



}


fun getDaysInMonth(year: Int, month: Int): Int {
    val calendar = GregorianCalendar(TimeZone.getDefault())
    calendar.firstDayOfWeek = Calendar.MONDAY // Set Monday as the first day of the week
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month - 1) // Calendar months are zero-based
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}

fun getWeeksInMonth(year: Int, month: Int): List<Int> {
    val calendar = GregorianCalendar(TimeZone.getDefault())
    calendar.firstDayOfWeek = Calendar.MONDAY // Set Monday as the first day of the week
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month - 1) // Calendar months are zero-based
    var startDay = 1
    calendar.set(Calendar.DAY_OF_MONTH, startDay)

    val weekNumbers = mutableListOf<Int>()

    val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    while (calendar.get(Calendar.DAY_OF_MONTH) < lastDayOfMonth) {
        val weekNumber = calendar.get(Calendar.WEEK_OF_YEAR)
        if (weekNumber !in weekNumbers) {
            weekNumbers.add(weekNumber)
        }
        startDay++
        calendar.set(Calendar.DAY_OF_MONTH, startDay)
    }

    return weekNumbers
}

fun getDayOfWeek(year: Int, month: Int, dayOfMonth: Int): Int {
    val calendar = GregorianCalendar(TimeZone.getDefault())
    calendar.firstDayOfWeek = Calendar.MONDAY // Set Monday as the first day of the week
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month - 1) // Calendar months are zero-based
    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

    var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    // Adjust the day of the week to your desired representation
    dayOfWeek = (dayOfWeek + 5) % 7 + 1

    return dayOfWeek
}

fun getDaysOfMonth(year: Int, month: Int): List<Any> {
    val dayOfWeek = getDayOfWeek(year, month, 1)
    val daysOfMonth = getDaysInMonth(year, month)

    // Create a list representing the days of the month
    val days = mutableListOf<Any>()

    // Add empty spaces for the days before the first day of the month
    for (i in 1 until dayOfWeek) {
        days.add(" ")
    }

    // Add the actual days of the month
    for (day in 1..daysOfMonth) {
        days.add(day)
    }

    return days
}


fun numberOfDaysSinceFirstJan(year: Int, month: Int, dayOfMonth: Any): Int {
    if (dayOfMonth != " ") {
        val calendar = GregorianCalendar(TimeZone.getDefault())
        calendar.firstDayOfWeek = Calendar.MONDAY // Set Monday as the first day of the week
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1) // Calendar months are zero-based
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth as Int)

        return calendar.get(Calendar.DAY_OF_YEAR) - 1
    }
    else return 0
}

fun numberOfWorkDays(year: Int, month: Int): Int {
    val calendar = GregorianCalendar(TimeZone.getDefault())
    calendar.firstDayOfWeek = Calendar.MONDAY // Set Monday as the first day of the week
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month - 1) // Calendar months are zero-based

    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    var workingDays = 0

    for (day in 1..daysInMonth) {
        calendar.set(Calendar.DAY_OF_MONTH, day)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
            workingDays++
        }
    }

    return workingDays
}