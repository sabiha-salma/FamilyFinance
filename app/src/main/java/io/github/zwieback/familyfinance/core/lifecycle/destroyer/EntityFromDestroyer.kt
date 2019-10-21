package io.github.zwieback.familyfinance.core.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.reactivex.functions.Consumer
import io.requery.Persistable
import io.requery.query.Condition
import io.requery.reactivex.ReactiveEntityStore

abstract class EntityFromDestroyer<E : IBaseEntity, F : IBaseEntity>(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityAlertDestroyer<E>(context, data) {

    protected abstract val fromClass: Class<F>

    protected abstract fun getWhereCondition(entity: E): Condition<*, *>

    override fun destroy(entity: E, terminalConsumer: Consumer<Int>) {
        data.count(fromClass)
            .where(getWhereCondition(entity)).get().single()
            .subscribe { operationCount ->
                if (operationCount > 0) {
                    showAlert(alertResourceId)
                } else {
                    next()?.destroy(entity, terminalConsumer)
                }
            }
    }
}
