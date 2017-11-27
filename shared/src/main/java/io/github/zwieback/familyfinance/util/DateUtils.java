package io.github.zwieback.familyfinance.util;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeParseException;
import org.threeten.bp.temporal.TemporalAdjusters;

import java.util.Calendar;

import static io.github.zwieback.familyfinance.util.NumberUtils.ID_AS_NULL;
import static io.github.zwieback.familyfinance.util.NumberUtils.isNullId;
import static io.github.zwieback.familyfinance.util.StringUtils.EMPTY;
import static io.github.zwieback.familyfinance.util.StringUtils.isTextEmpty;

public final class DateUtils {

    // bundle keys
    private static final String KEY_YEAR = "keyYear";
    private static final String KEY_MONTH = "keyMonth";
    private static final String KEY_DAY_OF_MONTH = "keyDayOfMonth";

    // workaround for month of calendar (start with 0)
    private static final int MONTH_OF_CALENDAR_INCREMENT = 1;

    public static boolean isTextAnLocalDate(@Nullable String text) {
        try {
            return stringToLocalDate(text) != null;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Nullable
    public static LocalDate stringToLocalDate(@Nullable String text) {
        if (isTextEmpty(text)) {
            return null;
        }
        return LocalDate.parse(text);
    }

    @NonNull
    public static String localDateToString(@Nullable LocalDate localDate) {
        if (localDate == null) {
            return EMPTY;
        }
        return localDate.toString();
    }

    @NonNull
    public static Calendar localDateToCalendar(@Nullable LocalDate localDate) {
        if (localDate == null) {
            return Calendar.getInstance();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(localDate.getYear(), localDate.getMonthValue() - MONTH_OF_CALENDAR_INCREMENT,
                localDate.getDayOfMonth());
        return calendar;
    }

    @NonNull
    public static LocalDate calendarDateToLocalDate(int calendarYear,
                                                    int calendarMonth,
                                                    int calendarDay) {
        return LocalDate.of(calendarYear, calendarMonth + MONTH_OF_CALENDAR_INCREMENT, calendarDay);
    }

    @NonNull
    public static LocalDate now() {
        return LocalDate.now();
    }

    @NonNull
    public static LocalDate startOfMonth() {
        return now().with(TemporalAdjusters.firstDayOfMonth());
    }

    @NonNull
    public static LocalDate endOfMonth() {
        return now().with(TemporalAdjusters.lastDayOfMonth());
    }

    public static void writeLocalDateToBundle(Bundle out, @NonNull LocalDate date) {
        out.putInt(KEY_YEAR, date.getYear());
        out.putInt(KEY_MONTH, date.getMonthValue());
        out.putInt(KEY_DAY_OF_MONTH, date.getDayOfMonth());
    }

    @NonNull
    public static LocalDate readLocalDateFromBundle(Bundle in) {
        int year = in.getInt(KEY_YEAR);
        int month = in.getInt(KEY_MONTH);
        int dayOfMonth = in.getInt(KEY_DAY_OF_MONTH);
        return LocalDate.of(year, month, dayOfMonth);
    }

    public static void writeLocalDateToIntent(@NonNull Intent out,
                                              @NonNull String name,
                                              @NonNull LocalDate date) {
        out.putExtra(name + KEY_YEAR, date.getYear());
        out.putExtra(name + KEY_MONTH, date.getMonthValue());
        out.putExtra(name + KEY_DAY_OF_MONTH, date.getDayOfMonth());
    }

    @NonNull
    public static LocalDate readLocalDateFromIntent(@NonNull Intent in, @NonNull String name) {
        LocalDate today = now();
        int year = in.getIntExtra(name + KEY_YEAR, today.getYear());
        int month = in.getIntExtra(name + KEY_MONTH, today.getMonthValue());
        int dayOfMonth = in.getIntExtra(name + KEY_DAY_OF_MONTH, today.getDayOfMonth());
        return LocalDate.of(year, month, dayOfMonth);
    }

    public static void writeLocalDateToParcel(Parcel out, @Nullable LocalDate date) {
        if (date == null) {
            out.writeInt(ID_AS_NULL);
            out.writeInt(ID_AS_NULL);
            out.writeInt(ID_AS_NULL);
        } else {
            out.writeInt(date.getYear());
            out.writeInt(date.getMonthValue());
            out.writeInt(date.getDayOfMonth());
        }
    }

    @Nullable
    public static LocalDate readLocalDateFromParcel(Parcel in) {
        int year = in.readInt();
        int month = in.readInt();
        int dayOfMonth = in.readInt();
        if (isNullId(year) || isNullId(month) || isNullId(dayOfMonth)) {
            return null;
        }
        return LocalDate.of(year, month, dayOfMonth);
    }

    private DateUtils() {
    }
}
