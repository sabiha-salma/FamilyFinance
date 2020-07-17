package io.github.zwieback.familyfinance.business.exchange_rate.lifecycle.creator

import io.github.zwieback.familyfinance.core.lifecycle.creator.EntityViewCreator
import io.github.zwieback.familyfinance.core.model.constant.Views
import java.sql.Connection

class ExchangeRateViewCreator(connection: Connection) : EntityViewCreator(connection) {

    override val viewName: String
        get() = Views.EXCHANGE_RATE

    override val viewBody: String
        get() = " SELECT er.id       AS id," +
                "       er.icon_name AS icon_name," +
                "       er.create_date      AS create_date," +
                "       er.last_change_date AS last_change_date," +
                "       er._value    AS _value," +
                "       er._date     AS _date," +
                "       cu.id        AS currency_id," +
                "       cu.name      AS currency_name" +
                "  FROM exchange_rate er" +
                "       INNER JOIN currency cu ON cu.id = er.currency_id"
}
