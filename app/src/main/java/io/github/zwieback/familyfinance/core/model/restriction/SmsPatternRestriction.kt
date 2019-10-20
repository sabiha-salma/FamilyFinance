package io.github.zwieback.familyfinance.core.model.restriction

interface SmsPatternRestriction {

    companion object {

        const val NAME_MAX_LENGTH = 100

        const val REGEX_MAX_LENGTH = 255

        const val SENDER_MAX_LENGTH = 20
    }
}
