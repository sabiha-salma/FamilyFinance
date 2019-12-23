package io.github.zwieback.familyfinance.core.preference.config

import android.content.Context
import com.afollestad.rxkprefs.Pref
import com.afollestad.rxkprefs.RxkPrefs
import com.afollestad.rxkprefs.rxkPrefs

class InterfacePrefs private constructor(context: Context) {

    private val prefs: RxkPrefs = rxkPrefs(context, FILE_NAME)

    private val showBalanceOnOperationScreensPref: Pref<Boolean>
        get() = prefs.boolean(
            SHOW_BALANCE_ON_OPERATION_SCREENS,
            SHOW_BALANCE_ON_OPERATION_SCREENS_DEFAULT
        )

    var isShowBalanceOnOperationScreens
        get() = showBalanceOnOperationScreensPref.get()
        set(value) = showBalanceOnOperationScreensPref.set(value)

    companion object {
        const val FILE_NAME = "interface_prefs"
        // region preferences
        private const val SHOW_BALANCE_ON_OPERATION_SCREENS = "showBalanceOnOperationScreens"
        private const val SHOW_BALANCE_ON_OPERATION_SCREENS_DEFAULT = true
        // endregion preferences

        fun with(context: Context) = InterfacePrefs(context)
    }
}
