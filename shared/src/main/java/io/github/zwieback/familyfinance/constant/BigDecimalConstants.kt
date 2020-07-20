package io.github.zwieback.familyfinance.constant

import io.github.zwieback.familyfinance.util.ConfigurationUtils
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object BigDecimalConstants {
    private const val DEFAULT_GROUPING_SIZE = 3
    val BIG_DECIMAL_FORMAT: DecimalFormat
    val BANK_DOT_FORMAT: DecimalFormat
    val BANK_COMMA_FORMAT: DecimalFormat

    init {
        val symbols = DecimalFormatSymbols(ConfigurationUtils.systemLocale)
        // see https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
        // and https://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html
        BIG_DECIMAL_FORMAT = DecimalFormat("0.00######", symbols)
        BIG_DECIMAL_FORMAT.isParseBigDecimal = true
        if (!BIG_DECIMAL_FORMAT.isGroupingUsed) {
            BIG_DECIMAL_FORMAT.isGroupingUsed = true
        }
        if (BIG_DECIMAL_FORMAT.groupingSize < DEFAULT_GROUPING_SIZE) {
            BIG_DECIMAL_FORMAT.groupingSize = DEFAULT_GROUPING_SIZE
        }

        val bankDotSymbols = DecimalFormatSymbols(Locale.ENGLISH)
        BANK_DOT_FORMAT = DecimalFormat("0.00", bankDotSymbols)

        val bankCommaSymbols = DecimalFormatSymbols(Locale.GERMAN)
        BANK_COMMA_FORMAT = DecimalFormat("0.00", bankCommaSymbols)
    }

    @JvmStatic
    val decimalSeparator: String
        get() = BIG_DECIMAL_FORMAT.decimalFormatSymbols.decimalSeparator.toString()
}
