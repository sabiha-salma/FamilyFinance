package io.github.zwieback.familyfinance.extension

import io.github.zwieback.familyfinance.constant.BigDecimalConstants.BANK_COMMA_FORMAT
import io.github.zwieback.familyfinance.constant.BigDecimalConstants.BANK_DOT_FORMAT
import io.github.zwieback.familyfinance.constant.BigDecimalConstants.BIG_DECIMAL_FORMAT
import io.github.zwieback.familyfinance.constant.StringConstants.EMPTY
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.ParseException

fun String.isBigDecimal(): Boolean {
    return try {
        this.parseAsBigDecimal() != null
    } catch (e: NumberFormatException) {
        false
    }
}

fun String.parseAsBigDecimal(): BigDecimal? {
    try {
        return if (this.isEmpty()) {
            null
        } else {
            BIG_DECIMAL_FORMAT.parse(this) as BigDecimal
        }
    } catch (e: ParseException) {
        throw NumberFormatException(e.message)
    }
}

fun String.bankNumberToBigDecimal(): BigDecimal? {
    return try {
        this.bankNumberWithDotToBigDecimal()
    } catch (e: NumberFormatException) {
        try {
            this.bankNumberWithCommaToBigDecimal()
        } catch (e: NumberFormatException) {
            null
        }
    }
}

private fun String.bankNumberWithDotToBigDecimal(): BigDecimal? {
    try {
        return if (this.isEmpty()) {
            null
        } else {
            BANK_DOT_FORMAT.parse(this)?.let { number ->
                BigDecimal(number.toString())
            }
        }
    } catch (e: ParseException) {
        throw NumberFormatException(e.message)
    }
}

private fun String.bankNumberWithCommaToBigDecimal(): BigDecimal? {
    try {
        return if (this.isEmpty()) {
            null
        } else {
            BANK_COMMA_FORMAT.parse(this)?.let { number ->
                BigDecimal(number.toString())
            }
        }
    } catch (e: ParseException) {
        throw NumberFormatException(e.message)
    }
}

fun BigDecimal?.toStringOrEmpty(): String {
    return this.toStringOrDefault(EMPTY)
}

fun BigDecimal?.toStringOrDefault(defaultValue: String): String {
    return this?.let { BIG_DECIMAL_FORMAT.format(this) } ?: defaultValue
}

fun BigDecimal.toStringWithPlaces(places: Int): String {
    val value = this.setScale(places, RoundingMode.HALF_EVEN)
    return BIG_DECIMAL_FORMAT.format(value)
}

// region Normal and High Precision values

private const val NORMAL_PRECISION_POWER = 2
private const val HIGH_PRECISION_POWER = 8

inline class NormalPrecision(val value: Long)
inline class HighPrecision(val value: Long)

fun NormalPrecision.toBigDecimal(): BigDecimal {
    return value.toBigDecimal(-NORMAL_PRECISION_POWER)
}

fun BigDecimal.toNormalPrecision(): NormalPrecision {
    return NormalPrecision(this.toLong(NORMAL_PRECISION_POWER))
}

fun HighPrecision.toBigDecimal(): BigDecimal {
    return value.toBigDecimal(-HIGH_PRECISION_POWER)
}

fun BigDecimal.toHighPrecision(): HighPrecision {
    return HighPrecision(this.toLong(HIGH_PRECISION_POWER))
}

private fun Long.toBigDecimal(power: Int): BigDecimal {
    return BigDecimal(this).scaleByPowerOfTen(power)
}

private fun BigDecimal.toLong(power: Int): Long {
    return this.scaleByPowerOfTen(power).toLong()
}

// endregion Normal and High Precision values
