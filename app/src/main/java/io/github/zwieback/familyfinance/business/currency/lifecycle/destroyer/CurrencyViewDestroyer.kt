package io.github.zwieback.familyfinance.business.currency.lifecycle.destroyer

import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityViewDestroyer
import io.github.zwieback.familyfinance.core.model.constant.Views
import java.sql.Connection

class CurrencyViewDestroyer(connection: Connection) : EntityViewDestroyer(connection) {

    override val viewName: String
        get() = Views.CURRENCY
}
