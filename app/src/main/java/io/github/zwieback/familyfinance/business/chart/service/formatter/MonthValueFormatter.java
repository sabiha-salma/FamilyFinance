package io.github.zwieback.familyfinance.business.chart.service.formatter;

import com.github.mikephil.charting.formatter.ValueFormatter;

import org.threeten.bp.LocalDate;

import io.github.zwieback.familyfinance.util.DateUtils;

public class MonthValueFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float monthsFromEpoch) {
        LocalDate month = determineCorrectMonth(monthsFromEpoch);
        return DateUtils.localDateToString(month, DateUtils.getISO_LOCAL_MONTH());
    }

    private static LocalDate determineCorrectMonth(float monthsFromEpoch) {
        return DateUtils.epochMonthToLocalDate((long) monthsFromEpoch);
    }
}
