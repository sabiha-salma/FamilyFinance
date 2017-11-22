package io.github.zwieback.familyfinance.business.operation.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.business.operation.dialog.TransferOperationFilterDialog;
import io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter;
import io.github.zwieback.familyfinance.business.operation.fragment.TransferOperationFragment;
import io.github.zwieback.familyfinance.business.operation.lifecycle.destroyer.TransferOperationForceDestroyer;
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer;
import io.github.zwieback.familyfinance.core.model.Operation;
import io.github.zwieback.familyfinance.core.model.OperationView;

import static io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter.TRANSFER_OPERATION_FILTER;

public class TransferOperationActivity
        extends OperationActivity<TransferOperationFragment, TransferOperationFilter> {

    @Override
    protected int getTitleStringId() {
        return R.string.transfer_operation_activity_title;
    }

    @NonNull
    @Override
    protected String getFilterName() {
        return TRANSFER_OPERATION_FILTER;
    }

    @NonNull
    @Override
    protected TransferOperationFilter createDefaultFilter() {
        TransferOperationFilter filter = new TransferOperationFilter();
        filter.setAccountId(databasePrefs.getAccountId());
        filter.setArticleId(databasePrefs.getTransferArticleId());
        filter.setCurrencyId(databasePrefs.getCurrencyId());
        filter.setOwnerId(databasePrefs.getPersonId());
        return filter;
    }

    @Override
    protected TransferOperationFragment createFragment() {
        return TransferOperationFragment.newInstance(filter);
    }

    @Override
    protected void addEntity() {
        super.addEntity();
        Intent intent = new Intent(this, TransferOperationEditActivity.class);
        intent.putExtra(TransferOperationEditActivity.INPUT_EXPENSE_ACCOUNT_ID,
                filter.getAccountId());
        startActivity(intent);
    }

    @Override
    protected void editEntity(OperationView operation) {
        super.editEntity(operation);
        Intent intent = new Intent(this, TransferOperationEditActivity.class);
        intent.putExtra(TransferOperationEditActivity.INPUT_TRANSFER_OPERATION_ID,
                TransferOperationQualifier.determineTransferExpenseOperationId(operation));
        startActivity(intent);
    }

    @Override
    protected EntityDestroyer<Operation> createDestroyer(OperationView entity) {
        return new TransferOperationForceDestroyer(this, data);
    }

    @Override
    protected void showFilterDialog() {
        DialogFragment dialog = TransferOperationFilterDialog.newInstance(filter);
        dialog.show(getSupportFragmentManager(), "TransferOperationFilterDialog");
    }
}
