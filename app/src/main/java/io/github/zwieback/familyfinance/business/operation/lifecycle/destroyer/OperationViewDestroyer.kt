package io.github.zwieback.familyfinance.business.operation.lifecycle.destroyer

import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityViewDestroyer
import java.sql.Connection

class OperationViewDestroyer(connection: Connection) : EntityViewDestroyer(connection) {

    override val viewName: String
        get() = "v_operation"
}
