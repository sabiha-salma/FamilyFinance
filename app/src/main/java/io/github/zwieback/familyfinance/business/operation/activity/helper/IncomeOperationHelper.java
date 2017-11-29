package io.github.zwieback.familyfinance.business.operation.activity.helper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

import io.github.zwieback.familyfinance.business.operation.activity.IncomeOperationEditActivity;
import io.github.zwieback.familyfinance.business.operation.filter.IncomeOperationFilter;
import io.github.zwieback.familyfinance.core.model.OperationView;
import io.github.zwieback.familyfinance.util.DateUtils;
import io.github.zwieback.familyfinance.util.NumberUtils;
import io.github.zwieback.familyfinance.util.StringUtils;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class IncomeOperationHelper extends OperationHelper<IncomeOperationFilter> {

    public IncomeOperationHelper(Context context, ReactiveEntityStore<Persistable> data) {
        super(context, data);
    }

    @Override
    public Intent getIntentToAdd() {
        return getEmptyIntent();
    }

    @Override
    public Intent getIntentToAdd(@Nullable Integer articleId,
                                 @Nullable Integer accountId,
                                 @Nullable Integer ignoredTransferAccountId,
                                 @Nullable Integer ownerId,
                                 @Nullable Integer currencyId,
                                 @Nullable Integer exchangeRateId,
                                 @Nullable LocalDate date,
                                 @Nullable BigDecimal value,
                                 @Nullable String description) {
        Intent intent = getEmptyIntent();
        if (articleId != null && articleId != databasePrefs.getIncomesArticleId()) {
            intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_ARTICLE_ID, articleId);
        }
        if (accountId != null) {
            intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_ACCOUNT_ID, accountId);
        }
        if (ownerId != null) {
            intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_OWNER_ID, ownerId);
        }
        if (currencyId != null) {
            intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_CURRENCY_ID, currencyId);
        }
        if (exchangeRateId != null) {
            intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_EXCHANGE_RATE_ID,
                    exchangeRateId);
        }
        if (date != null) {
            DateUtils.writeLocalDateToIntent(intent,
                    IncomeOperationEditActivity.INPUT_INCOME_DATE, date);
        }
        if (value != null) {
            NumberUtils.writeBigDecimalToIntent(intent,
                    IncomeOperationEditActivity.INPUT_INCOME_VALUE, value);
        }
        if (StringUtils.isTextNotEmpty(description)) {
            intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_DESCRIPTION, description);
        }
        return intent;
    }

    @Override
    public Intent getIntentToAdd(@Nullable IncomeOperationFilter filter) {
        if (filter == null) {
            return getEmptyIntent();
        }
        return getIntentToAdd(filter.getArticleId(), filter.getAccountId(), null,
                filter.getOwnerId(), filter.getCurrencyId(), null, null, null, null);
    }

    @Override
    public Intent getIntentToEdit(OperationView operation) {
        Intent intent = getEmptyIntent();
        intent.putExtra(IncomeOperationEditActivity.INPUT_INCOME_OPERATION_ID, operation.getId());
        return intent;
    }

    @Override
    public Intent getIntentToDuplicate(OperationView operation) {
        return getIntentToAdd(operation.getArticleId(), operation.getAccountId(), null,
                operation.getOwnerId(), operation.getCurrencyId(), operation.getExchangeRateId(),
                operation.getDate(), operation.getValue(), operation.getDescription());
    }

    @Override
    Intent getEmptyIntent() {
        return new Intent(context, IncomeOperationEditActivity.class);
    }
}
