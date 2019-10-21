package io.github.zwieback.familyfinance.core.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.reactivex.functions.Consumer
import io.requery.Persistable
import io.requery.meta.QueryExpression
import io.requery.reactivex.ReactiveEntityStore

abstract class EntityAsParentDestroyer<E : IBaseEntity>(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityAlertDestroyer<E>(context, data) {

    protected abstract val entityClass: Class<E>

    protected abstract val parentIdExpression: QueryExpression<Int>

    override fun destroy(entity: E, terminalConsumer: Consumer<Int>) {
        data.count(entityClass)
            .where(parentIdExpression.eq(entity.id)).get().single()
            .subscribe { childCount ->
                if (childCount > 0) {
                    showAlert(alertResourceId)
                } else {
                    next()?.destroy(entity, terminalConsumer)
                }
            }
    }
}
