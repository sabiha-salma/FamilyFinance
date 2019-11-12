package io.github.zwieback.familyfinance.business.sms_pattern.filter

import android.os.Parcel
import android.os.Parcelable
import io.github.zwieback.familyfinance.core.filter.EntityFilter

class SmsPatternFilter : EntityFilter {

    constructor() : super()

    constructor(filter: SmsPatternFilter) : super(filter)

    private constructor(`in`: Parcel) : super(`in`)

    companion object {
        const val SMS_PATTERN_FILTER = "smsPatternFilter"

        @JvmField
        var CREATOR: Parcelable.Creator<SmsPatternFilter> =
            object : Parcelable.Creator<SmsPatternFilter> {

                override fun createFromParcel(parcel: Parcel): SmsPatternFilter {
                    return SmsPatternFilter(parcel)
                }

                override fun newArray(size: Int): Array<SmsPatternFilter?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
