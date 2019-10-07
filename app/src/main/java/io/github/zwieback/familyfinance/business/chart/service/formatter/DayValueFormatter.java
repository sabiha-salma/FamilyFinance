package io.github.zwieback.familyfinance.business.chart.service.formatter;

import com.github.mikephil.charting.formatter.ValueFormatter;

import org.threeten.bp.LocalDate;

import io.github.zwieback.familyfinance.util.DateUtils;

public class DayValueFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float daysFromEpoch) {
        LocalDate day = determineCorrectDay(daysFromEpoch);
        return DateUtils.localDateToString(day);
    }

    private static LocalDate determineCorrectDay(float daysFromEpoch) {
        return DateUtils.epochDayToLocalDate((long) daysFromEpoch);
    }
}
