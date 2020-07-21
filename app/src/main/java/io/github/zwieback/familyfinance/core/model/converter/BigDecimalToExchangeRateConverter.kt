package io.github.zwieback.familyfinance.core.model.converter

import io.github.zwieback.familyfinance.extension.HighPrecision
import io.github.zwieback.familyfinance.extension.toBigDecimal
import io.github.zwieback.familyfinance.extension.toHighPrecision
import io.requery.Converter
import java.math.BigDecimal

/**
 * Workaround for reading of a DECIMAL value.
 */
class BigDecimalToExchangeRateConverter : Converter<BigDecimal, Long> {

    override fun getMappedType(): Class<BigDecimal> {
        return BigDecimal::class.java
    }

    override fun getPersistedType(): Class<Long> {
        return Long::class.java
    }

    override fun getPersistedSize(): Int? {
        return null
    }

    override fun convertToPersisted(value: BigDecimal?): Long? {
        return value?.let { value.toHighPrecision().value }
    }

    override fun convertToMapped(type: Class<out BigDecimal>, value: Long?): BigDecimal? {
        return value?.let { HighPrecision(value).toBigDecimal() }
    }
}
