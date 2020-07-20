package io.github.zwieback.familyfinance.extension

fun String.isInt(): Boolean {
    return this.toIntOrNull() != null
}

fun Int?.toStringOrEmpty(): String {
    return this?.toString().orEmpty()
}
