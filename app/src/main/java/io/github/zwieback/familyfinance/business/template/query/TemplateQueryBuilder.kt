package io.github.zwieback.familyfinance.business.template.query

import io.github.zwieback.familyfinance.core.model.TemplateView
import io.github.zwieback.familyfinance.core.query.EntityQueryBuilder
import io.requery.Persistable
import io.requery.query.Limit
import io.requery.query.OrderBy
import io.requery.query.Where
import io.requery.query.WhereAndOr
import io.requery.reactivex.ReactiveEntityStore
import io.requery.reactivex.ReactiveResult

class TemplateQueryBuilder private constructor(data: ReactiveEntityStore<Persistable>) :
    EntityQueryBuilder<TemplateView>(data) {

    override val entityClass: Class<TemplateView>
        get() = TemplateView::class.java

    override fun buildWhere(
        select: Where<ReactiveResult<TemplateView>>
    ): WhereAndOr<ReactiveResult<TemplateView>> {
        return select.where(TemplateView.ID.gt(0))
    }

    override fun buildOrderBy(
        where: OrderBy<Limit<ReactiveResult<TemplateView>>>
    ): Limit<ReactiveResult<TemplateView>> {
        return where.orderBy(TemplateView.TYPE, TemplateView.NAME)
    }

    companion object {
        fun create(data: ReactiveEntityStore<Persistable>): TemplateQueryBuilder {
            return TemplateQueryBuilder(data)
        }
    }
}
