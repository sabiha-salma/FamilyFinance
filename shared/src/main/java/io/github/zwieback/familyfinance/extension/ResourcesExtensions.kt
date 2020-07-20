package io.github.zwieback.familyfinance.extension

import android.content.Context
import io.github.zwieback.familyfinance.core.R

fun Context.collectMaterialDesignColors(): List<Int> {
    val colors = this.resources.getIntArray(R.array.colors_md)
    return colors.toList()
}
