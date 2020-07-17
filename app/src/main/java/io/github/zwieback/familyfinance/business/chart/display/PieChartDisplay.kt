package io.github.zwieback.familyfinance.business.chart.display

import android.os.Parcel
import android.os.Parcelable
import io.github.zwieback.familyfinance.business.chart.display.type.PieChartGroupByType
import io.github.zwieback.familyfinance.business.chart.display.type.PieChartGroupingType
import io.github.zwieback.familyfinance.util.BooleanUtils

class PieChartDisplay : ChartDisplay<PieChartDisplay> {

    /**
     * Initialized in the [init] method.
     */
    lateinit var groupingType: PieChartGroupingType

    /**
     * Initialized in the [init] method.
     */
    lateinit var groupByType: PieChartGroupByType
    var isViewValues: Boolean = false
    var isUsePercentValues: Boolean = false

    constructor() : super()

    constructor(display: PieChartDisplay) : super(display) {
        groupingType = display.groupingType
        groupByType = display.groupByType
        isViewValues = display.isViewValues
        isUsePercentValues = display.isUsePercentValues
    }

    private constructor(`in`: Parcel) : super(`in`)

    override fun init() {
        super.init()
        groupingType = PieChartGroupingType.LIMIT
        groupByType = PieChartGroupByType.ARTICLE_PARENT
        isViewValues = true
        isUsePercentValues = false
    }

    override fun readFromParcel(`in`: Parcel) {
        groupingType = PieChartGroupingType.values()[`in`.readInt()]
        groupByType = PieChartGroupByType.values()[`in`.readInt()]
        isViewValues = BooleanUtils.readBooleanFromParcel(`in`)
        isUsePercentValues = BooleanUtils.readBooleanFromParcel(`in`)
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeInt(groupingType.ordinal)
        out.writeInt(groupByType.ordinal)
        BooleanUtils.writeBooleanToParcel(out, isViewValues)
        BooleanUtils.writeBooleanToParcel(out, isUsePercentValues)
    }

    override fun needRefreshData(newDisplay: PieChartDisplay): Boolean {
        return groupingType !== newDisplay.groupingType
                || groupByType !== newDisplay.groupByType
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PieChartDisplay

        if (groupingType != other.groupingType) return false
        if (groupByType != other.groupByType) return false
        if (isViewValues != other.isViewValues) return false
        if (isUsePercentValues != other.isUsePercentValues) return false

        return true
    }

    override fun hashCode(): Int {
        var result = groupingType.hashCode()
        result = 31 * result + groupByType.hashCode()
        result = 31 * result + isViewValues.hashCode()
        result = 31 * result + isUsePercentValues.hashCode()
        return result
    }

    companion object {
        const val PIE_CHART_DISPLAY = "pieChartDisplay"

        @JvmField
        val CREATOR: Parcelable.Creator<PieChartDisplay> =
            object : Parcelable.Creator<PieChartDisplay> {

                override fun createFromParcel(`in`: Parcel): PieChartDisplay {
                    return PieChartDisplay(`in`)
                }

                override fun newArray(size: Int): Array<PieChartDisplay?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
