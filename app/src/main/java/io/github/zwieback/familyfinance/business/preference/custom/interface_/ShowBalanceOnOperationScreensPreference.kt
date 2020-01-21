package io.github.zwieback.familyfinance.business.preference.custom.interface_

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.Preference
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.preference.config.InterfacePrefs
import io.github.zwieback.familyfinance.core.preference.custom.BooleanPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

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

    override fun onAttached() {
        super.onAttached()
        runBlocking(Dispatchers.IO) {
            interfacePrefs = InterfacePrefs.with(context)
        }
        launch {
            val showBalance = withContext(Dispatchers.IO) {
                interfacePrefs.isShowBalanceOnOperationScreens
            }
            callChangeListener(showBalance)
        }
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        if (newValue is Boolean) {
            runBlocking(Dispatchers.IO) {
                interfacePrefs.isShowBalanceOnOperationScreens = newValue
            }
            return true
        }
        return false
    }
}
