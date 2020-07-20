package io.github.zwieback.familyfinance.constant

import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.chrono.IsoChronology
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeFormatterBuilder
import org.threeten.bp.format.ResolverStyle
import org.threeten.bp.format.SignStyle
import org.threeten.bp.temporal.ChronoField
import org.threeten.bp.temporal.IsoFields

object DateConstants {
    val ISO_LOCAL_WEEK: DateTimeFormatter =
        DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .appendLiteral('-')
            .appendValue(ChronoField.ALIGNED_WEEK_OF_YEAR, 2)
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT)
            .withChronology(IsoChronology.INSTANCE)

    val ISO_LOCAL_MONTH: DateTimeFormatter =
        DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .appendLiteral('-')
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT)
            .withChronology(IsoChronology.INSTANCE)

    val ISO_LOCAL_QUARTER: DateTimeFormatter =
        DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .appendLiteral("-Q")
            .appendValue(IsoFields.QUARTER_OF_YEAR, 1)
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT)
            .withChronology(IsoChronology.INSTANCE)

    val BANK_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")

    val EPOCH_DATE: LocalDate = LocalDate.of(1970, Month.JANUARY, 1)
}
