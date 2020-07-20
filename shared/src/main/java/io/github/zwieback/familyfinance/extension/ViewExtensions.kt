package io.github.zwieback.familyfinance.extension

import android.view.View
import androidx.core.view.ViewCompat

fun View.isLeftToRightLayoutDirection(): Boolean {
    return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR
}
