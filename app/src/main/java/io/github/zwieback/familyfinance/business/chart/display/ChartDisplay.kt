package io.github.zwieback.familyfinance.business.chart.display

import android.os.Parcelable

abstract class ChartDisplay<D : ChartDisplay<D>> : Parcelable {
    abstract fun needRefreshData(newDisplay: D): Boolean
}
