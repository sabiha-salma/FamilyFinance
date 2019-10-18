package io.github.zwieback.familyfinance.util

import android.content.res.Configuration
import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import java.util.*

object ConfigurationUtils {

    /**
     * See [Android get current Locale, not default](https://stackoverflow.com/a/49256800/8035065)
     *
     * @return system locale
     */
    @JvmStatic
    val systemLocale: Locale
        get() {
            val configuration = Resources.getSystem().configuration
            val locales = ConfigurationCompat.getLocales(configuration)
            check(!locales.isEmpty) { "Locale list is empty" }
            return locales.get(0)
        }

    /**
     * Overall orientation of the screen.
     *
     * @return [Configuration.ORIENTATION_LANDSCAPE] or [Configuration.ORIENTATION_PORTRAIT]
     */
    @JvmStatic
    val orientation: Int
        get() = Resources.getSystem().configuration.orientation
}
