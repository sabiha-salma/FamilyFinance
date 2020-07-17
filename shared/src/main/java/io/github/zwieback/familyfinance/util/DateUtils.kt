package io.github.zwieback.familyfinance.util

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import io.github.zwieback.familyfinance.extension.EMPTY_ID
import io.github.zwieback.familyfinance.extension.isEmptyId
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.chrono.IsoChronology
import org.threeten.bp.format.*
import org.threeten.bp.temporal.ChronoField
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.IsoFields
import org.threeten.bp.temporal.TemporalAdjusters
import java.util.*

object DateUtils {

    @JvmStatic
    val ISO_LOCAL_WEEK: DateTimeFormatter =
            DateTimeFormatterBuilder()
                    .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
                    .appendLiteral('-')
                    .appendValue(ChronoField.ALIGNED_WEEK_OF_YEAR, 2)
                    .toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT)
                    .withChronology(IsoChronology.INSTANCE)

    @JvmStatic
    val ISO_LOCAL_MONTH: DateTimeFormatter =
            DateTimeFormatterBuilder()
                    .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
                    .appendLiteral('-')
                    .appendValue(ChronoField.MONTH_OF_YEAR, 2)
                    .toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT)
                    .withChronology(IsoChronology.INSTANCE)

    @JvmStatic
    val ISO_LOCAL_QUARTER: DateTimeFormatter =
            DateTimeFormatterBuilder()
                    .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
                    .appendLiteral("-Q")
                    .appendValue(IsoFields.QUARTER_OF_YEAR, 1)
                    .toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT)
                    .withChronology(IsoChronology.INSTANCE)

    @JvmStatic
    val BANK_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")

    private val EPOCH_DATE: LocalDate = epochDay

    // bundle keys
    private const val KEY_YEAR = "keyYear"
    private const val KEY_MONTH = "keyMonth"
    private const val KEY_DAY_OF_MONTH = "keyDayOfMonth"

    // workaround for month of calendar (start with 0)
    private const val MONTH_OF_CALENDAR_INCREMENT = 1

    private val epochDay: LocalDate
        get() = LocalDate.of(1970, Month.JANUARY, 1)

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
    fun writeLocalDateToBundle(out: Bundle, date: LocalDate) {
        out.putInt(KEY_YEAR, date.year)
        out.putInt(KEY_MONTH, date.monthValue)
        out.putInt(KEY_DAY_OF_MONTH, date.dayOfMonth)
    }

    @JvmStatic
    fun readLocalDateFromBundle(input: Bundle): LocalDate {
        val year = input.getInt(KEY_YEAR)
        val month = input.getInt(KEY_MONTH)
        val dayOfMonth = input.getInt(KEY_DAY_OF_MONTH)
        return LocalDate.of(year, month, dayOfMonth)
    }

    @JvmStatic
    fun writeLocalDateToIntent(out: Intent, name: String, date: LocalDate) {
        out.putExtra(name + KEY_YEAR, date.year)
        out.putExtra(name + KEY_MONTH, date.monthValue)
        out.putExtra(name + KEY_DAY_OF_MONTH, date.dayOfMonth)
    }

    @JvmStatic
    fun readLocalDateFromIntent(input: Intent, name: String): LocalDate {
        val today = now()
        val year = input.getIntExtra(name + KEY_YEAR, today.year)
        val month = input.getIntExtra(name + KEY_MONTH, today.monthValue)
        val dayOfMonth = input.getIntExtra(name + KEY_DAY_OF_MONTH, today.dayOfMonth)
        return LocalDate.of(year, month, dayOfMonth)
    }

    @JvmStatic
    fun writeLocalDateToParcel(out: Parcel, date: LocalDate?) {
        if (date == null) {
            out.writeInt(EMPTY_ID)
            out.writeInt(EMPTY_ID)
            out.writeInt(EMPTY_ID)
        } else {
            out.writeInt(date.year)
            out.writeInt(date.monthValue)
            out.writeInt(date.dayOfMonth)
        }
    }

    @JvmStatic
    fun readLocalDateFromParcel(input: Parcel): LocalDate? {
        val year = input.readInt()
        val month = input.readInt()
        val dayOfMonth = input.readInt()
        return if (year.isEmptyId() || month.isEmptyId() || dayOfMonth.isEmptyId()) {
            null
        } else {
            LocalDate.of(year, month, dayOfMonth)
        }
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
