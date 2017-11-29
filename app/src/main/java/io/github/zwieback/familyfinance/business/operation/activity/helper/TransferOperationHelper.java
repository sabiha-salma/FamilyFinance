package io.github.zwieback.familyfinance.business.operation.activity.helper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

import io.github.zwieback.familyfinance.business.operation.activity.TransferOperationEditActivity;
import io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter;
import io.github.zwieback.familyfinance.business.operation.lifecycle.destroyer.TransferOperationForceDestroyer;
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer;
import io.github.zwieback.familyfinance.core.model.Operation;
import io.github.zwieback.familyfinance.core.model.OperationView;
import io.github.zwieback.familyfinance.util.DateUtils;
import io.github.zwieback.familyfinance.util.NumberUtils;
import io.github.zwieback.familyfinance.util.StringUtils;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class TransferOperationHelper extends OperationHelper<TransferOperationFilter> {

    public TransferOperationHelper(Context context, ReactiveEntityStore<Persistable> data) {
        super(context, data);
    }

    @Override
    public Intent getIntentToAdd() {
        return getEmptyIntent();
    }

    @Override
    public Intent getIntentToAdd(@Nullable Integer ignoredArticleId,
                                 @Nullable Integer accountId,
                                 @Nullable Integer transferAccountId,
                                 @Nullable Integer ownerId,
                                 @Nullable Integer currencyId,
                                 @Nullable Integer exchangeRateId,
                                 @Nullable LocalDate date,
                                 @Nullable BigDecimal value,
                                 @Nullable String description) {
        Intent intent = getEmptyIntent();
        if (accountId != null) {
            intent.putExtra(TransferOperationEditActivity.INPUT_EXPENSE_ACCOUNT_ID, accountId);
        }
        if (transferAccountId != null) {
            intent.putExtra(TransferOperationEditActivity.INPUT_INCOME_ACCOUNT_ID,
                    transferAccountId);
        }
        if (ownerId != null) {
            intent.putExtra(TransferOperationEditActivity.INPUT_EXPENSE_OWNER_ID, ownerId);
        }
        if (currencyId != null) {
            intent.putExtra(TransferOperationEditActivity.INPUT_EXPENSE_CURRENCY_ID, currencyId);
        }
        if (exchangeRateId != null) {
            intent.putExtra(TransferOperationEditActivity.INPUT_EXPENSE_EXCHANGE_RATE_ID,
                    exchangeRateId);
        }
        if (date != null) {
            DateUtils.writeLocalDateToIntent(intent,
                    TransferOperationEditActivity.INPUT_EXPENSE_DATE, date);
        }
        if (value != null) {
            NumberUtils.writeBigDecimalToIntent(intent,
                    TransferOperationEditActivity.INPUT_EXPENSE_VALUE, value);
        }
        if (StringUtils.isTextNotEmpty(description)) {
            intent.putExtra(TransferOperationEditActivity.INPUT_EXPENSE_DESCRIPTION, description);
        }
        return intent;
    }

    @Override
    public Intent getIntentToAdd(@Nullable TransferOperationFilter filter) {
        if (filter == null) {
            return getEmptyIntent();
        }
        return getIntentToAdd(null, filter.getAccountId(), null,
                filter.getOwnerId(), filter.getCurrencyId(), null, null, null, null);
    }

    @Override
    public Intent getIntentToEdit(OperationView operation) {
        Intent intent = getEmptyIntent();
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
        return getIntentToAdd(null, expenseOperation.getAccountId(), incomeOperation.getAccountId(),
                expenseOperation.getOwnerId(), expenseOperation.getCurrencyId(),
                expenseOperation.getExchangeRateId(), expenseOperation.getDate(),
                expenseOperation.getValue(), expenseOperation.getDescription());
    }

    @Override
    Intent getEmptyIntent() {
        return new Intent(context, TransferOperationEditActivity.class);
    }

    @Override
    public EntityDestroyer<Operation> createDestroyer(OperationView operation) {
        return new TransferOperationForceDestroyer(context, data);
    }
}
