package io.github.zwieback.familyfinance.core.preference.custom

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.CallSuper
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import io.github.zwieback.familyfinance.R

abstract class BooleanPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = TypedArrayUtils.getAttr(
        context,
        R.attr.checkBoxPreferenceStyle,
        android.R.attr.checkBoxPreferenceStyle
    ),
    defStyleRes: Int = 0
) : CheckBoxPreference(context, attrs, defStyleAttr, defStyleRes),
    Preference.OnPreferenceChangeListener {

    init {
        init(context)
    }

    @CallSuper
    protected open fun init(context: Context) {
        onPreferenceChangeListener = this
    }
}
