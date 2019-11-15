package com.johnpetitto.validator

import android.util.Patterns
import io.github.zwieback.familyfinance.util.DateUtils
import io.github.zwieback.familyfinance.util.NumberUtils

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
    NumberUtils.isTextAnInteger(input)
}

/**
 * Validates input for BigDecimal number formatting.
 */
val BigDecimalValidator: Validator = { input ->
    NumberUtils.isTextABigDecimal(input)
}

/**
 * Validates input for LocalDate date formatting.
 */
val DateValidator: Validator = { input ->
    DateUtils.isTextAnLocalDate(input)
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
    NumberUtils.isTextASignedNumber(input)
}

/**
 * Validates input for account number formatting.
 */
val AccountNumberValidator: Validator = { input ->
    NumberUtils.isTextAnAccountNumber(input)
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
