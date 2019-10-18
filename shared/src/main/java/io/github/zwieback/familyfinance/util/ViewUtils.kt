package io.github.zwieback.familyfinance.util

import android.view.View
import androidx.core.view.ViewCompat

object ViewUtils {

    @JvmStatic
    fun isLeftToRightLayoutDirection(view: View): Boolean {
        return ViewCompat.getLayoutDirection(view) == ViewCompat.LAYOUT_DIRECTION_LTR
    }
}
