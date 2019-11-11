package io.github.zwieback.familyfinance.core.lifecycle.creator

import android.content.Context
import androidx.annotation.StringRes
import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.github.zwieback.familyfinance.core.preference.config.DatabasePrefs
import io.reactivex.Single
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore
import java.util.concurrent.Callable

abstract class EntityCreator<E : IBaseEntity> protected constructor(
    protected val context: Context,
    protected val data: ReactiveEntityStore<Persistable>
) : Callable<Single<Iterable<E>>>, Comparator<E> {

    protected val databasePrefs: DatabasePrefs = DatabasePrefs.with(context)

    protected abstract fun buildEntities(): Iterable<E>

    override fun call(): Single<Iterable<E>> {
        return data.insert(buildEntities())
    }

    protected fun getString(@StringRes resId: Int): String {
        return context.resources.getString(resId)
    }
}
