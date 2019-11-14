package io.github.zwieback.familyfinance.business.chart.fragment

import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.chart.dialog.PieChartDisplayDialog
import io.github.zwieback.familyfinance.business.operation.dialog.ExpenseOperationFilterDialog
import io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter
import io.github.zwieback.familyfinance.business.operation.query.ExpenseOperationQueryBuilder
import io.github.zwieback.familyfinance.core.model.OperationView
import io.requery.query.Result

class PieChartOfExpensesFragment : PieChartFragment<ExpenseOperationFilter>() {

    override val filterName: String
        get() = ExpenseOperationFilter.EXPENSE_OPERATION_FILTER

    override val dataSetLabel: Int
        get() = R.string.data_set_expenses

    override fun createDefaultFilter(): ExpenseOperationFilter {
        return ExpenseOperationFilter()
    }

    override fun buildOperations(): Result<OperationView> {
        return ExpenseOperationQueryBuilder.create(data)
            .withStartDate(filter.startDate)
            .withEndDate(filter.endDate)
            .withStartValue(filter.startValue)
            .withEndValue(filter.endValue)
            .withOwnerId(filter.getOwnerId())
            .withCurrencyId(filter.getCurrencyId())
            .withArticleId(filter.getArticleId())
            .withAccountId(filter.getAccountId())
            .build()
    }

    override fun showFilterDialog() {
        ExpenseOperationFilterDialog
            .newInstance(filter, R.string.pie_chart_of_expenses_filter_title)
            .show(childFragmentManager, "ExpenseOperationFilterDialog")
    }

    override fun showDisplayDialog() {
        PieChartDisplayDialog
            .newInstance(display, R.string.pie_chart_of_expenses_display_title)
            .show(childFragmentManager, "PieChartDisplayDialog")
    }
}
