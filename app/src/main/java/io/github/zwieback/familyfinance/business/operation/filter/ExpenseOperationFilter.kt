package io.github.zwieback.familyfinance.business.operation.filter

import android.content.Context
import android.os.Parcel
import android.os.Parcelable

class ExpenseOperationFilter : OperationFilter {

    constructor(context: Context) : super(context)

    constructor(filter: ExpenseOperationFilter) : super(filter)

    private constructor(`in`: Parcel) : super(`in`)

    companion object {
        const val EXPENSE_OPERATION_FILTER = "expenseOperationFilter"

        @JvmField
        var CREATOR: Parcelable.Creator<ExpenseOperationFilter> =
            object : Parcelable.Creator<ExpenseOperationFilter> {

                override fun createFromParcel(parcel: Parcel): ExpenseOperationFilter {
                    return ExpenseOperationFilter(parcel)
                }

                override fun newArray(size: Int): Array<ExpenseOperationFilter?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
