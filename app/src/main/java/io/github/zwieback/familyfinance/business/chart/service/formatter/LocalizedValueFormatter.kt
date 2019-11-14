package io.github.zwieback.familyfinance.business.chart.service.formatter

import com.github.mikephil.charting.formatter.ValueFormatter
import io.github.zwieback.familyfinance.util.NumberUtils
import java.math.BigDecimal

class LocalizedValueFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return NumberUtils.bigDecimalToString(
            BigDecimal.valueOf(value.toDouble()),
            NumberUtils.ACCOUNT_PLACES
        )
    }
}
