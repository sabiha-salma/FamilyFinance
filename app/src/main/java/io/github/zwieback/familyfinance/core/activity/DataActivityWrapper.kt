package io.github.zwieback.familyfinance.core.activity

import android.os.Bundle

import io.github.zwieback.familyfinance.app.FamilyFinanceApplication
import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.github.zwieback.familyfinance.core.preference.config.DatabasePrefs
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

abstract class DataActivityWrapper : ActivityWrapper() {

    protected lateinit var data: ReactiveEntityStore<Persistable>
    protected lateinit var databasePrefs: DatabasePrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = (application as FamilyFinanceApplication).data
        databasePrefs = DatabasePrefs.with(this)
    }

    protected open fun <E : IBaseEntity> loadEntity(
        entityClass: Class<E>,
        entityId: Int,
        onSuccess: Consumer<E>,
        onError: Consumer<in Throwable>
    ) {
        addDisposable(
            data.findByKey(entityClass, entityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError)
        )
    }
}
