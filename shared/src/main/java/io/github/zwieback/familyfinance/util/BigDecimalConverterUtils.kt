package io.github.zwieback.familyfinance.util

import java.math.BigDecimal

object BigDecimalConverterUtils {

    private const val WORTH_POWER = 2
    private const val EXCHANGE_RATE_POWER = 8

    fun worthToBigDecimal(value: Long?): BigDecimal? {
        return longToBigDecimal(value, -WORTH_POWER)
    }

    fun bigDecimalToWorth(value: BigDecimal?): Long? {
        return bigDecimalToLong(value, WORTH_POWER)
    }

    fun exchangeRateToBigDecimal(value: Long?): BigDecimal? {
        return longToBigDecimal(value, -EXCHANGE_RATE_POWER)
    }

    fun bigDecimalToExchangeRate(value: BigDecimal?): Long? {
        return bigDecimalToLong(value, EXCHANGE_RATE_POWER)
    }

    private fun longToBigDecimal(value: Long?, power: Int): BigDecimal? {
        return value?.let { BigDecimal(value).scaleByPowerOfTen(power) }
    }

    private fun bigDecimalToLong(value: BigDecimal?, power: Int): Long? {
        return value?.scaleByPowerOfTen(power)?.toLong()
    }
}
