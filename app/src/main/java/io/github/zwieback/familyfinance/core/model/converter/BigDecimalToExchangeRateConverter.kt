package io.github.zwieback.familyfinance.core.model.converter

import io.github.zwieback.familyfinance.util.BigDecimalConverterUtils.bigDecimalToExchangeRate
import io.github.zwieback.familyfinance.util.BigDecimalConverterUtils.exchangeRateToBigDecimal
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

    override fun convertToPersisted(value: BigDecimal): Long? {
        return bigDecimalToExchangeRate(value)
    }

    override fun convertToMapped(type: Class<out BigDecimal>, value: Long?): BigDecimal? {
        return exchangeRateToBigDecimal(value)
    }
}
