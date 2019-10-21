package io.github.zwieback.familyfinance.core.preference.custom

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import androidx.annotation.StringRes
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.Preference
import androidx.preference.R
import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.github.zwieback.familyfinance.util.NumberUtils.ID_AS_NULL
import io.github.zwieback.familyfinance.util.StringUtils

abstract class EntityActivityResultPreference<E : IBaseEntity> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = TypedArrayUtils.getAttr(
        context,
        R.attr.preferenceStyle,
        android.R.attr.preferenceStyle
    ),
    defStyleRes: Int = 0
) : ActivityResultPreference(context, attrs, defStyleAttr, defStyleRes) {

    protected abstract val resultName: String

    protected abstract val savedEntityId: Int

    protected abstract val entityClass: Class<E>

    @get:StringRes
    protected abstract val preferenceTitleRes: Int

    override fun init(context: Context) {
        super.init(context)
        callChangeListener(savedEntityId)
    }

    override fun onSuccessResult(resultIntent: Intent) {
        val entityId = extractOutputId(resultIntent, resultName)
        saveEntityId(entityId)
        callChangeListener(entityId)
    }

    protected abstract fun saveEntityId(entityId: Int)

    protected abstract fun getEntityName(entity: E): String

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        val newEntityId = newValue as Int
        activity.loadEntity(entityClass, newEntityId,
            { foundEntity ->
                val entityTitle = activity.getString(preferenceTitleRes, getEntityName(foundEntity))
                preference.title = entityTitle
            },
            {
                val entityTitle = activity.getString(preferenceTitleRes, StringUtils.UNDEFINED)
                preference.title = entityTitle
            }
        )
        return true
    }

    private fun extractOutputId(resultIntent: Intent, name: String): Int {
        return resultIntent.getIntExtra(name, ID_AS_NULL)
    }
}
