package io.github.zwieback.familyfinance.util;

import android.content.Intent;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import static io.github.zwieback.familyfinance.util.StringUtils.EMPTY;
import static io.github.zwieback.familyfinance.util.StringUtils.isTextEmpty;

/**
 * todo refactor this class and another utils
 */
public final class NumberUtils {
//    private static final String TAG = "NumberUtils";
    public static final int ID_AS_NULL = -1;
    public static final int ACCOUNT_PLACES = 2;

    public static final long UI_DEBOUNCE_TIMEOUT = 500L;

    private static final int DEFAULT_GROUPING_SIZE = 3;
    private static final DecimalFormat bigDecimalFormat;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(ConfigurationUtils.getSystemLocale());
        // see https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
        // and https://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html
        bigDecimalFormat = new DecimalFormat("0.00######", symbols);
        bigDecimalFormat.setParseBigDecimal(true);
        if (!bigDecimalFormat.isGroupingUsed()) {
            bigDecimalFormat.setGroupingUsed(true);
        }
        if (bigDecimalFormat.getGroupingSize() < DEFAULT_GROUPING_SIZE) {
            bigDecimalFormat.setGroupingSize(DEFAULT_GROUPING_SIZE);
        }
    }

    public static boolean isNullId(int id) {
        return id == ID_AS_NULL;
    }

    public static boolean nonNullId(int id) {
        return !isNullId(id);
    }

    @Nullable
    public static Integer intToIntegerId(int id) {
        return isNullId(id) ? null : id;
    }

    public static int integerToIntId(@Nullable Integer id) {
        return id == null ? ID_AS_NULL : id;
    }

    public static boolean isTextAnInteger(String text) {
        try {
            stringToInt(text);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isTextAnBigDecimal(String text) {
        try {
            return stringToBigDecimal(text) != null;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int stringToInt(String text) {
        return Integer.parseInt(text);
    }

    @Nullable
    public static BigDecimal stringToBigDecimal(@Nullable String text) {
        if (isTextEmpty(text)) {
            return null;
        }
        try {
            return (BigDecimal) bigDecimalFormat.parse(text);
        } catch (ParseException e) {
            throw new NumberFormatException(e.getMessage());
        }
    }

    @NonNull
    public static String intToString(int number) {
        return String.valueOf(number);
    }

    @NonNull
    public static String bigDecimalToString(@Nullable BigDecimal number) {
        if (number == null) {
            return EMPTY;
        }
        return bigDecimalFormat.format(number);
    }

    @NonNull
    public static String bigDecimalToString(@Nullable BigDecimal number, int places) {
        if (number == null) {
            return EMPTY;
        }

//        Log.d(TAG, "bigDecimalToString: source value = " + bigDecimalFormat.format(number));
//        for (RoundingMode roundingMode : RoundingMode.values()) {
//            if (roundingMode.equals(RoundingMode.UNNECESSARY)) continue;
//            Log.d(TAG, "bigDecimalToString: roundingMode = " + roundingMode + "; value = " + bigDecimalFormat.format(number.setScale(places, roundingMode)));
//        }

        BigDecimal value = number.setScale(places, RoundingMode.HALF_EVEN);
        return bigDecimalFormat.format(value);
    }

    public static void writeBigDecimalToParcel(Parcel out, @Nullable BigDecimal value) {
        if (value == null) {
            out.writeString(EMPTY);
        } else {
            out.writeString(bigDecimalToString(value));
        }
    }

    @Nullable
    public static BigDecimal readBigDecimalFromParcel(Parcel in) {
        return stringToBigDecimal(in.readString());
    }

    public static void writeBigDecimalToIntent(@NonNull Intent out,
                                               @NonNull String name,
                                               @Nullable BigDecimal value) {
        out.putExtra(name, bigDecimalToString(value));
    }

    @Nullable
    public static BigDecimal readBigDecimalFromIntent(@NonNull Intent in, @NonNull String name) {
        String value = in.getStringExtra(name);
        return stringToBigDecimal(value);
    }

    public static char getDecimalSeparator() {
        return bigDecimalFormat.getDecimalFormatSymbols().getDecimalSeparator();
    }

    private NumberUtils() {
    }
}
