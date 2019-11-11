package io.github.zwieback.familyfinance.app

import android.os.StrictMode
import io.github.zwieback.familyfinance.app.lifecycle.creator.DatabaseTableCreator
import io.github.zwieback.familyfinance.app.lifecycle.destroyer.DatabaseDestroyer
import io.github.zwieback.familyfinance.core.model.Models
import io.requery.android.sqlite.DatabaseProvider
import io.requery.android.sqlitex.SqlitexDatabaseSource
import io.requery.sql.TableCreationMode

class FamilyFinanceApplication : AbstractApplication() {

    override fun onCreate() {
        super.onCreate()
        turnOnStrictMode()
//        recreateDatabase();
    }

    override fun buildDatabaseProvider(): DatabaseProvider<*> {
        // override onUpgrade to handle migrating to a new version
        val source = SqlitexDatabaseSource(this, Models.DEFAULT, DB_VERSION)
        // use this in development mode to drop and recreate the tables on every upgrade
        source.setLoggingEnabled(true)
        source.setTableCreationMode(TableCreationMode.CREATE_NOT_EXISTS)
        destroyViews(source.configuration)
        createViews(source.configuration)
        return source
    }

    private fun recreateDatabase() {
        DatabaseDestroyer(this).deleteDatabases()
        DatabaseTableCreator(this, data).createTables()
    }

    private fun turnOnStrictMode() {
        StrictMode.enableDefaults()
    }
}
