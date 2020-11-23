package io.github.zwieback.familyfinance.app

import androidx.multidex.MultiDexApplication
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mikepenz.iconics.Iconics
import io.github.zwieback.familyfinance.app.lifecycle.creator.DatabaseViewCreator
import io.github.zwieback.familyfinance.app.lifecycle.destroyer.DatabaseViewDestroyer
import io.github.zwieback.familyfinance.core.model.Models
import io.requery.Persistable
import io.requery.android.sqlite.DatabaseProvider
import io.requery.android.sqlitex.SqlitexDatabaseSource
import io.requery.reactivex.ReactiveEntityStore
import io.requery.reactivex.ReactiveSupport
import io.requery.sql.Configuration
import io.requery.sql.EntityDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.sql.SQLException

abstract class AbstractApplication : MultiDexApplication() {

    /**
     * Note if you're using Dagger you can make this part of your application
     * level module returning `@Provides @Singleton`.
     *
     * @return [EntityDataStore] single instance for the application.
     */
    val data: ReactiveEntityStore<Persistable> by lazy {
        val databaseProvider = createDatabaseProvider()
        setupDatabaseProvider(databaseProvider)
        val configuration = databaseProvider.configuration
        destroyViews(configuration)
        createViews(configuration)
        ReactiveSupport.toReactiveStore(EntityDataStore(configuration))
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        Iconics.init(this)
    }

    private fun createDatabaseProvider(): DatabaseProvider<*> {
        return SqlitexDatabaseSource(this, Models.DEFAULT, DB_VERSION)
    }

    protected open fun setupDatabaseProvider(source: DatabaseProvider<*>) {
        // do nothing
    }

    /**
     * Workaround for creating views, while requery does not support
     * creating views.
     *
     * Note: This method must be called before the creation of the [data],
     * otherwise [SQLException] will be thrown.
     *
     * See [Using SQLite views](https://github.com/requery/requery/issues/721.issuecomment-344153774)
     */
    @Throws(SQLException::class)
    private fun createViews(configuration: Configuration) = runBlocking(Dispatchers.IO) {
        configuration.connectionProvider.connection.use { connection ->
            DatabaseViewCreator(connection).createViews()
        }
    }

    /**
     * Workaround for destroying views, while requery does not support
     * destroying views.
     *
     * Note: This method must be called before the creation of the [data],
     * otherwise [SQLException] will be thrown.
     *
     * See [Using SQLite views](https://github.com/requery/requery/issues/721.issuecomment-344153774)
     */
    @Throws(SQLException::class)
    private fun destroyViews(configuration: Configuration) = runBlocking(Dispatchers.IO) {
        configuration.connectionProvider.connection.use { connection ->
            DatabaseViewDestroyer(connection).destroyViews()
        }
    }

    companion object {
        const val DB_VERSION = 9
    }
}
