package io.github.zwieback.familyfinance.extension

import android.os.Parcel
import io.github.zwieback.familyfinance.constant.IdConstants.EMPTY_ID
import org.threeten.bp.LocalDate

fun Parcel.writeLocalDate(date: LocalDate?) {
    this.writeInt(date?.year ?: EMPTY_ID)
    this.writeInt(date?.monthValue ?: EMPTY_ID)
    this.writeInt(date?.dayOfMonth ?: EMPTY_ID)
}

fun Parcel.readLocalDate(): LocalDate? {
    val year = this.readInt()
    val month = this.readInt()
    val dayOfMonth = this.readInt()
    return if (year.isEmptyId() || month.isEmptyId() || dayOfMonth.isEmptyId()) {
        null
    } else {
        LocalDate.of(year, month, dayOfMonth)
    }
}
