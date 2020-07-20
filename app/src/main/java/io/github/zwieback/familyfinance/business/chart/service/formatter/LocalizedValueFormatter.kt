package io.github.zwieback.familyfinance.business.chart.service.formatter

import com.github.mikephil.charting.formatter.ValueFormatter
import io.github.zwieback.familyfinance.constant.AccountNumberConstants
import io.github.zwieback.familyfinance.extension.toStringWithPlaces
import java.math.BigDecimal

class LocalizedValueFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return BigDecimal.valueOf(value.toDouble())
            .toStringWithPlaces(AccountNumberConstants.ACCOUNT_PLACES)
    }
}
