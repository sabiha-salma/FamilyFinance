package io.github.zwieback.familyfinance.extension

import android.os.Bundle
import org.threeten.bp.LocalDate

private const val KEY_YEAR = "keyYear"
private const val KEY_MONTH = "keyMonth"
private const val KEY_DAY_OF_MONTH = "keyDayOfMonth"

fun Bundle.writeLocalDate(date: LocalDate) {
    this.putInt(KEY_YEAR, date.year)
    this.putInt(KEY_MONTH, date.monthValue)
    this.putInt(KEY_DAY_OF_MONTH, date.dayOfMonth)
}

fun Bundle.readLocalDate(): LocalDate {
    val year = this.getInt(KEY_YEAR)
    val month = this.getInt(KEY_MONTH)
    val dayOfMonth = this.getInt(KEY_DAY_OF_MONTH)
    return LocalDate.of(year, month, dayOfMonth)
}
