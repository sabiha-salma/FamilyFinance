package io.github.zwieback.familyfinance.business.operation.activity.helper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import io.github.zwieback.familyfinance.business.operation.activity.TransferOperationEditActivity;
import io.github.zwieback.familyfinance.business.operation.lifecycle.destroyer.TransferOperationForceDestroyer;
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer;
import io.github.zwieback.familyfinance.core.model.Operation;
import io.github.zwieback.familyfinance.core.model.OperationView;
import io.github.zwieback.familyfinance.util.DateUtils;
import io.github.zwieback.familyfinance.util.NumberUtils;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class TransferOperationHelper extends OperationHelper {

    public TransferOperationHelper(Context context, ReactiveEntityStore<Persistable> data) {
        super(context, data);
    }

    @Override
    public Intent getIntentToAdd(@Nullable Integer accountId) {
        Intent intent = new Intent(context, TransferOperationEditActivity.class);
        intent.putExtra(TransferOperationEditActivity.INPUT_EXPENSE_ACCOUNT_ID, accountId);
        return intent;
    }

    @Override
    public Intent getIntentToEdit(OperationView operation) {
        Intent intent = new Intent(context, TransferOperationEditActivity.class);
        intent.putExtra(TransferOperationEditActivity.INPUT_TRANSFER_OPERATION_ID,
                TransferOperationQualifier.determineTransferExpenseOperationId(operation));
        return intent;
    }

    @Override
    public Intent getIntentToDuplicate(OperationView operation) {
        OperationView expenseOperation = TransferOperationFinder.findExpenseOperation(data,
                operation);
        OperationView incomeOperation = TransferOperationFinder.findIncomeOperation(data,
                operation);
        Intent intent = new Intent(context, TransferOperationEditActivity.class);
        intent.putExtra(TransferOperationEditActivity.INPUT_EXPENSE_ACCOUNT_ID,
                expenseOperation.getAccountId());
        intent.putExtra(TransferOperationEditActivity.INPUT_INCOME_ACCOUNT_ID,
                incomeOperation.getAccountId());
        intent.putExtra(TransferOperationEditActivity.INPUT_EXPENSE_OWNER_ID,
                expenseOperation.getOwnerId());
        intent.putExtra(TransferOperationEditActivity.INPUT_EXPENSE_EXCHANGE_RATE_ID,
                expenseOperation.getExchangeRateId());
        intent.putExtra(TransferOperationEditActivity.INPUT_EXPENSE_DESCRIPTION,
                expenseOperation.getDescription());
        NumberUtils.writeBigDecimalToIntent(intent,
                TransferOperationEditActivity.INPUT_EXPENSE_VALUE, expenseOperation.getValue());
        DateUtils.writeLocalDateToIntent(intent,
                TransferOperationEditActivity.INPUT_EXPENSE_DATE, expenseOperation.getDate());
        return intent;
    }

    @Override
    public EntityDestroyer<Operation> createDestroyer(OperationView operation) {
        return new TransferOperationForceDestroyer(context, data);
    }
}
