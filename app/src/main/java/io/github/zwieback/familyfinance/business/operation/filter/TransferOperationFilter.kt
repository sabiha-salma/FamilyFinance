package io.github.zwieback.familyfinance.business.operation.filter

import android.os.Parcel
import android.os.Parcelable

class TransferOperationFilter : OperationFilter {

    constructor() : super()

    constructor(filter: TransferOperationFilter) : super(filter)

    private constructor(`in`: Parcel) : super(`in`)

    companion object {
        const val TRANSFER_OPERATION_FILTER = "transferOperationFilter"

        @JvmField
        var CREATOR: Parcelable.Creator<TransferOperationFilter> =
            object : Parcelable.Creator<TransferOperationFilter> {

                override fun createFromParcel(parcel: Parcel): TransferOperationFilter {
                    return TransferOperationFilter(parcel)
                }

                override fun newArray(size: Int): Array<TransferOperationFilter?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
