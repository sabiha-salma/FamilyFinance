package io.github.zwieback.familyfinance.widget.filter

import android.text.InputFilter
import android.text.Spanned
import io.github.zwieback.familyfinance.util.NumberUtils
import io.github.zwieback.familyfinance.util.StringUtils.EMPTY

/**
 * Got an idea from [this answer](https://stackoverflow.com/a/20884556/8035065)
 * and improved it.
 *
 * Solved decimal separator problem.
 *
 * See [Decimal separator comma (',') with numberDecimal inputType in EditText](https://stackoverflow.com/q/3821539/8035065)
 *
 * See [Android - Comma as decimal separator on Numeric Keyboard](https://stackoverflow.com/a/14986892/8035065)
 */
class DecimalNumberInputFilter : InputFilter {

    /**
     * @return `null` to keep the original
     */
    override fun filter(
        source: CharSequence?, start: Int, end: Int,
        dest: Spanned, dstart: Int, dend: Int
    ): CharSequence? {
        if (source.isNullOrEmpty()) {
            return null
        }

        val before = dest.toString()
        val after = dest.toString().substring(0, dstart) +
                source.subSequence(start, end) +
                dest.toString().substring(dend)

        return if (needReplaceToDecimalSeparator(source)) {
            if (before.contains(DECIMAL_SEPARATOR)) EMPTY else DECIMAL_SEPARATOR
        } else {
            if (!NumberUtils.isTextABigDecimal(after)) EMPTY else null
        }
    }

    private fun needReplaceToDecimalSeparator(source: CharSequence): Boolean {
        return source.isNotEmpty() && SEPARATORS_TO_REPLACE.contains(source)
    }

    companion object {
        private val DECIMAL_SEPARATOR: String = NumberUtils.decimalSeparator
        private const val SEPARATORS_TO_REPLACE = ", ., ,"
    }
}
