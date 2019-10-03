package io.github.zwieback.familyfinance.util;

import androidx.core.view.ViewCompat;
import android.view.View;

public final class ViewUtils {

    public static boolean isLeftToRightLayoutDirection(View view) {
        return ViewCompat.getLayoutDirection(view) == ViewCompat.LAYOUT_DIRECTION_LTR;
    }

    private ViewUtils() {
    }
}
