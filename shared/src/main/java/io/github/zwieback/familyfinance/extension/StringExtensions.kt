package io.github.zwieback.familyfinance.extension

import io.github.zwieback.familyfinance.constant.StringConstants.EMPTY

fun String.deleteLastChar(): String {
    return if (this.isNotEmpty()) {
        this.substring(0, this.length - 1)
    } else {
        EMPTY
    }
}

fun String.addChar(character: String): String {
    return if (this.isNotEmpty()) {
        this + character
    } else {
        character
    }
}

fun String.addUniqueChar(character: String): String {
    return if (this.isNotEmpty()) {
        if (this.contains(character)) {
            this
        } else {
            this + character
        }
    } else {
        character
    }
}
