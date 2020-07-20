package io.github.zwieback.familyfinance.extension

import io.github.zwieback.familyfinance.constant.IdConstants.EMPTY_ID

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
