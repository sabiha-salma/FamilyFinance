/**
 * This extensions and parcelers are taken from [that article](https://medium.com/-/59a5adcd5909)
 */
package io.github.zwieback.familyfinance.extension

import android.os.Parcel
import kotlinx.parcelize.Parceler
import org.threeten.bp.LocalDate
import java.math.BigDecimal
import java.math.BigInteger

private inline fun <T> Parcel.readNullable(reader: () -> T): T? =
    if (readInt() != 0) {
        reader()
    } else {
        null
    }

private inline fun <T> Parcel.writeNullable(value: T?, writer: T.() -> Unit) =
    if (value != null) {
        writeInt(1)
        value.writer()
    } else {
        writeInt(0)
    }

object LocalDateParceler : Parceler<LocalDate?> {

    override fun create(parcel: Parcel): LocalDate? =
        parcel.readNullable {
            val year = parcel.readInt()
            val month = parcel.readInt()
            val dayOfMonth = parcel.readInt()
            LocalDate.of(year, month, dayOfMonth)
        }

    override fun LocalDate?.write(parcel: Parcel, flags: Int) =
        parcel.writeNullable(this) {
            parcel.writeInt(this.year)
            parcel.writeInt(this.monthValue)
            parcel.writeInt(this.dayOfMonth)
        }
}

object BigDecimalParceler : Parceler<BigDecimal?> {

    override fun create(parcel: Parcel): BigDecimal? =
        parcel.readNullable {
            BigDecimal(BigInteger(parcel.createByteArray()), parcel.readInt())
        }

    override fun BigDecimal?.write(parcel: Parcel, flags: Int) =
        parcel.writeNullable(this) {
            parcel.writeByteArray(unscaledValue().toByteArray())
            parcel.writeInt(scale())
        }
}
