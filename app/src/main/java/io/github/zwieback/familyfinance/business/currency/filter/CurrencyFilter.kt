package io.github.zwieback.familyfinance.business.currency.filter

import android.os.Parcel
import android.os.Parcelable
import io.github.zwieback.familyfinance.core.filter.EntityFilter

class CurrencyFilter : EntityFilter {

    constructor() : super()

    constructor(filter: CurrencyFilter) : super(filter)

    private constructor(`in`: Parcel) : super(`in`)

    companion object {
        const val CURRENCY_FILTER = "currencyFilter"

        @JvmField
        var CREATOR: Parcelable.Creator<CurrencyFilter> =
            object : Parcelable.Creator<CurrencyFilter> {

                override fun createFromParcel(parcel: Parcel): CurrencyFilter {
                    return CurrencyFilter(parcel)
                }

                override fun newArray(size: Int): Array<CurrencyFilter?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
