package io.github.zwieback.familyfinance.extension

import io.github.zwieback.familyfinance.constant.DateConstants.BANK_DATE_FORMATTER
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.format.DateTimeParseException
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.IsoFields
import org.threeten.bp.temporal.TemporalAdjusters
import java.util.*

fun String?.isLocalDate(): Boolean {
    return try {
        this.toLocalDate() != null
    } catch (e: DateTimeParseException) {
        false
    }
}

fun String?.toLocalDate(): LocalDate? {
    return if (!this.isNullOrEmpty()) {
        LocalDate.parse(this)
    } else {
        null
    }
}

fun String?.bankDateToLocalDate(): LocalDate {
    return if (!this.isNullOrEmpty()) {
        LocalDate.from(BANK_DATE_FORMATTER.parse(this))
    } else {
        LocalDate.now()
    }
}

fun LocalDate?.toStringOrEmpty(): String {
    return this?.toString().orEmpty()
}

fun LocalDate.startOfMonth(): LocalDate {
    return this.with(TemporalAdjusters.firstDayOfMonth())
}

fun LocalDate.endOfMonth(): LocalDate {
    return this.with(TemporalAdjusters.lastDayOfMonth())
}

fun LocalDate.getWeekOfYear(): Int {
    return this.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
}

fun LocalDate.getQuarterOfYear(): Int {
    return this.get(IsoFields.QUARTER_OF_YEAR)
}

fun LocalDate.plusQuarters(numberOfQuarters: Long): LocalDate {
    return this.plus(numberOfQuarters, IsoFields.QUARTER_YEARS)
}

// region Calendar

/**
 * Workaround for month of calendar (start with 0)
 *
 * @see [Calendar.MONTH]
 */
private const val MONTH_OF_CALENDAR_INCREMENT = 1

data class CalendarDate(val year: Int, val month: Int, val day: Int)

/**
 * @see [Calendar.MONTH]
 */
fun CalendarDate.toLocalDateWithMonthFix(): LocalDate {
    return LocalDate.of(year, month + MONTH_OF_CALENDAR_INCREMENT, day)
}

/**
 * @see [Calendar.MONTH]
 */
fun LocalDate.toCalendarDateWithMonthFix(): CalendarDate {
    return CalendarDate(year, monthValue - MONTH_OF_CALENDAR_INCREMENT, dayOfMonth)
}

// endregion Calendar

// region Epoch

/**
 * [Unix time](https://en.wikipedia.org/wiki/Unix_time)
 */
private val EPOCH_DATE: LocalDate = LocalDate.of(1970, Month.JANUARY, 1)

fun LocalDate.toDaysFromEpoch(): Long {
    return ChronoUnit.DAYS.between(EPOCH_DATE, this)
}

fun LocalDate.toWeeksFromEpoch(): Long {
    return ChronoUnit.WEEKS.between(EPOCH_DATE, this)
}

fun LocalDate.toMonthsFromEpoch(): Long {
    return ChronoUnit.MONTHS.between(EPOCH_DATE, this)
}

fun LocalDate.toQuartersFromEpoch(): Long {
    return IsoFields.QUARTER_YEARS.between(EPOCH_DATE, this)
}

interface SpecificPeriodFromEpoch {
    fun toLocalDate(): LocalDate
}

inline class DaysFromEpoch(val value: Long) : SpecificPeriodFromEpoch {
    override fun toLocalDate(): LocalDate = EPOCH_DATE.plusDays(value)
}

inline class WeeksFromEpoch(val value: Long) : SpecificPeriodFromEpoch {
    override fun toLocalDate(): LocalDate = EPOCH_DATE.plusWeeks(value)
}

inline class MonthsFromEpoch(val value: Long) : SpecificPeriodFromEpoch {
    override fun toLocalDate(): LocalDate = EPOCH_DATE.plusMonths(value)
}

inline class QuartersFromEpoch(val value: Long) : SpecificPeriodFromEpoch {
    override fun toLocalDate(): LocalDate = EPOCH_DATE.plusQuarters(value)
}

// endregion Epoch
