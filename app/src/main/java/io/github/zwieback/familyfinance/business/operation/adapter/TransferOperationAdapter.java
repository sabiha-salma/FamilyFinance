package io.github.zwieback.familyfinance.business.operation.adapter;

import android.content.Context;
import androidx.annotation.NonNull;

import io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter;
import io.github.zwieback.familyfinance.business.operation.listener.OnOperationClickListener;
import io.github.zwieback.familyfinance.business.operation.query.TransferOperationQueryBuilder;
import io.github.zwieback.familyfinance.business.operation.service.provider.TransferOperationViewProvider;
import io.github.zwieback.familyfinance.core.adapter.EntityProvider;
import io.github.zwieback.familyfinance.core.model.OperationView;
import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.reactivex.ReactiveEntityStore;

public class TransferOperationAdapter extends OperationAdapter<TransferOperationFilter> {

    public TransferOperationAdapter(Context context,
                                    OnOperationClickListener clickListener,
                                    ReactiveEntityStore<Persistable> data,
                                    TransferOperationFilter filter) {
        super(context, clickListener, data, filter);
    }

    @Override
    protected EntityProvider<OperationView> createProvider(Context context) {
        return new TransferOperationViewProvider(context);
    }

    @NonNull
    @Override
    public Result<OperationView> performQuery() {
        return TransferOperationQueryBuilder.create(getData())
                .setStartDate(getFilter().getStartDate())
                .setEndDate(getFilter().getEndDate())
                .setStartValue(getFilter().getStartValue())
                .setEndValue(getFilter().getEndValue())
                .setOwnerId(getFilter().getOwnerId())
                .setCurrencyId(getFilter().getCurrencyId())
                .setAccountId(getFilter().getAccountId())
                .build();
    }
}
