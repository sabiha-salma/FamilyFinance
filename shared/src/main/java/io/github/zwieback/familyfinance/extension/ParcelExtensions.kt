package io.github.zwieback.familyfinance.extension

import android.os.Parcel
import io.github.zwieback.familyfinance.constant.IdConstants.EMPTY_ID
import io.github.zwieback.familyfinance.constant.StringConstants
import org.threeten.bp.LocalDate
import java.math.BigDecimal

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

fun Parcel.writeBigDecimal(value: BigDecimal?) {
    if (value == null) {
        this.writeString(StringConstants.EMPTY)
    } else {
        this.writeString(value.toStringOrEmpty())
    }
}

fun Parcel.readBigDecimal(): BigDecimal? {
    return this.readString()?.parseAsBigDecimal()
}
