package io.github.zwieback.familyfinance.business.preference.custom.interface_

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.Preference
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.preference.config.InterfacePrefs
import io.github.zwieback.familyfinance.core.preference.custom.BooleanPreference

class ShowBalanceOnOperationScreensPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = TypedArrayUtils.getAttr(
        context,
        R.attr.checkBoxPreferenceStyle,
        android.R.attr.checkBoxPreferenceStyle
    ),
    defStyleRes: Int = 0
) : BooleanPreference(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var interfacePrefs: InterfacePrefs

    private var isShowBalance: Boolean
        get() = interfacePrefs.isShowBalanceOnOperationScreens
        set(showBalance) {
            interfacePrefs.isShowBalanceOnOperationScreens = showBalance
        }

    override fun init(context: Context) {
        super.init(context)
        interfacePrefs = InterfacePrefs.with(context)
        callChangeListener(isShowBalance)
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        if (newValue is Boolean) {
            isShowBalance = newValue
            return true
        }
        return false
    }
}
