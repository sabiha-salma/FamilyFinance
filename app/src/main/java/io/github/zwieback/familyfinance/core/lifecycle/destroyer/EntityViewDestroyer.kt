package io.github.zwieback.familyfinance.core.lifecycle.destroyer

import io.reactivex.Single
import java.sql.Connection
import java.util.concurrent.Callable

abstract class EntityViewDestroyer protected constructor(
    private val connection: Connection
) : Callable<Single<Boolean>> {

    protected abstract val viewName: String

    /**
     * @return `false` because "DROP VIEW" is DDL operation
     */
    override fun call(): Single<Boolean> {
        return Single.fromCallable {
            connection.createStatement()
                .use { statement -> statement.execute("DROP VIEW IF EXISTS $viewName") }
        }
    }
}
