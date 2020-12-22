package io.github.zwieback.familyfinance.business.operation.adapter

import android.content.Context
import io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter
import io.github.zwieback.familyfinance.business.operation.listener.OnOperationClickListener
import io.github.zwieback.familyfinance.business.operation.query.ExpenseOperationQueryBuilder
import io.github.zwieback.familyfinance.business.operation.service.provider.ExpenseOperationViewProvider
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.OperationView
import io.requery.Persistable
import io.requery.query.Result
import io.requery.reactivex.ReactiveEntityStore

class ExpenseOperationAdapter(
    context: Context,
    clickListener: OnOperationClickListener,
    data: ReactiveEntityStore<Persistable>,
    filter: ExpenseOperationFilter
) : OperationAdapter<ExpenseOperationFilter>(context, clickListener, data, filter) {

    override fun createProvider(context: Context): EntityProvider<OperationView> {
        return ExpenseOperationViewProvider(context)
    }

    override fun performQuery(): Result<OperationView> {
        return ExpenseOperationQueryBuilder.create(data)
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
