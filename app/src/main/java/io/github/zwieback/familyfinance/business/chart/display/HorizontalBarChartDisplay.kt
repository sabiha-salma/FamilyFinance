package io.github.zwieback.familyfinance.business.chart.display

import android.os.Parcel
import android.os.Parcelable
import io.github.zwieback.familyfinance.business.chart.display.type.HorizontalBarChartGroupByType

class HorizontalBarChartDisplay : ChartDisplay<HorizontalBarChartDisplay> {

    /**
     * Initialized in the [init] method.
     */
    lateinit var groupByType: HorizontalBarChartGroupByType
    var isViewValues: Boolean = false
    var isUsePercentValues: Boolean = false

    constructor() : super()

    constructor(display: HorizontalBarChartDisplay) : super(display) {
        groupByType = display.groupByType
        isViewValues = display.isViewValues
        isUsePercentValues = display.isUsePercentValues
    }

    private constructor(`in`: Parcel) : super(`in`)

    override fun init() {
        super.init()
        groupByType = HorizontalBarChartGroupByType.ARTICLE
        isViewValues = true
        isUsePercentValues = false
    }

    override fun readFromParcel(`in`: Parcel) {
        groupByType = HorizontalBarChartGroupByType.values()[`in`.readInt()]
        isViewValues = `in`.readBoolean()
        isUsePercentValues = `in`.readBoolean()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeInt(groupByType.ordinal)
        out.writeBoolean(isViewValues)
        out.writeBoolean(isUsePercentValues)
    }

    override fun needRefreshData(newDisplay: HorizontalBarChartDisplay): Boolean {
        return groupByType !== newDisplay.groupByType
                || isUsePercentValues != newDisplay.isUsePercentValues
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HorizontalBarChartDisplay

        if (groupByType != other.groupByType) return false
        if (isViewValues != other.isViewValues) return false
        if (isUsePercentValues != other.isUsePercentValues) return false

        return true
    }

    override fun hashCode(): Int {
        var result = groupByType.hashCode()
        result = 31 * result + isViewValues.hashCode()
        result = 31 * result + isUsePercentValues.hashCode()
        return result
    }

    companion object {
        const val HORIZONTAL_BAR_CHART_DISPLAY = "horizontalBarChartDisplay"

        @JvmField
        val CREATOR: Parcelable.Creator<HorizontalBarChartDisplay> =
            object : Parcelable.Creator<HorizontalBarChartDisplay> {

                override fun createFromParcel(`in`: Parcel): HorizontalBarChartDisplay {
                    return HorizontalBarChartDisplay(`in`)
                }

                override fun newArray(size: Int): Array<HorizontalBarChartDisplay?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
