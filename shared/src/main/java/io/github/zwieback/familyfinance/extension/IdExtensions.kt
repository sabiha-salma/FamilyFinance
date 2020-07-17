package io.github.zwieback.familyfinance.extension

const val EMPTY_ID = -1

fun Int.isEmptyId(): Boolean {
    return this == EMPTY_ID
}

fun Int.isNotEmptyId(): Boolean {
    return !isEmptyId()
}

fun Int.toNullableId(): Int? {
    return if (this.isEmptyId()) null else this
}

fun Int?.toEmptyId(): Int {
    return this ?: EMPTY_ID
}
