package io.github.zwieback.familyfinance.core.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.reactivex.functions.Consumer
import io.requery.Persistable
import io.requery.meta.QueryAttribute
import io.requery.reactivex.ReactiveEntityStore

abstract class EntityForceDestroyer<E : IBaseEntity>(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityDestroyer<E>(context, data) {

    protected abstract val entityClass: Class<E>

    protected abstract val idAttribute: QueryAttribute<E, Int>

    override fun next(): EntityDestroyer<E>? = null

    override fun destroy(entity: E, terminalConsumer: Consumer<Int>) {
        data.delete(entityClass)
            .where(idAttribute.eq(entity.id)).get().single()
            .subscribe(terminalConsumer)
    }
}
