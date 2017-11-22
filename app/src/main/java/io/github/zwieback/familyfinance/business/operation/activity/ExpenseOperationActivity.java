package io.github.zwieback.familyfinance.business.operation.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.business.operation.dialog.ExpenseOperationFilterDialog;
import io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter;
import io.github.zwieback.familyfinance.business.operation.fragment.ExpenseOperationFragment;
import io.github.zwieback.familyfinance.core.model.OperationView;

import static io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter.EXPENSE_OPERATION_FILTER;

public class ExpenseOperationActivity
        extends OperationActivity<ExpenseOperationFragment, ExpenseOperationFilter> {

    @Override
    protected int getTitleStringId() {
        return R.string.expense_operation_activity_title;
    }

    @NonNull
    @Override
    protected String getFilterName() {
        return EXPENSE_OPERATION_FILTER;
    }

    @NonNull
    @Override
    protected ExpenseOperationFilter createDefaultFilter() {
        ExpenseOperationFilter filter = new ExpenseOperationFilter();
        filter.setAccountId(databasePrefs.getAccountId());
        filter.setArticleId(databasePrefs.getExpensesArticleId());
        filter.setCurrencyId(databasePrefs.getCurrencyId());
        filter.setOwnerId(databasePrefs.getPersonId());
        return filter;
    }

    @Override
    protected ExpenseOperationFragment createFragment() {
        return ExpenseOperationFragment.newInstance(filter);
    }

    @Override
    protected void addEntity() {
        super.addEntity();
        Intent intent = new Intent(this, ExpenseOperationEditActivity.class);
        intent.putExtra(ExpenseOperationEditActivity.INPUT_EXPENSE_ACCOUNT_ID,
                filter.getAccountId());
        startActivity(intent);
    }

    @Override
    protected void editEntity(OperationView operation) {
        super.editEntity(operation);
        Intent intent = new Intent(this, ExpenseOperationEditActivity.class);
        intent.putExtra(ExpenseOperationEditActivity.INPUT_EXPENSE_OPERATION_ID, operation.getId());
        startActivity(intent);
    }

    @Override
    protected void showFilterDialog() {
        DialogFragment dialog = ExpenseOperationFilterDialog.newInstance(filter);
        dialog.show(getSupportFragmentManager(), "ExpenseOperationFilterDialog");
    }
}
