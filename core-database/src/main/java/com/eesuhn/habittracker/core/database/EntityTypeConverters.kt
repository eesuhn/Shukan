package com.eesuhn.habittracker.core.database

import androidx.room.TypeConverter
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.Month

class EntityTypeConverters {

    @TypeConverter
    fun toInstant(epochMillis: Long): Instant = Instant.ofEpochMilli(epochMillis)

    @TypeConverter
    fun fromInstant(instant: Instant): Long = instant.toEpochMilli()

    @TypeConverter
    fun toDate(dateString: String?): LocalDate? =
        if (dateString == null) null else LocalDate.parse(dateString)

    @TypeConverter
    fun fromDate(localDate: LocalDate): String = localDate.toString()

    @TypeConverter
    fun toDayOfWeek(dayIndex: Int): DayOfWeek {
        // SQLite day of week: 0-6 with Sunday == 0
        return DayOfWeek.of(if (dayIndex == 0) 7 else dayIndex)
    }

    @TypeConverter
    fun toMonth(monthString: String): Month {
        // SQLite's strftime('%m, ...) returns month numbers with a zero padding (eg. 01, 02, 03).
        // We can't convert the returned value to Int directly because the leading zero makes
        // Kotlin (Java) parse the digits as an octal value. It works for the first 7 months,
        // then everything blows up in August...
        return Month.of(monthString.toInt())
    }
}