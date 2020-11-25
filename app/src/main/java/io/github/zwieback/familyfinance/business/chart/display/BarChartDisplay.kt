package io.github.zwieback.familyfinance.business.chart.display

import io.github.zwieback.familyfinance.business.chart.display.type.BarChartGroupType
import kotlinx.parcelize.Parcelize

@Parcelize
data class BarChartDisplay(
    var groupType: BarChartGroupType = BarChartGroupType.DAYS,
    var isViewIncomeValues: Boolean = true,
    var isViewExpenseValues: Boolean = true,
    var isViewProfitValues: Boolean = false,
    var isViewIncomes: Boolean = true,
    var isViewExpenses: Boolean = true,
    var isViewProfits: Boolean = false,
    var isIncludeTransfers: Boolean = false
) : ChartDisplay<BarChartDisplay>() {

    override fun needRefreshData(newDisplay: BarChartDisplay): Boolean {
        return groupType !== newDisplay.groupType
                || isViewIncomes != newDisplay.isViewIncomes
                || isViewExpenses != newDisplay.isViewExpenses
                || isViewProfits != newDisplay.isViewProfits
                || isIncludeTransfers != newDisplay.isIncludeTransfers
    }

    companion object {
        const val BAR_CHART_DISPLAY = "barChartDisplay"
    }
}
