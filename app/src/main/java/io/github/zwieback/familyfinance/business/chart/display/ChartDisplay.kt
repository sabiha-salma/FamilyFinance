package io.github.zwieback.familyfinance.business.chart.display

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.CallSuper

abstract class ChartDisplay<D : ChartDisplay<D>> : Parcelable {

    internal constructor() {
        init()
    }

    internal constructor(display: ChartDisplay<D>) : this()

    internal constructor(`in`: Parcel) {
        readFromParcel(`in`)
    }

    @CallSuper
    protected open fun init() {
        // do nothing
    }

    protected open fun readFromParcel(`in`: Parcel) {
        // do nothing
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        // do nothing
    }

    override fun describeContents(): Int {
        return 0
    }

    abstract fun needRefreshData(newDisplay: D): Boolean
}
