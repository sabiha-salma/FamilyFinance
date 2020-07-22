package io.github.zwieback.familyfinance.business.exchange_rate.lifecycle.destroyer

import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityViewDestroyer
import io.github.zwieback.familyfinance.core.model.constant.Views
import java.sql.Connection

class ExchangeRateViewDestroyer(connection: Connection) : EntityViewDestroyer(connection) {

    override val viewName: String
        get() = Views.EXCHANGE_RATE
}
