package io.github.zwieback.familyfinance.business.chart.service.formatter;

import com.github.mikephil.charting.formatter.ValueFormatter;

import org.threeten.bp.LocalDate;

import io.github.zwieback.familyfinance.util.DateUtils;

public class QuarterValueFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float quartersFromEpoch) {
        LocalDate quarter = determineCorrectQuarter(quartersFromEpoch);
        return DateUtils.localDateToString(quarter, DateUtils.ISO_LOCAL_QUARTER);
    }

    private static LocalDate determineCorrectQuarter(float quartersFromEpoch) {
        return DateUtils.epochQuarterToLocalDate((long) quartersFromEpoch);
    }
}
