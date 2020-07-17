package io.github.zwieback.familyfinance.business.sms_pattern.lifecycle.destroyer

import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityViewDestroyer
import io.github.zwieback.familyfinance.core.model.constant.Views
import java.sql.Connection

class SmsPatternViewDestroyer(connection: Connection) : EntityViewDestroyer(connection) {

    override val viewName: String
        get() = Views.SMS_PATTERN
}
