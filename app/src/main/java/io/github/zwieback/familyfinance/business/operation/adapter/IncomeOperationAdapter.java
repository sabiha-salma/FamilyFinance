package io.github.zwieback.familyfinance.business.operation.adapter;

import android.content.Context;
import androidx.annotation.NonNull;

import io.github.zwieback.familyfinance.business.operation.filter.IncomeOperationFilter;
import io.github.zwieback.familyfinance.business.operation.listener.OnOperationClickListener;
import io.github.zwieback.familyfinance.business.operation.query.IncomeOperationQueryBuilder;
import io.github.zwieback.familyfinance.business.operation.service.provider.IncomeOperationViewProvider;
import io.github.zwieback.familyfinance.core.adapter.EntityProvider;
import io.github.zwieback.familyfinance.core.model.OperationView;
import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.reactivex.ReactiveEntityStore;

public class IncomeOperationAdapter extends OperationAdapter<IncomeOperationFilter> {

    public IncomeOperationAdapter(Context context,
                                  OnOperationClickListener clickListener,
                                  ReactiveEntityStore<Persistable> data,
                                  IncomeOperationFilter filter) {
        super(context, clickListener, data, filter);
    }

    @Override
    protected EntityProvider<OperationView> createProvider(Context context) {
        return new IncomeOperationViewProvider(context);
    }

    @NonNull
    @Override
    public Result<OperationView> performQuery() {
        return IncomeOperationQueryBuilder.create(getData())
                .setStartDate(getFilter().getStartDate())
                .setEndDate(getFilter().getEndDate())
                .setStartValue(getFilter().getStartValue())
                .setEndValue(getFilter().getEndValue())
                .setOwnerId(getFilter().getOwnerId())
                .setCurrencyId(getFilter().getCurrencyId())
                .setArticleId(getFilter().getArticleId())
                .setAccountId(getFilter().getAccountId())
                .build();
    }
}
