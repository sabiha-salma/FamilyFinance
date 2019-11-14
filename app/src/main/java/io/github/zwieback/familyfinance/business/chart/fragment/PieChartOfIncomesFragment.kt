package io.github.zwieback.familyfinance.business.chart.fragment

import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.chart.dialog.PieChartDisplayDialog
import io.github.zwieback.familyfinance.business.operation.dialog.IncomeOperationFilterDialog
import io.github.zwieback.familyfinance.business.operation.filter.IncomeOperationFilter
import io.github.zwieback.familyfinance.business.operation.query.IncomeOperationQueryBuilder
import io.github.zwieback.familyfinance.core.model.OperationView
import io.requery.query.Result

class PieChartOfIncomesFragment : PieChartFragment<IncomeOperationFilter>() {

    override val filterName: String
        get() = IncomeOperationFilter.INCOME_OPERATION_FILTER

    override val dataSetLabel: Int
        get() = R.string.data_set_incomes

    override fun createDefaultFilter(): IncomeOperationFilter {
        return IncomeOperationFilter()
    }

    override fun buildOperations(): Result<OperationView> {
        return IncomeOperationQueryBuilder.create(data)
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
        IncomeOperationFilterDialog
            .newInstance(filter, R.string.pie_chart_of_incomes_filter_title)
            .show(childFragmentManager, "IncomeOperationFilterDialog")
    }

    override fun showDisplayDialog() {
        PieChartDisplayDialog
            .newInstance(display, R.string.pie_chart_of_incomes_display_title)
            .show(childFragmentManager, "PieChartDisplayDialog")
    }
}
