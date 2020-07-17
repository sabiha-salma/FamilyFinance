package io.github.zwieback.familyfinance.business.sms_pattern.lifecycle.creator

import io.github.zwieback.familyfinance.core.lifecycle.creator.EntityViewCreator
import java.sql.Connection

class SmsPatternViewCreator(connection: Connection) : EntityViewCreator(connection) {

    override val viewName: String
        get() = "v_sms_pattern"

    override val viewBody: String
        get() = " SELECT sp.id           AS id," +
                "       sp.icon_name     AS icon_name," +
                "       sp.create_date   AS create_date," +
                "       sp.last_change_date AS last_change_date," +
                "       sp.name          AS name," +
                "       sp.regex         AS regex," +
                "       sp.sender        AS sender," +
                "       sp.date_group    AS date_group," +
                "       sp.value_group   AS value_group," +
                "       sp.common        AS common," +
                "       te.id            AS template_id," +
                "       te.name          AS template_name" +
                "  FROM t_sms_pattern sp" +
                "       LEFT JOIN t_template te ON te.id = sp.template_id"
}
