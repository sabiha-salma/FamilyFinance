package com.johnpetitto.validator

import android.util.Patterns
import io.github.zwieback.familyfinance.util.DateUtils
import io.github.zwieback.familyfinance.util.NumberUtils

/**
 * Validates input for [ValidatingTextInputLayout] to meet some requirement.
 */
interface Validator {
    /**
     * Returns `true` if the input is considered valid for some requirement.
     */
    fun isValid(input: String): Boolean
}

/**
 * Validates input for email formatting.
 */
object EmailValidator : Validator {
    override fun isValid(input: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }
}

/**
 * Validates input for phone number formatting.
 */
object PhoneValidator : Validator {
    override fun isValid(input: String): Boolean {
        return Patterns.PHONE.matcher(input).matches()
    }
}

/**
 * Validates input for Integer number formatting.
 */
object IntegerValidator : Validator {
    override fun isValid(input: String): Boolean {
        return NumberUtils.isTextAnInteger(input)
    }
}

/**
 * Validates input for BigDecimal number formatting.
 */
object BigDecimalValidator : Validator {
    override fun isValid(input: String): Boolean {
        return NumberUtils.isTextABigDecimal(input)
    }
}

/**
 * Validates input for LocalDate date formatting.
 */
object DateValidator : Validator {
    override fun isValid(input: String): Boolean {
        return DateUtils.isTextAnLocalDate(input)
    }
}

/**
 * Validates input for not empty String.
 */
object NotEmptyValidator : Validator {
    override fun isValid(input: String): Boolean {
        return input.isNotEmpty()
    }
}

/**
 * Validates input for number formatting.
 */
object SignedNumberValidator : Validator {
    override fun isValid(input: String): Boolean {
        return NumberUtils.isTextASignedNumber(input)
    }
}

/**
 * Validates input for account number formatting.
 */
object AccountNumberValidator : Validator {
    override fun isValid(input: String): Boolean {
        return NumberUtils.isTextAnAccountNumber(input)
    }
}

/**
 * Collection of utilities for working with [ValidatingTextInputLayout].
 */
object Validators {
    /**
     * Validates multiple inputs at once and returns `true` if all inputs
     * are valid.
     */
    @JvmStatic
    fun validate(layouts: List<ValidatingTextInputLayout>): Boolean {
        return layouts.all { it.validate() }
    }
}
