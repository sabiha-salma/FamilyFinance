package io.github.zwieback.familyfinance.business.person.filter

import android.os.Parcel
import android.os.Parcelable
import io.github.zwieback.familyfinance.core.filter.EntityFolderFilter

class PersonFilter : EntityFolderFilter {

    constructor() : super()

    constructor(filter: PersonFilter) : super(filter)

    private constructor(`in`: Parcel) : super(`in`)

    companion object {
        const val PERSON_FILTER = "personFilter"

        @JvmField
        var CREATOR: Parcelable.Creator<PersonFilter> = object : Parcelable.Creator<PersonFilter> {

            override fun createFromParcel(parcel: Parcel): PersonFilter {
                return PersonFilter(parcel)
            }

            override fun newArray(size: Int): Array<PersonFilter?> {
                return arrayOfNulls(size)
            }
        }
    }
}
