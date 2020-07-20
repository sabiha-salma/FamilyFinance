package io.github.zwieback.familyfinance.extension

import android.content.Intent
import org.threeten.bp.LocalDate
import java.math.BigDecimal

fun Intent.getLocalDateExtra(name: String): LocalDate {
    return (this.getSerializableExtra(name) as? LocalDate) ?: LocalDate.now()
}

fun Intent.putBigDecimalExtra(name: String, value: BigDecimal?) {
    this.putExtra(name, value.toStringOrEmpty())
}

fun Intent.getBigDecimalExtra(name: String): BigDecimal? {
    val value = this.getStringExtra(name)
    return value?.parseAsBigDecimal()
}
