package io.github.zwieback.familyfinance.util

import android.content.Context
import io.github.zwieback.familyfinance.core.R

object ColorUtils {

    fun collectMaterialDesignColors(context: Context): List<Int> {
        val colors = context.resources.getIntArray(R.array.colors_md)
        return colors.toList()
    }
}
