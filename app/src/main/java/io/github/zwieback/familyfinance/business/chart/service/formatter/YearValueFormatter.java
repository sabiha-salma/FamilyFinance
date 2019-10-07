package io.github.zwieback.familyfinance.business.chart.service.formatter;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class YearValueFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float year) {
        return String.valueOf((int) year);
    }
}
