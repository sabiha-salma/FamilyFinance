package io.github.zwieback.familyfinance.business.operation.activity.helper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import io.github.zwieback.familyfinance.business.operation.activity.IncomeOperationEditActivity;
import io.github.zwieback.familyfinance.business.operation.filter.IncomeOperationFilter;
import io.github.zwieback.familyfinance.core.model.OperationView;
import io.github.zwieback.familyfinance.util.DateUtils;
import io.github.zwieback.familyfinance.util.NumberUtils;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class IncomeOperationHelper extends OperationHelper<IncomeOperationFilter> {

    public IncomeOperationHelper(Context context, ReactiveEntityStore<Persistable> data) {
        super(context, data);
    }

    @Override
    public Intent getIntentToAdd() {
        return new Intent(context, IncomeOperationEditActivity.class);
    }

    @Override
    public Intent getIntentToAdd(@Nullable IncomeOperationFilter filter) {
        Intent intent = new Intent(context, IncomeOperationEditActivity.class);
        if (filter == null) {
            return intent;
        }
        if (filter.getAccountId() != null) {
            intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_ACCOUNT_ID,
                    filter.getAccountId());
        }
        if (filter.getArticleId() != null
                && filter.getArticleId() != databasePrefs.getIncomesArticleId()) {
            intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_ARTICLE_ID,
                    filter.getArticleId());
        }
        if (filter.getOwnerId() != null) {
            intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_OWNER_ID,
                    filter.getOwnerId());
        }
        if (filter.getCurrencyId() != null) {
            intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_CURRENCY_ID,
                    filter.getCurrencyId());
        }
        return intent;
    }

    @Override
    public Intent getIntentToEdit(OperationView operation) {
        Intent intent = new Intent(context, IncomeOperationEditActivity.class);
        intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_OPERATION_ID, operation.getId());
        return intent;
    }

    @Override
    public Intent getIntentToDuplicate(OperationView operation) {
        Intent intent = new Intent(context, IncomeOperationEditActivity.class);
        intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_ACCOUNT_ID,
                operation.getAccountId());
        intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_ARTICLE_ID,
                operation.getArticleId());
        intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_OWNER_ID,
                operation.getOwnerId());
        intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_EXCHANGE_RATE_ID,
                operation.getExchangeRateId());
        intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_DESCRIPTION,
                operation.getDescription());
        NumberUtils.writeBigDecimalToIntent(intent,
                IncomeOperationEditActivity.INPUT_INCOME_VALUE, operation.getValue());
        DateUtils.writeLocalDateToIntent(intent,
                IncomeOperationEditActivity.INPUT_INCOME_DATE, operation.getDate());
        return intent;
    }
}
