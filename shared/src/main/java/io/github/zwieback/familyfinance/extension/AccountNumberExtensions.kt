package io.github.zwieback.familyfinance.extension

import java.util.regex.Pattern

private val ACCOUNT_NUMBER_PATTERN = Pattern.compile("\\d{20}")

fun String.isAccountNumber(): Boolean {
    return ACCOUNT_NUMBER_PATTERN.matcher(this).matches()
}
