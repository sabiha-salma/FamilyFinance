package io.github.zwieback.familyfinance.core.model.restriction

interface AccountRestriction {

    companion object {

        const val CARD_NUMBER_MAX_LENGTH = 20

        const val NAME_MAX_LENGTH = 100

        const val NUMBER_MAX_LENGTH = 20

        const val PAYMENT_SYSTEM_MAX_LENGTH = 40

        const val TYPE_MAX_LENGTH = 50
    }
}
