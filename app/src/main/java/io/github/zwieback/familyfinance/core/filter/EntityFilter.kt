package io.github.zwieback.familyfinance.core.filter

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.CallSuper

abstract class EntityFilter : Parcelable {

    protected constructor() {
        init()
    }

    protected constructor(context: Context) {
        init(context)
    }

    protected constructor(filter: EntityFilter) : this()

    protected constructor(`in`: Parcel) {
        readFromParcel(`in`)
    }

    @CallSuper
    protected open fun init() {
        // stub
    }

    @CallSuper
    protected open fun init(context: Context) {
        // stub
    }

    protected open fun readFromParcel(`in`: Parcel) {
        // stub
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        // stub
    }

    override fun describeContents(): Int {
        return 0
    }
}
