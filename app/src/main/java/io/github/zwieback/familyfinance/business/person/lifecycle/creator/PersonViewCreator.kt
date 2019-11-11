package io.github.zwieback.familyfinance.business.person.lifecycle.creator

import io.github.zwieback.familyfinance.core.lifecycle.creator.EntityViewCreator
import java.sql.Connection

class PersonViewCreator(connection: Connection) : EntityViewCreator(connection) {

    override val viewName: String
        get() = "v_person"

    override val viewBody: String
        get() = " SELECT pe.id        AS id," +
                "       pe.icon_name  AS icon_name," +
                "       pe.is_folder  AS is_folder," +
                "       pe.name       AS name," +
                "       pe.order_code AS order_code," +
                "       pp.id         AS parent_id," +
                "       pp.name       AS parent_name" +
                "  FROM person pe" +
                "       LEFT JOIN person pp ON pp.id = pe.parent_id"
}
