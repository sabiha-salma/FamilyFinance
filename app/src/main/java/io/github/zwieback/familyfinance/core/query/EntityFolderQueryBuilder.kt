package io.github.zwieback.familyfinance.core.query

import io.github.zwieback.familyfinance.core.model.IBaseEntityFolder
import io.requery.Persistable
import io.requery.meta.QueryExpression
import io.requery.query.Where
import io.requery.query.WhereAndOr
import io.requery.reactivex.ReactiveEntityStore
import io.requery.reactivex.ReactiveResult

abstract class EntityFolderQueryBuilder<T : EntityFolderQueryBuilder<T, E>, E : IBaseEntityFolder>
protected constructor(
    data: ReactiveEntityStore<Persistable>
) : EntityQueryBuilder<E>(data) {

    protected var parentId: Int? = null

    protected abstract val parentIdColumn: QueryExpression<Int>

    override fun buildWhere(select: Where<ReactiveResult<E>>): WhereAndOr<ReactiveResult<E>> {
        return select.where(parentIdColumn.eq(parentId))
    }

    @Suppress("UNCHECKED_CAST")
    fun withParentId(parentId: Int?): T {
        return apply { this.parentId = parentId } as T
    }
}
