package io.github.zwieback.familyfinance.business.operation.activity.helper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import io.github.zwieback.familyfinance.business.operation.activity.ExpenseOperationEditActivity;
import io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter;
import io.github.zwieback.familyfinance.core.model.OperationView;
import io.github.zwieback.familyfinance.util.DateUtils;
import io.github.zwieback.familyfinance.util.NumberUtils;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class ExpenseOperationHelper extends OperationHelper<ExpenseOperationFilter> {

    public ExpenseOperationHelper(Context context, ReactiveEntityStore<Persistable> data) {
        super(context, data);
    }

    @Override
    public Intent getIntentToAdd() {
        return new Intent(context, ExpenseOperationEditActivity.class);
    }

    @Override
    public Intent getIntentToAdd(@Nullable ExpenseOperationFilter filter) {
        Intent intent = new Intent(context, ExpenseOperationEditActivity.class);
        if (filter == null) {
            return intent;
        }
        if (filter.getAccountId() != null) {
            intent.putExtra(ExpenseOperationEditActivity.INPUT_EXPENSE_ACCOUNT_ID,
                    filter.getAccountId());
        }
        if (filter.getArticleId() != null
                && filter.getArticleId() != databasePrefs.getExpensesArticleId()) {
            intent.putExtra(ExpenseOperationEditActivity.INPUT_EXPENSE_ARTICLE_ID,
                    filter.getArticleId());
        }
        if (filter.getOwnerId() != null) {
            intent.putExtra(ExpenseOperationEditActivity.INPUT_EXPENSE_OWNER_ID,
                    filter.getOwnerId());
        }
        if (filter.getCurrencyId() != null) {
            intent.putExtra(ExpenseOperationEditActivity.INPUT_EXPENSE_CURRENCY_ID,
                    filter.getCurrencyId());
        }
        return intent;
    }

    @Override
    public Intent getIntentToEdit(OperationView operation) {
        Intent intent = new Intent(context, ExpenseOperationEditActivity.class);
        intent.putExtra(ExpenseOperationEditActivity.INPUT_EXPENSE_OPERATION_ID, operation.getId());
        return intent;
    }

    @Override
    public Intent getIntentToDuplicate(OperationView operation) {
        Intent intent = new Intent(context, ExpenseOperationEditActivity.class);
        intent.putExtra(ExpenseOperationEditActivity.INPUT_EXPENSE_ACCOUNT_ID,
                operation.getAccountId());
        intent.putExtra(ExpenseOperationEditActivity.INPUT_EXPENSE_ARTICLE_ID,
                operation.getArticleId());
        intent.putExtra(ExpenseOperationEditActivity.INPUT_EXPENSE_OWNER_ID,
                operation.getOwnerId());
        intent.putExtra(ExpenseOperationEditActivity.INPUT_EXPENSE_EXCHANGE_RATE_ID,
                operation.getExchangeRateId());
        intent.putExtra(ExpenseOperationEditActivity.INPUT_EXPENSE_DESCRIPTION,
                operation.getDescription());
        NumberUtils.writeBigDecimalToIntent(intent,
                ExpenseOperationEditActivity.INPUT_EXPENSE_VALUE, operation.getValue());
        DateUtils.writeLocalDateToIntent(intent,
                ExpenseOperationEditActivity.INPUT_EXPENSE_DATE, operation.getDate());
        return intent;
    }
}
