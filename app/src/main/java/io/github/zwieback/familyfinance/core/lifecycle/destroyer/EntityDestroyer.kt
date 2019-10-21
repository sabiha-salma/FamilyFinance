package io.github.zwieback.familyfinance.core.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.reactivex.functions.Consumer
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

abstract class EntityDestroyer<E : IBaseEntity>(
    protected val context: Context,
    protected val data: ReactiveEntityStore<Persistable>
) {

    protected abstract operator fun next(): EntityDestroyer<E>?

    abstract fun destroy(entity: E, terminalConsumer: Consumer<Int>)
}
