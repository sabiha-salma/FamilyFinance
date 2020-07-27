package io.github.zwieback.familyfinance.business.operation.adapter

import android.content.Context
import io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter
import io.github.zwieback.familyfinance.business.operation.listener.OnOperationClickListener
import io.github.zwieback.familyfinance.business.operation.query.TransferOperationQueryBuilder
import io.github.zwieback.familyfinance.business.operation.service.provider.TransferOperationViewProvider
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.OperationView
import io.requery.Persistable
import io.requery.query.Result
import io.requery.reactivex.ReactiveEntityStore

class TransferOperationAdapter(
    context: Context,
    clickListener: OnOperationClickListener,
    data: ReactiveEntityStore<Persistable>,
    filter: TransferOperationFilter
) : OperationAdapter<TransferOperationFilter>(context, clickListener, data, filter) {

    override fun createProvider(context: Context): EntityProvider<OperationView> {
        return TransferOperationViewProvider(context)
    }

    override fun performQuery(): Result<OperationView> {
        return TransferOperationQueryBuilder.create(data)
            .withStartDate(filter.startDate)
            .withEndDate(filter.endDate)
            .withStartValue(filter.startValue)
            .withEndValue(filter.endValue)
            .withOwnerId(filter.takeOwnerId())
            .withCurrencyId(filter.takeCurrencyId())
            .withAccountId(filter.takeAccountId())
            .withSortType(sortType)
            .build()
    }
}
