package io.github.zwieback.familyfinance.app

import android.os.StrictMode
import io.github.zwieback.familyfinance.app.lifecycle.creator.DatabaseTableCreator
import io.github.zwieback.familyfinance.app.lifecycle.destroyer.DatabaseDestroyer
import io.requery.android.sqlite.DatabaseProvider
import io.requery.sql.TableCreationMode

class FamilyFinanceApplication : AbstractApplication() {

    override fun onCreate() {
        super.onCreate()
        turnOnStrictMode()
//        recreateDatabase();
    }

    override fun setupDatabaseProvider(source: DatabaseProvider<*>) {
        // use this in development mode to drop and recreate the tables on every upgrade
        source.setLoggingEnabled(true)
        source.setTableCreationMode(TableCreationMode.CREATE_NOT_EXISTS)
    }

    private fun recreateDatabase() {
        DatabaseDestroyer(this).deleteDatabases()
        DatabaseTableCreator(this, data).createTables()
    }

    private fun turnOnStrictMode() {
        StrictMode.enableDefaults()
    }
}
