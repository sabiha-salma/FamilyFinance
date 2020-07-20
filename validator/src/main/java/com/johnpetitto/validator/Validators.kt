package com.johnpetitto.validator

import android.util.Patterns
import io.github.zwieback.familyfinance.extension.*

/**
 * Validates input for [ValidatingTextInputLayout] to meet some requirement.
 *
 * Returns `true` if the input is considered valid for some requirement.
 */
typealias Validator = (input: String) -> Boolean

/**
 * Validates input for email formatting.
 */
val EmailValidator: Validator = { input ->
    Patterns.EMAIL_ADDRESS.matcher(input).matches()
}

/**
 * Validates input for phone number formatting.
 */
val PhoneValidator: Validator = { input ->
    Patterns.PHONE.matcher(input).matches()
}

/**
 * Validates input for Integer number formatting.
 */
val IntegerValidator: Validator = { input ->
    input.isInt()
}

/**
 * Validates input for BigDecimal number formatting.
 */
val BigDecimalValidator: Validator = { input ->
    input.isBigDecimal()
}

/**
 * Validates input for LocalDate date formatting.
 */
val DateValidator: Validator = { input ->
    input.isLocalDate()
}

/**
 * Validates input for not empty String.
 */
val NotEmptyValidator: Validator = { input ->
    input.isNotEmpty()
}

/**
 * Validates input for number formatting.
 */
val SignedNumberValidator: Validator = { input ->
    input.isSignedNumber()
}

/**
 * Validates input for account number formatting.
 */
val AccountNumberValidator: Validator = { input ->
    input.isAccountNumber()
}

/**
 * Collection of utilities for working with [ValidatingTextInputLayout].
 */
object Validators {
    /**
     * Validates multiple inputs at once and returns `true` if all inputs
     * are valid.
     */
    fun validate(layouts: List<ValidatingTextInputLayout>): Boolean {
        return layouts.all { it.validate() }
    }
}
