package io.github.zwieback.familyfinance.core.model.converter

import io.github.zwieback.familyfinance.util.BigDecimalConverterUtils.bigDecimalToWorth
import io.github.zwieback.familyfinance.util.BigDecimalConverterUtils.worthToBigDecimal
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

    override fun convertToPersisted(value: BigDecimal): Long? {
        return bigDecimalToWorth(value)
    }

    override fun convertToMapped(type: Class<out BigDecimal>, value: Long?): BigDecimal? {
        return worthToBigDecimal(value)
    }
}
