package io.github.zwieback.familyfinance.core.adapter

import android.content.Context
import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.mikepenz.iconics.Iconics
import com.mikepenz.iconics.IconicsColor
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.utils.clearedIconName
import com.mikepenz.iconics.utils.iconPrefix
import io.github.zwieback.familyfinance.BuildConfig
import io.github.zwieback.familyfinance.core.model.IBaseEntity

abstract class EntityProvider<E : IBaseEntity> protected constructor(
    protected val context: Context
) {

    abstract fun provideDefaultIcon(entity: E): IIcon

    @ColorRes
    abstract fun provideDefaultIconColor(entity: E): Int

    @ColorInt
    open fun provideTextColor(entity: E): Int {
        throw UnsupportedOperationException()
    }

    fun setupIcon(drawable: IconicsDrawable?, entity: E) {
        if (drawable == null) {
            return
        }
        val icon = provideIcon(entity) ?: provideDefaultIcon(entity)
        drawable.icon(icon)
        drawable.color(IconicsColor.colorRes(provideDefaultIconColor(entity)))
    }

    /**
     * Returns the icon of the entity, if it is defined.
     *
     * @see [IconicsDrawable.icon]
     */
    private fun provideIcon(entity: E): IIcon? {
        val iconName = entity.iconName
        if (!iconName.isNullOrEmpty()) {
            val fontName = iconName.iconPrefix
            val font = Iconics.findFont(fontName, context)
            font?.let {
                val clearedIconName = iconName.clearedIconName
                return try {
                    font.getIcon(clearedIconName)
                } catch (e: IllegalArgumentException) {
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, "Icon $iconName not found")
                    }
                    null
                }
            }
        }
        return null
    }

    companion object {
        private const val TAG = "EntityProvider"
    }
}
