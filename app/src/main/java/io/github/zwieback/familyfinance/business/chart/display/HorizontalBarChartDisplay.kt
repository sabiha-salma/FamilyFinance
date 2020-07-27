package io.github.zwieback.familyfinance.business.chart.display

import io.github.zwieback.familyfinance.business.chart.display.type.HorizontalBarChartGroupByType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HorizontalBarChartDisplay(
    var groupByType: HorizontalBarChartGroupByType = HorizontalBarChartGroupByType.ARTICLE,
    var isViewValues: Boolean = true,
    var isUsePercentValues: Boolean = false
) : ChartDisplay<HorizontalBarChartDisplay>() {

    override fun needRefreshData(newDisplay: HorizontalBarChartDisplay): Boolean {
        return groupByType !== newDisplay.groupByType
                || isUsePercentValues != newDisplay.isUsePercentValues
    }

    companion object {
        const val HORIZONTAL_BAR_CHART_DISPLAY = "horizontalBarChartDisplay"
    }
}
