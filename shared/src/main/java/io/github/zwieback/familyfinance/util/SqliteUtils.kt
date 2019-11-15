package io.github.zwieback.familyfinance.util

import android.os.Build

object SqliteUtils {

    /**
     * CTEs are available only in SQLite 3.8.3 or later.
     *
     * See [Android SQLite database recursive query](https://stackoverflow.com/a/22093357/8035065)
     */
    fun cteSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }
}
