package io.github.zwieback.familyfinance.core.database.exception

class SQLException(message: String?, cause: Throwable) : RuntimeException(message, cause) {

    companion object {
        private const val serialVersionUID = 122863976549240999L
    }
}
