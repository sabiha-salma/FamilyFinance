package io.github.zwieback.familyfinance.business.operation.filter

import android.os.Parcel
import android.os.Parcelable

class IncomeOperationFilter : OperationFilter {

    constructor() : super()

    constructor(filter: IncomeOperationFilter) : super(filter)

    private constructor(`in`: Parcel) : super(`in`)

    companion object {
        const val INCOME_OPERATION_FILTER = "incomeOperationFilter"

        @JvmField
        var CREATOR: Parcelable.Creator<IncomeOperationFilter> =
            object : Parcelable.Creator<IncomeOperationFilter> {

                override fun createFromParcel(parcel: Parcel): IncomeOperationFilter {
                    return IncomeOperationFilter(parcel)
                }

                override fun newArray(size: Int): Array<IncomeOperationFilter?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
