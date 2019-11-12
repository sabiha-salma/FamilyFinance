package io.github.zwieback.familyfinance.business.person.query

import io.github.zwieback.familyfinance.core.model.PersonView
import io.github.zwieback.familyfinance.core.query.EntityFolderQueryBuilder
import io.requery.Persistable
import io.requery.meta.QueryExpression
import io.requery.query.Limit
import io.requery.query.OrderBy
import io.requery.reactivex.ReactiveEntityStore
import io.requery.reactivex.ReactiveResult

class PersonQueryBuilder private constructor(data: ReactiveEntityStore<Persistable>) :
    EntityFolderQueryBuilder<PersonQueryBuilder, PersonView>(data) {

    override val entityClass: Class<PersonView>
        get() = PersonView::class.java

    override val parentIdColumn: QueryExpression<Int>
        get() = PersonView.PARENT_ID

    override fun buildOrderBy(
        where: OrderBy<Limit<ReactiveResult<PersonView>>>
    ): Limit<ReactiveResult<PersonView>> {
        return where.orderBy(PersonView.ORDER_CODE)
    }

    companion object {
        fun create(data: ReactiveEntityStore<Persistable>): PersonQueryBuilder {
            return PersonQueryBuilder(data)
        }
    }
}
