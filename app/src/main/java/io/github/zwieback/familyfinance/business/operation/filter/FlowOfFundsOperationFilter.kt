package io.github.zwieback.familyfinance.business.operation.filter

import android.content.Context
import android.os.Parcel
import android.os.Parcelable

class FlowOfFundsOperationFilter : OperationFilter {

    constructor(context: Context) : super(context)

    constructor(filter: FlowOfFundsOperationFilter) : super(filter)

    private constructor(`in`: Parcel) : super(`in`)

    companion object {
        const val FLOW_OF_FUNDS_OPERATION_FILTER = "flowOfFundsOperationFilter"

        @JvmField
        var CREATOR: Parcelable.Creator<FlowOfFundsOperationFilter> =
            object : Parcelable.Creator<FlowOfFundsOperationFilter> {

                override fun createFromParcel(parcel: Parcel): FlowOfFundsOperationFilter {
                    return FlowOfFundsOperationFilter(parcel)
                }

                override fun newArray(size: Int): Array<FlowOfFundsOperationFilter?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
