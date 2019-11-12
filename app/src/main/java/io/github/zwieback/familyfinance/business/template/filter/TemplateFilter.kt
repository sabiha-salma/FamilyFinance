package io.github.zwieback.familyfinance.business.template.filter

import android.os.Parcel
import android.os.Parcelable
import io.github.zwieback.familyfinance.core.filter.EntityFilter

class TemplateFilter : EntityFilter {

    constructor() : super()

    constructor(filter: TemplateFilter) : super(filter)

    private constructor(`in`: Parcel) : super(`in`)

    companion object {
        const val TEMPLATE_FILTER = "templateFilter"

        @JvmField
        var CREATOR: Parcelable.Creator<TemplateFilter> =
            object : Parcelable.Creator<TemplateFilter> {

                override fun createFromParcel(parcel: Parcel): TemplateFilter {
                    return TemplateFilter(parcel)
                }

                override fun newArray(size: Int): Array<TemplateFilter?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
