package io.github.zwieback.familyfinance.business.chart.service.formatter;

import com.github.mikephil.charting.formatter.ValueFormatter;

import org.threeten.bp.LocalDate;

import io.github.zwieback.familyfinance.util.DateUtils;

public class WeekValueFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float weeksFromEpoch) {
        LocalDate month = determineCorrectWeek(weeksFromEpoch);
        return DateUtils.localDateToString(month, DateUtils.ISO_LOCAL_WEEK);
    }

    private static LocalDate determineCorrectWeek(float weeksFromEpoch) {
        return DateUtils.epochWeekToLocalDate((long) weeksFromEpoch);
    }
}
