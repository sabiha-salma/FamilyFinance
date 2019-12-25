package io.github.zwieback.familyfinance.core.preference.custom

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import io.github.zwieback.familyfinance.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

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
    Preference.OnPreferenceChangeListener,
    CoroutineScope {

    private lateinit var rootJob: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob

    override fun onAttached() {
        super.onAttached()
        rootJob = Job()
        onPreferenceChangeListener = this
    }

    override fun onDetached() {
        rootJob.cancel()
        super.onDetached()
    }
}
