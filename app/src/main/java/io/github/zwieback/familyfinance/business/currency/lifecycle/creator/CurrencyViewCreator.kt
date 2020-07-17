package io.github.zwieback.familyfinance.business.currency.lifecycle.creator

import io.github.zwieback.familyfinance.core.lifecycle.creator.EntityViewCreator
import io.github.zwieback.familyfinance.core.model.constant.Views
import java.sql.Connection

class CurrencyViewCreator(connection: Connection) : EntityViewCreator(connection) {

    override val viewName: String
        get() = Views.CURRENCY

    override val viewBody: String
        get() = " SELECT cu.id         AS id," +
                "       cu.icon_name   AS icon_name," +
                "       cu.create_date AS create_date," +
                "       cu.last_change_date AS last_change_date," +
                "       cu.name        AS name," +
                "       cu.description AS description" +
                "  FROM currency cu"
}
