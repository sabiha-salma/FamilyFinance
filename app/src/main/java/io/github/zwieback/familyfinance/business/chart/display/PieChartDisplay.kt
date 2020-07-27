package io.github.zwieback.familyfinance.business.chart.display

import io.github.zwieback.familyfinance.business.chart.display.type.PieChartGroupByType
import io.github.zwieback.familyfinance.business.chart.display.type.PieChartGroupingType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PieChartDisplay(
    var groupingType: PieChartGroupingType = PieChartGroupingType.LIMIT,
    var groupByType: PieChartGroupByType = PieChartGroupByType.ARTICLE_PARENT,
    var isViewValues: Boolean = true,
    var isUsePercentValues: Boolean = false
) : ChartDisplay<PieChartDisplay>() {

    override fun needRefreshData(newDisplay: PieChartDisplay): Boolean {
        return groupingType !== newDisplay.groupingType
                || groupByType !== newDisplay.groupByType
    }

    companion object {
        const val PIE_CHART_DISPLAY = "pieChartDisplay"
    }
}
