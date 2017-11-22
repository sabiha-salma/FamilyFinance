package io.github.zwieback.familyfinance.business.operation.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.MenuItem;

import java.util.Collections;
import java.util.List;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.business.operation.activity.exception.IllegalOperationTypeException;
import io.github.zwieback.familyfinance.business.operation.dialog.FlowOfFundsOperationFilterDialog;
import io.github.zwieback.familyfinance.business.operation.filter.FlowOfFundsOperationFilter;
import io.github.zwieback.familyfinance.business.operation.fragment.FlowOfFundsOperationFragment;
import io.github.zwieback.familyfinance.business.operation.lifecycle.destroyer.OperationForceDestroyer;
import io.github.zwieback.familyfinance.business.operation.lifecycle.destroyer.TransferOperationForceDestroyer;
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer;
import io.github.zwieback.familyfinance.core.model.Operation;
import io.github.zwieback.familyfinance.core.model.OperationView;

import static io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.EXPENSE_OPERATION_EDIT_CODE;
import static io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.INCOME_OPERATION_EDIT_CODE;
import static io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.TRANSFER_OPERATION_EDIT_CODE;
import static io.github.zwieback.familyfinance.business.operation.filter.FlowOfFundsOperationFilter.FLOW_OF_FUNDS_OPERATION_FILTER;

public class FlowOfFundsOperationActivity
        extends OperationActivity<FlowOfFundsOperationFragment, FlowOfFundsOperationFilter> {

    @Override
    protected List<Integer> collectMenuIds() {
        if (!readOnly) {
            return Collections.singletonList(R.menu.menu_entity_flow_of_funds_operation);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_expense:
                addExpenseOperation();
                return true;
            case R.id.action_add_income:
                addIncomeOperation();
                return true;
            case R.id.action_add_transfer:
                addTransferOperation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getTitleStringId() {
        return R.string.flow_of_funds_activity_title;
    }

    @NonNull
    @Override
    protected String getFilterName() {
        return FLOW_OF_FUNDS_OPERATION_FILTER;
    }

    @NonNull
    @Override
    protected FlowOfFundsOperationFilter createDefaultFilter() {
        return new FlowOfFundsOperationFilter();
    }

    @Override
    protected FlowOfFundsOperationFragment createFragment() {
        return FlowOfFundsOperationFragment.newInstance(filter);
    }

    private void addExpenseOperation() {
        super.addEntity();
        Intent intent = new Intent(this, ExpenseOperationEditActivity.class);
        startActivityForResult(intent, EXPENSE_OPERATION_EDIT_CODE);
    }

    private void addIncomeOperation() {
        super.addEntity();
        Intent intent = new Intent(this, IncomeOperationEditActivity.class);
        startActivityForResult(intent, INCOME_OPERATION_EDIT_CODE);
    }

    private void addTransferOperation() {
        super.addEntity();
        Intent intent = new Intent(this, TransferOperationEditActivity.class);
        startActivityForResult(intent, TRANSFER_OPERATION_EDIT_CODE);
    }

    @Override
    protected void editEntity(OperationView operation) {
        super.editEntity(operation);
        switch (operation.getType()) {
            case EXPENSE_OPERATION:
                editExpenseOperation(operation);
                break;
            case INCOME_OPERATION:
                editIncomeOperation(operation);
                break;
            case TRANSFER_EXPENSE_OPERATION:
            case TRANSFER_INCOME_OPERATION:
                editTransferOperation(operation);
                break;
        }
    }

    private void editExpenseOperation(OperationView operation) {
        Intent intent = new Intent(this, ExpenseOperationEditActivity.class);
        intent.putExtra(ExpenseOperationEditActivity.INPUT_EXPENSE_OPERATION_ID, operation.getId());
        startActivityForResult(intent, EXPENSE_OPERATION_EDIT_CODE);
    }

    private void editIncomeOperation(OperationView operation) {
        Intent intent = new Intent(this, IncomeOperationEditActivity.class);
        intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_OPERATION_ID, operation.getId());
        startActivityForResult(intent, INCOME_OPERATION_EDIT_CODE);
    }

    private void editTransferOperation(OperationView operation) {
        Intent intent = new Intent(this, TransferOperationEditActivity.class);
        intent.putExtra(TransferOperationEditActivity.INPUT_TRANSFER_OPERATION_ID,
                TransferOperationQualifier.determineTransferExpenseOperationId(operation));
        startActivityForResult(intent, TRANSFER_OPERATION_EDIT_CODE);
    }

    @Override
    protected EntityDestroyer<Operation> createDestroyer(OperationView operation) {
        switch (operation.getType()) {
            case EXPENSE_OPERATION:
            case INCOME_OPERATION:
                return new OperationForceDestroyer(this, data);
            case TRANSFER_EXPENSE_OPERATION:
            case TRANSFER_INCOME_OPERATION:
                return new TransferOperationForceDestroyer(this, data);
            default:
                throw IllegalOperationTypeException.unsupportedOperationType(operation);
        }
    }

    @Override
    protected void showFilterDialog() {
        DialogFragment dialog = FlowOfFundsOperationFilterDialog.newInstance(filter);
        dialog.show(getSupportFragmentManager(), "FlowOfFundsOperationFilterDialog");
    }
}
