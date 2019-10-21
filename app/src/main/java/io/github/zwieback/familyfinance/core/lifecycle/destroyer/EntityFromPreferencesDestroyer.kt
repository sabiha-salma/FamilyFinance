package io.github.zwieback.familyfinance.core.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.github.zwieback.familyfinance.core.preference.config.DatabasePrefs
import io.reactivex.functions.Consumer
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

abstract class EntityFromPreferencesDestroyer<E : IBaseEntity>(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityAlertDestroyer<E>(context, data) {

    protected val databasePrefs: DatabasePrefs = DatabasePrefs.with(context)

    protected abstract fun preferencesContainsEntity(entity: E): Boolean

    override fun destroy(entity: E, terminalConsumer: Consumer<Int>) {
        if (preferencesContainsEntity(entity)) {
            showAlert(alertResourceId)
        } else {
            next()?.destroy(entity, terminalConsumer)
        }
    }
}
