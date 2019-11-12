package io.github.zwieback.familyfinance.business.sms_pattern.lifecycle.destroyer

import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityViewDestroyer
import java.sql.Connection

class SmsPatternViewDestroyer(connection: Connection) : EntityViewDestroyer(connection) {

    override val viewName: String
        get() = "v_sms_pattern"
}
