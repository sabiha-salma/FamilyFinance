package io.github.zwieback.familyfinance.util

import android.content.Intent
import android.os.Parcel
import io.github.zwieback.familyfinance.constant.StringConstants.EMPTY
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.util.*
import java.util.regex.Pattern

/**
 * todo refactor this class and another utils
 */
object NumberUtils {

    const val ACCOUNT_PLACES = 2

    const val UI_DEBOUNCE_TIMEOUT = 500L

    private const val DEFAULT_GROUPING_SIZE = 3
    private val bigDecimalFormat: DecimalFormat
    private val bankDotFormat: DecimalFormat
    private val bankCommaFormat: DecimalFormat

    private val SIGNED_NUMBER_PATTERN = Pattern.compile("\\d+")
    private val ACCOUNT_NUMBER_PATTERN = Pattern.compile("\\d{20}")

    @JvmStatic
    val decimalSeparator: String
        get() = bigDecimalFormat.decimalFormatSymbols.decimalSeparator.toString()

    init {
        val symbols = DecimalFormatSymbols(ConfigurationUtils.systemLocale)
        // see https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
        // and https://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html
        bigDecimalFormat = DecimalFormat("0.00######", symbols)
        bigDecimalFormat.isParseBigDecimal = true
        if (!bigDecimalFormat.isGroupingUsed) {
            bigDecimalFormat.isGroupingUsed = true
        }
        if (bigDecimalFormat.groupingSize < DEFAULT_GROUPING_SIZE) {
            bigDecimalFormat.groupingSize = DEFAULT_GROUPING_SIZE
        }

        val bankDotSymbols = DecimalFormatSymbols(Locale.ENGLISH)
        bankDotFormat = DecimalFormat("0.00", bankDotSymbols)
        val bankCommaSymbols = DecimalFormatSymbols(Locale.GERMAN)
        bankCommaFormat = DecimalFormat("0.00", bankCommaSymbols)
    }

    @JvmStatic
    fun isTextAnInteger(text: String): Boolean {
        return try {
            stringToInt(text)
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    @JvmStatic
    fun isTextABigDecimal(text: String): Boolean {
        return try {
            stringToBigDecimal(text) != null
        } catch (e: NumberFormatException) {
            false
        }
    }

    @JvmStatic
    fun isTextASignedNumber(text: String): Boolean {
        return SIGNED_NUMBER_PATTERN.matcher(text).matches()
    }

    @JvmStatic
    fun isTextAnAccountNumber(text: String): Boolean {
        return ACCOUNT_NUMBER_PATTERN.matcher(text).matches()
    }

    @JvmStatic
    fun stringToInt(text: String): Int {
        return Integer.parseInt(text)
    }

    @JvmStatic
    fun stringToInteger(text: String?): Int? {
        return try {
            return if (text.isNullOrEmpty()) {
                null
            } else {
                Integer.valueOf(text)
            }
        } catch (e: NumberFormatException) {
            null
        }
    }

    @JvmStatic
    fun stringToBigDecimal(text: String?): BigDecimal? {
        try {
            return if (text.isNullOrEmpty()) {
                null
            } else {
                bigDecimalFormat.parse(text) as BigDecimal
            }
        } catch (e: ParseException) {
            throw NumberFormatException(e.message)
        }
    }

    @JvmStatic
    fun bankNumberToBigDecimal(text: String?): BigDecimal? {
        return try {
            bankNumberWithDotToBigDecimal(text)
        } catch (e: NumberFormatException) {
            bankNumberWithCommaToBigDecimal(text)
        }
    }

    @JvmStatic
    private fun bankNumberWithDotToBigDecimal(text: String?): BigDecimal? {
        try {
            return if (text.isNullOrEmpty()) {
                null
            } else {
                bankDotFormat.parse(text)?.let { number ->
                    BigDecimal(number.toString())
                }
            }
        } catch (e: ParseException) {
            throw NumberFormatException(e.message)
        }
    }

    @JvmStatic
    private fun bankNumberWithCommaToBigDecimal(text: String?): BigDecimal? {
        try {
            return if (text.isNullOrEmpty()) {
                null
            } else {
                bankCommaFormat.parse(text)?.let { number ->
                    BigDecimal(number.toString())
                }
            }
        } catch (e: ParseException) {
            throw NumberFormatException(e.message)
        }
    }

    @JvmStatic
    fun integerToString(number: Int?): String {
        return number?.toString().orEmpty()
    }

    @JvmStatic
    fun intToString(number: Int): String {
        return number.toString()
    }

    @JvmStatic
    fun bigDecimalToString(number: BigDecimal?): String {
        return bigDecimalToString(number, EMPTY)
    }

    @JvmStatic
    fun bigDecimalToString(number: BigDecimal?, defaultValue: String): String {
        return number?.let { bigDecimalFormat.format(number) } ?: defaultValue
    }

    @JvmStatic
    fun bigDecimalToString(number: BigDecimal?, places: Int): String {
        return number?.let {
            val value = number.setScale(places, RoundingMode.HALF_EVEN)
            bigDecimalFormat.format(value)
        }.orEmpty()
    }

    @JvmStatic
    fun writeBigDecimalToParcel(out: Parcel, value: BigDecimal?) {
        if (value == null) {
            out.writeString(EMPTY)
        } else {
            out.writeString(bigDecimalToString(value))
        }
    }

    @JvmStatic
    fun readBigDecimalFromParcel(input: Parcel): BigDecimal? {
        return stringToBigDecimal(input.readString())
    }

    @JvmStatic
    fun writeBigDecimalToIntent(out: Intent, name: String, value: BigDecimal?) {
        out.putExtra(name, bigDecimalToString(value))
    }

    @JvmStatic
    fun readBigDecimalFromIntent(input: Intent, name: String): BigDecimal? {
        val value = input.getStringExtra(name)
        return stringToBigDecimal(value)
    }
}
