package io.github.zwieback.familyfinance.business.chart.fragment

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.chart.dialog.HorizontalBarChartDisplayDialog
import io.github.zwieback.familyfinance.business.operation.dialog.IncomeOperationFilterDialog
import io.github.zwieback.familyfinance.business.operation.filter.IncomeOperationFilter
import io.github.zwieback.familyfinance.business.operation.query.IncomeOperationQueryBuilder
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.extension.operation.filter.applyPreferences
import io.requery.query.Result

class HorizontalBarChartOfIncomesFragment : HorizontalBarChartFragment<IncomeOperationFilter>() {

    override val filterName: String
        get() = IncomeOperationFilter.INCOME_OPERATION_FILTER

    override val dataSetLabel: Int
        @StringRes
        get() = R.string.data_set_incomes

    override val dataSetColor: Int
        @ColorRes
        get() = R.color.colorIncome

    override fun createDefaultFilter() = IncomeOperationFilter().apply {
        applyPreferences(this@HorizontalBarChartOfIncomesFragment.requireContext())
    }

    override fun buildOperations(): Result<OperationView> {
        return IncomeOperationQueryBuilder.create(data)
            .withStartDate(filter.startDate)
            .withEndDate(filter.endDate)
            .withStartValue(filter.startValue)
            .withEndValue(filter.endValue)
            .withOwnerId(filter.takeOwnerId())
            .withToWhomId(filter.takeToWhomId())
            .withCurrencyId(filter.takeCurrencyId())
            .withArticleId(filter.takeArticleId())
            .withAccountId(filter.takeAccountId())
            .build()
    }

    override fun onApplyFilter(filter: IncomeOperationFilter) {
        this.filter = filter
        this.operationConverter = determineOperationConverter()
        refreshData()
    }

    override fun showFilterDialog() {
        IncomeOperationFilterDialog
            .newInstance(filter, R.string.horizontal_bar_chart_of_incomes_filter_title)
            .show(childFragmentManager, "IncomeOperationFilterDialog")
    }

    override fun showDisplayDialog() {
        HorizontalBarChartDisplayDialog
            .newInstance(display, R.string.horizontal_bar_chart_of_incomes_display_title)
            .show(childFragmentManager, "HorizontalBarChartDisplayDialog")
    }
}
