package io.github.zwieback.familyfinance.extension

import java.util.regex.Pattern

private val SIGNED_NUMBER_PATTERN = Pattern.compile("\\d+")

fun String.isSignedNumber(): Boolean {
    return SIGNED_NUMBER_PATTERN.matcher(this).matches()
}
