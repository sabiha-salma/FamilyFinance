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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

abstract class DataActivityWrapper :
    ActivityWrapper(),
    CoroutineScope {

    protected lateinit var data: ReactiveEntityStore<Persistable>
    protected lateinit var databasePrefs: DatabasePrefs
    private lateinit var rootJob: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootJob = Job()
        data = (application as FamilyFinanceApplication).data
        runBlocking(Dispatchers.IO) {
            databasePrefs = DatabasePrefs.with(this@DataActivityWrapper)
        }
    }

    override fun onDestroy() {
        rootJob.cancel()
        super.onDestroy()
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
