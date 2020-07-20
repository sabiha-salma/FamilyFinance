package io.github.zwieback.familyfinance.util

import io.github.zwieback.familyfinance.constant.DateConstants.BANK_DATE_FORMATTER
import io.github.zwieback.familyfinance.constant.DateConstants.EPOCH_DATE
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.IsoFields
import org.threeten.bp.temporal.TemporalAdjusters
import java.util.*

object DateUtils {

    // workaround for month of calendar (start with 0)
    private const val MONTH_OF_CALENDAR_INCREMENT = 1


    @JvmStatic
    fun isTextAnLocalDate(text: String?): Boolean {
        return try {
            stringToLocalDate(text) != null
        } catch (e: DateTimeParseException) {
            false
        }
    }

    @JvmStatic
    fun stringToLocalDate(text: String?): LocalDate? {
        return if (!text.isNullOrEmpty()) {
            LocalDate.parse(text)
        } else {
            null
        }
    }

    @JvmStatic
    fun bankDateToLocalDate(text: String?): LocalDate {
        return if (!text.isNullOrEmpty()) {
            LocalDate.from(BANK_DATE_FORMATTER.parse(text))
        } else {
            now()
        }
    }

    @JvmStatic
    fun localDateToString(localDate: LocalDate?): String {
        return localDate?.toString().orEmpty()
    }

    @JvmStatic
    fun localDateToString(localDate: LocalDate?, formatter: DateTimeFormatter): String {
        return localDate?.format(formatter).orEmpty()
    }

    @JvmStatic
    fun localDateToCalendar(localDate: LocalDate?): Calendar {
        return localDate?.let {
            Calendar.getInstance().apply {
                set(
                    localDate.year,
                    localDate.monthValue - MONTH_OF_CALENDAR_INCREMENT,
                    localDate.dayOfMonth
                )
            }
        } ?: Calendar.getInstance()
    }

    @JvmStatic
    fun calendarDateToLocalDate(
        calendarYear: Int,
        calendarMonth: Int,
        calendarDay: Int
    ): LocalDate {
        return LocalDate.of(calendarYear, calendarMonth + MONTH_OF_CALENDAR_INCREMENT, calendarDay)
    }

    @JvmStatic
    fun now(): LocalDate {
        return LocalDate.now()
    }

    @JvmStatic
    fun startOfMonth(): LocalDate {
        return now().with(TemporalAdjusters.firstDayOfMonth())
    }

    @JvmStatic
    fun endOfMonth(): LocalDate {
        return now().with(TemporalAdjusters.lastDayOfMonth())
    }

    @JvmStatic
    fun localDateToEpochDay(date: LocalDate): Long {
        return ChronoUnit.DAYS.between(EPOCH_DATE, date)
    }

    @JvmStatic
    fun epochDayToLocalDate(daysFromEpoch: Long): LocalDate {
        return EPOCH_DATE.plusDays(daysFromEpoch)
    }

    @JvmStatic
    fun localDateToEpochWeek(date: LocalDate): Long {
        return ChronoUnit.WEEKS.between(EPOCH_DATE, date)
    }

    @JvmStatic
    fun epochWeekToLocalDate(monthsFromWeek: Long): LocalDate {
        return EPOCH_DATE.plusWeeks(monthsFromWeek)
    }

    @JvmStatic
    fun localDateToEpochMonth(date: LocalDate): Long {
        return ChronoUnit.MONTHS.between(EPOCH_DATE, date)
    }

    @JvmStatic
    fun epochMonthToLocalDate(monthsFromEpoch: Long): LocalDate {
        return EPOCH_DATE.plusMonths(monthsFromEpoch)
    }

    @JvmStatic
    fun localDateToEpochQuarter(date: LocalDate): Long {
        return IsoFields.QUARTER_YEARS.between(EPOCH_DATE, date)
    }

    @JvmStatic
    fun epochQuarterToLocalDate(quartersFromWeek: Long): LocalDate {
        return plusQuarters(EPOCH_DATE, quartersFromWeek)
    }

    @JvmStatic
    fun plusQuarters(date: LocalDate, numberOfQuarters: Long): LocalDate {
        return date.plus(numberOfQuarters, IsoFields.QUARTER_YEARS)
    }

    @JvmStatic
    fun extractWeekOfYear(date: LocalDate): Int {
        return date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
    }

    @JvmStatic
    fun extractQuarterOfYear(date: LocalDate): Int {
        return date.get(IsoFields.QUARTER_OF_YEAR)
    }
}
