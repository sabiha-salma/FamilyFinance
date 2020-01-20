package io.github.zwieback.familyfinance.core.preference.custom

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.Preference
import androidx.preference.SeekBarPreference
import io.github.zwieback.familyfinance.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class SeekBarPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = TypedArrayUtils.getAttr(
        context,
        R.attr.seekBarPreferenceStyle,
        android.R.attr.seekBarStyle
    ),
    defStyleRes: Int = 0
) : SeekBarPreference(context, attrs, defStyleAttr, defStyleRes),
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
