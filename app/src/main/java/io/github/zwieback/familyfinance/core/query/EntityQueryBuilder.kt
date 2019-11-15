package io.github.zwieback.familyfinance.core.query

import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.requery.Persistable
import io.requery.query.*
import io.requery.reactivex.ReactiveEntityStore
import io.requery.reactivex.ReactiveResult

abstract class EntityQueryBuilder<E : IBaseEntity> protected constructor(
    protected val data: ReactiveEntityStore<Persistable>
) {

    protected abstract val entityClass: Class<E>

    protected open val isJoinRequired: Boolean
        get() = false

    private fun buildSelect(): Selection<ReactiveResult<E>> {
        return data.select(entityClass)
    }

    /**
     * @return `not null` if [isJoinRequired] is `true`
     */
    protected open fun buildJoin(
        select: Selection<ReactiveResult<E>>
    ): JoinAndOr<ReactiveResult<E>>? {
        return null
    }

    protected abstract fun buildWhere(
        select: Where<ReactiveResult<E>>
    ): WhereAndOr<ReactiveResult<E>>

    protected abstract fun buildOrderBy(
        where: OrderBy<Limit<ReactiveResult<E>>>
    ): Limit<ReactiveResult<E>>

    fun build(): Result<E> {
        val select = buildSelect()
        val where = if (isJoinRequired) {
            buildJoin(select)
                ?.let { joinAndOr -> buildWhere(joinAndOr) }
                ?: error("Did you forget to override the buildJoin() method?")
        } else {
            buildWhere(select)
        }
        val orderBy = buildOrderBy(where)
        return orderBy.get()
    }
}
