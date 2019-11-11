package io.github.zwieback.familyfinance.core.lifecycle.creator

import io.reactivex.Single
import java.sql.Connection
import java.util.concurrent.Callable

abstract class EntityViewCreator protected constructor(
    private val connection: Connection
) : Callable<Single<Boolean>> {

    protected abstract val viewName: String

    protected abstract val viewBody: String

    /**
     * @return `false` because "CREATE VIEW" is DDL operation
     */
    override fun call(): Single<Boolean> {
        return Single.fromCallable {
            connection.createStatement().use { statement ->
                statement.execute("CREATE VIEW IF NOT EXISTS $viewName AS $viewBody")
            }
        }
    }
}
