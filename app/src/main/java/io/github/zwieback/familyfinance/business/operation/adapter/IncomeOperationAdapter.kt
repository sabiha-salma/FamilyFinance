package io.github.zwieback.familyfinance.business.operation.adapter

import android.content.Context
import io.github.zwieback.familyfinance.business.operation.filter.IncomeOperationFilter
import io.github.zwieback.familyfinance.business.operation.listener.OnOperationClickListener
import io.github.zwieback.familyfinance.business.operation.query.IncomeOperationQueryBuilder
import io.github.zwieback.familyfinance.business.operation.service.provider.IncomeOperationViewProvider
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.OperationView
import io.requery.Persistable
import io.requery.query.Result
import io.requery.reactivex.ReactiveEntityStore

class IncomeOperationAdapter(
    context: Context,
    clickListener: OnOperationClickListener,
    data: ReactiveEntityStore<Persistable>,
    filter: IncomeOperationFilter
) : OperationAdapter<IncomeOperationFilter>(context, clickListener, data, filter) {

    override fun createProvider(context: Context): EntityProvider<OperationView> {
        return IncomeOperationViewProvider(context)
    }

    override fun performQuery(): Result<OperationView> {
        return IncomeOperationQueryBuilder.create(data)
            .withStartDate(filter.startDate)
            .withEndDate(filter.endDate)
            .withStartValue(filter.startValue)
            .withEndValue(filter.endValue)
            .withOwnerId(filter.takeOwnerId())
            .withToWhomIsNull(filter.toWhomIsNull)
            .withToWhomId(filter.takeToWhomId())
            .withCurrencyId(filter.takeCurrencyId())
            .withArticleId(filter.takeArticleId())
            .withAccountId(filter.takeAccountId())
            .withSortType(sortType)
            .build()
    }
}
