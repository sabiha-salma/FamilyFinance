package io.github.zwieback.familyfinance.business.chart.fragment

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.chart.dialog.HorizontalBarChartDisplayDialog
import io.github.zwieback.familyfinance.business.operation.dialog.ExpenseOperationFilterDialog
import io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter
import io.github.zwieback.familyfinance.business.operation.query.ExpenseOperationQueryBuilder
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.extension.operation.filter.applyPreferences
import io.requery.query.Result

class HorizontalBarChartOfExpensesFragment : HorizontalBarChartFragment<ExpenseOperationFilter>() {

    override val filterName: String
        get() = ExpenseOperationFilter.EXPENSE_OPERATION_FILTER

    override val dataSetLabel: Int
        @StringRes
        get() = R.string.data_set_expenses

    override val dataSetColor: Int
        @ColorRes
        get() = R.color.colorExpense

    override fun createDefaultFilter() = ExpenseOperationFilter().apply {
        applyPreferences(this@HorizontalBarChartOfExpensesFragment.requireContext())
    }

    override fun buildOperations(): Result<OperationView> {
        return ExpenseOperationQueryBuilder.create(data)
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

    override fun onApplyFilter(filter: ExpenseOperationFilter) {
        this.filter = filter
        this.operationConverter = determineOperationConverter()
        refreshData()
    }

    override fun showFilterDialog() {
        ExpenseOperationFilterDialog
            .newInstance(filter, R.string.horizontal_bar_chart_of_expenses_filter_title)
            .show(childFragmentManager, "ExpenseOperationFilterDialog")
    }

    override fun showDisplayDialog() {
        HorizontalBarChartDisplayDialog
            .newInstance(display, R.string.horizontal_bar_chart_of_expenses_display_title)
            .show(childFragmentManager, "HorizontalBarChartDisplayDialog")
    }
}
