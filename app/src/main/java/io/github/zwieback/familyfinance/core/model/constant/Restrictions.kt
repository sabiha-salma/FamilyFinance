package io.github.zwieback.familyfinance.core.model.constant

object AccountRestriction {
    const val CARD_NUMBER_MAX_LENGTH = 20
    const val NAME_MAX_LENGTH = 100
    const val NUMBER_MAX_LENGTH = 20
    const val PAYMENT_SYSTEM_MAX_LENGTH = 40
    const val TYPE_MAX_LENGTH = 50
}

object ArticleRestriction {
    const val TYPE_MAX_LENGTH = 30
    const val NAME_MAX_LENGTH = 50
    const val NAME_ASCII_MAX_LENGTH = 100
}

object BaseRestriction {
    const val ICON_NAME_MAX_LENGTH = 50
}

object CurrencyRestriction {
    const val NAME_MAX_LENGTH = 10
    const val DESCRIPTION_MAX_LENGTH = 100
}

object OperationRestriction {
    const val TYPE_MAX_LENGTH = 30
    const val DESCRIPTION_MAX_LENGTH = 1024
    const val URL_MAX_LENGTH = 1024
}

object PersonRestriction {
    const val NAME_MAX_LENGTH = 50
}

object SmsPatternRestriction {
    const val NAME_MAX_LENGTH = 100
    const val REGEX_MAX_LENGTH = 255
    const val SENDER_MAX_LENGTH = 20
}

object TemplateRestriction {
    const val NAME_MAX_LENGTH = 100
    const val TYPE_MAX_LENGTH = 30
}
