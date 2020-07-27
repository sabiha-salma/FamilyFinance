package io.github.zwieback.familyfinance.business.exchange_rate.filter

import io.github.zwieback.familyfinance.constant.IdConstants.EMPTY_ID
import io.github.zwieback.familyfinance.core.filter.EntityFilter
import io.github.zwieback.familyfinance.extension.BigDecimalParceler
import io.github.zwieback.familyfinance.extension.LocalDateParceler
import io.github.zwieback.familyfinance.extension.toEmptyId
import io.github.zwieback.familyfinance.extension.toNullableId
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler
import org.threeten.bp.LocalDate
import java.math.BigDecimal

@Parcelize
@TypeParceler<LocalDate?, LocalDateParceler>
@TypeParceler<BigDecimal?, BigDecimalParceler>
data class ExchangeRateFilter(
    var currencyId: Int = EMPTY_ID,
    var startDate: LocalDate? = null,
    var endDate: LocalDate? = null,
    var startValue: BigDecimal? = null,
    var endValue: BigDecimal? = null
) : EntityFilter() {

    fun takeCurrencyId(): Int? {
        return currencyId.toNullableId()
    }

    fun putCurrencyId(currencyId: Int?) {
        this.currencyId = currencyId.toEmptyId()
    }

    companion object {
        const val EXCHANGE_RATE_FILTER = "exchangeRateFilter"
    }
}
