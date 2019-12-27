package io.github.zwieback.familyfinance.business.chart.display

import android.os.Parcel
import android.os.Parcelable
import io.github.zwieback.familyfinance.business.chart.display.type.BarChartGroupType
import io.github.zwieback.familyfinance.util.BooleanUtils

class BarChartDisplay : ChartDisplay<BarChartDisplay> {

    /**
     * Initialized in the [init] method.
     */
    lateinit var groupType: BarChartGroupType
    var isViewIncomeValues: Boolean = false
    var isViewExpenseValues: Boolean = false
    var isViewProfitValues: Boolean = false
    var isViewIncomes: Boolean = false
    var isViewExpenses: Boolean = false
    var isViewProfits: Boolean = false
    var isIncludeTransfers: Boolean = false

    constructor() : super()

    constructor(display: BarChartDisplay) : super(display) {
        groupType = display.groupType
        isViewIncomeValues = display.isViewIncomeValues
        isViewExpenseValues = display.isViewExpenseValues
        isViewProfitValues = display.isViewProfitValues
        isViewIncomes = display.isViewIncomes
        isViewExpenses = display.isViewExpenses
        isViewProfits = display.isViewProfits
        isIncludeTransfers = display.isIncludeTransfers
    }

    private constructor(`in`: Parcel) : super(`in`)

    override fun init() {
        super.init()
        groupType = BarChartGroupType.DAYS
        isViewIncomeValues = true
        isViewExpenseValues = true
        isViewProfitValues = false
        isViewIncomes = true
        isViewExpenses = true
        isViewProfits = false
        isIncludeTransfers = false
    }

    override fun readFromParcel(`in`: Parcel) {
        groupType = BarChartGroupType.values()[`in`.readInt()]
        isViewIncomeValues = BooleanUtils.readBooleanFromParcel(`in`)
        isViewExpenseValues = BooleanUtils.readBooleanFromParcel(`in`)
        isViewProfitValues = BooleanUtils.readBooleanFromParcel(`in`)
        isViewIncomes = BooleanUtils.readBooleanFromParcel(`in`)
        isViewExpenses = BooleanUtils.readBooleanFromParcel(`in`)
        isViewProfits = BooleanUtils.readBooleanFromParcel(`in`)
        isIncludeTransfers = BooleanUtils.readBooleanFromParcel(`in`)
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeInt(groupType.ordinal)
        BooleanUtils.writeBooleanToParcel(out, isViewIncomeValues)
        BooleanUtils.writeBooleanToParcel(out, isViewExpenseValues)
        BooleanUtils.writeBooleanToParcel(out, isViewProfitValues)
        BooleanUtils.writeBooleanToParcel(out, isViewIncomes)
        BooleanUtils.writeBooleanToParcel(out, isViewExpenses)
        BooleanUtils.writeBooleanToParcel(out, isViewProfits)
        BooleanUtils.writeBooleanToParcel(out, isIncludeTransfers)
    }

    override fun needRefreshData(newDisplay: BarChartDisplay): Boolean {
        return groupType !== newDisplay.groupType
                || isViewIncomes != newDisplay.isViewIncomes
                || isViewExpenses != newDisplay.isViewExpenses
                || isViewProfits != newDisplay.isViewProfits
                || isIncludeTransfers != newDisplay.isIncludeTransfers
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BarChartDisplay

        if (groupType != other.groupType) return false
        if (isViewIncomeValues != other.isViewIncomeValues) return false
        if (isViewExpenseValues != other.isViewExpenseValues) return false
        if (isViewProfitValues != other.isViewProfitValues) return false
        if (isViewIncomes != other.isViewIncomes) return false
        if (isViewExpenses != other.isViewExpenses) return false
        if (isViewProfits != other.isViewProfits) return false
        if (isIncludeTransfers != other.isIncludeTransfers) return false

        return true
    }

    override fun hashCode(): Int {
        var result = groupType.hashCode()
        result = 31 * result + isViewIncomeValues.hashCode()
        result = 31 * result + isViewExpenseValues.hashCode()
        result = 31 * result + isViewProfitValues.hashCode()
        result = 31 * result + isViewIncomes.hashCode()
        result = 31 * result + isViewExpenses.hashCode()
        result = 31 * result + isViewProfits.hashCode()
        result = 31 * result + isIncludeTransfers.hashCode()
        return result
    }

    companion object {
        const val BAR_CHART_DISPLAY = "barChartDisplay"

        @JvmField
        val CREATOR: Parcelable.Creator<BarChartDisplay> =
            object : Parcelable.Creator<BarChartDisplay> {

                override fun createFromParcel(`in`: Parcel): BarChartDisplay {
                    return BarChartDisplay(`in`)
                }

                override fun newArray(size: Int): Array<BarChartDisplay?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
