package io.github.zwieback.familyfinance.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;

public final class StringUtils {

    public static final String EMPTY = "";
    public static final String UNDEFINED = "undefined";

    public static boolean isTextEmpty(@Nullable CharSequence text) {
        return TextUtils.isEmpty(text);
    }

    public static boolean isTextNotEmpty(@Nullable CharSequence text) {
        return !isTextEmpty(text);
    }

    private StringUtils() {
    }
}
