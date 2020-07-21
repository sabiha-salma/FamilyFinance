package io.github.zwieback.familyfinance.core.model.converter

import io.github.zwieback.familyfinance.extension.NormalPrecision
import io.github.zwieback.familyfinance.extension.toBigDecimal
import io.github.zwieback.familyfinance.extension.toNormalPrecision
import io.requery.Converter
import java.math.BigDecimal

/**
 * Workaround for reading of a DECIMAL value.
 */
class BigDecimalToWorthConverter : Converter<BigDecimal, Long> {

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
        return value?.toNormalPrecision()?.value
    }

    override fun convertToMapped(type: Class<out BigDecimal>, value: Long?): BigDecimal? {
        return if (value == 0L) {
            null
        } else {
            value?.let { NormalPrecision(value).toBigDecimal() }
        }
    }
}
