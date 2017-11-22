package io.github.zwieback.familyfinance.business.operation.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.business.operation.dialog.IncomeOperationFilterDialog;
import io.github.zwieback.familyfinance.business.operation.filter.IncomeOperationFilter;
import io.github.zwieback.familyfinance.business.operation.fragment.IncomeOperationFragment;
import io.github.zwieback.familyfinance.core.model.OperationView;

import static io.github.zwieback.familyfinance.business.operation.filter.IncomeOperationFilter.INCOME_OPERATION_FILTER;

public class IncomeOperationActivity
        extends OperationActivity<IncomeOperationFragment, IncomeOperationFilter> {

    @Override
    protected int getTitleStringId() {
        return R.string.income_operation_activity_title;
    }

    @NonNull
    @Override
    protected String getFilterName() {
        return INCOME_OPERATION_FILTER;
    }

    @NonNull
    @Override
    protected IncomeOperationFilter createDefaultFilter() {
        IncomeOperationFilter filter = new IncomeOperationFilter();
        filter.setAccountId(databasePrefs.getAccountId());
        filter.setArticleId(databasePrefs.getIncomesArticleId());
        filter.setCurrencyId(databasePrefs.getCurrencyId());
        filter.setOwnerId(databasePrefs.getPersonId());
        return filter;
    }

    @Override
    protected IncomeOperationFragment createFragment() {
        return IncomeOperationFragment.newInstance(filter);
    }

    @Override
    protected void addEntity() {
        super.addEntity();
        Intent intent = new Intent(this, IncomeOperationEditActivity.class);
        intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_ACCOUNT_ID, filter.getAccountId());
        startActivity(intent);
    }

    @Override
    protected void editEntity(OperationView operation) {
        super.editEntity(operation);
        Intent intent = new Intent(this, IncomeOperationEditActivity.class);
        intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_OPERATION_ID, operation.getId());
        startActivity(intent);
    }

    @Override
    protected void showFilterDialog() {
        DialogFragment dialog = IncomeOperationFilterDialog.newInstance(filter);
        dialog.show(getSupportFragmentManager(), "IncomeOperationFilterDialog");
    }
}
