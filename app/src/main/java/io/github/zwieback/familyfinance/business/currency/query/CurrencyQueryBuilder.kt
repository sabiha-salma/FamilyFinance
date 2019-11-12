package io.github.zwieback.familyfinance.business.currency.query

import io.github.zwieback.familyfinance.core.model.CurrencyView
import io.github.zwieback.familyfinance.core.query.EntityQueryBuilder
import io.requery.Persistable
import io.requery.query.Limit
import io.requery.query.OrderBy
import io.requery.query.Where
import io.requery.query.WhereAndOr
import io.requery.reactivex.ReactiveEntityStore
import io.requery.reactivex.ReactiveResult

class CurrencyQueryBuilder private constructor(data: ReactiveEntityStore<Persistable>) :
    EntityQueryBuilder<CurrencyView>(data) {

    override val entityClass: Class<CurrencyView>
        get() = CurrencyView::class.java

    override fun buildWhere(
        select: Where<ReactiveResult<CurrencyView>>
    ): WhereAndOr<ReactiveResult<CurrencyView>> {
        return select.where(CurrencyView.ID.gt(0))
    }

    override fun buildOrderBy(
        where: OrderBy<Limit<ReactiveResult<CurrencyView>>>
    ): Limit<ReactiveResult<CurrencyView>> {
        return where.orderBy(CurrencyView.ID)
    }

    companion object {
        fun create(data: ReactiveEntityStore<Persistable>): CurrencyQueryBuilder {
            return CurrencyQueryBuilder(data)
        }
    }
}
