package io.github.zwieback.familyfinance.business.currency.filter

import io.github.zwieback.familyfinance.core.filter.EntityFilter
import kotlinx.android.parcel.Parcelize

@Parcelize
class CurrencyFilter : EntityFilter() {

    companion object {
        const val CURRENCY_FILTER = "currencyFilter"
    }
}
