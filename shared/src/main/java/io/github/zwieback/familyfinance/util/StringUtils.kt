package io.github.zwieback.familyfinance.util

object StringUtils {

    const val EMPTY = ""
    const val UNDEFINED = "undefined"
    const val QUESTION = "?"

    fun deleteLastChar(text: String?): String {
        return if (text?.isNotEmpty() == true) {
            text.substring(0, text.length - 1)
        } else {
            EMPTY
        }
    }

    fun addChar(text: String?, character: String): String {
        return if (text?.isNotEmpty() == true) {
            text + character
        } else {
            character
        }
    }

    fun addUniqueChar(text: String?, character: String): String {
        return if (text?.isNotEmpty() == true) {
            if (text.contains(character)) {
                text
            } else {
                text + character
            }
        } else {
            character
        }
    }
}
