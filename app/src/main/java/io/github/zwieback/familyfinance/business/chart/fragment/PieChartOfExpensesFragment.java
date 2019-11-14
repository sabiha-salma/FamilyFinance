package io.github.zwieback.familyfinance.business.chart.fragment;

import androidx.fragment.app.DialogFragment;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.business.chart.dialog.PieChartDisplayDialog;
import io.github.zwieback.familyfinance.business.operation.dialog.ExpenseOperationFilterDialog;
import io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter;
import io.github.zwieback.familyfinance.business.operation.query.ExpenseOperationQueryBuilder;
import io.github.zwieback.familyfinance.core.model.OperationView;
import io.requery.query.Result;

public class PieChartOfExpensesFragment extends PieChartFragment<ExpenseOperationFilter> {

    @Override
    protected String getFilterName() {
        return ExpenseOperationFilter.EXPENSE_OPERATION_FILTER;
    }

    @Override
    protected ExpenseOperationFilter createDefaultFilter() {
        return new ExpenseOperationFilter();
    }

    @Override
    protected Result<OperationView> buildOperations() {
        return ExpenseOperationQueryBuilder.Companion.create(data)
                .withStartDate(filter.getStartDate())
                .withEndDate(filter.getEndDate())
                .withStartValue(filter.getStartValue())
                .withEndValue(filter.getEndValue())
                .withOwnerId(filter.getOwnerId())
                .withCurrencyId(filter.getCurrencyId())
                .withArticleId(filter.getArticleId())
                .withAccountId(filter.getAccountId())
                .build();
    }

    @Override
    protected int getDataSetLabel() {
        return R.string.data_set_expenses;
    }

    @Override
    public void showFilterDialog() {
        DialogFragment dialog = ExpenseOperationFilterDialog.Companion.newInstance(filter,
                R.string.pie_chart_of_expenses_filter_title);
        dialog.show(getChildFragmentManager(), "ExpenseOperationFilterDialog");
    }

    @Override
    public void showDisplayDialog() {
        DialogFragment dialog = PieChartDisplayDialog.newInstance(display,
                R.string.pie_chart_of_expenses_display_title);
        dialog.show(getChildFragmentManager(), "PieChartDisplayDialog");
    }
}
