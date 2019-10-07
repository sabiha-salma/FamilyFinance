package io.github.zwieback.familyfinance.business.chart.service.formatter;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.math.BigDecimal;

import io.github.zwieback.familyfinance.util.NumberUtils;

public class LocalizedValueFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        return NumberUtils.bigDecimalToString(BigDecimal.valueOf(value),
                NumberUtils.ACCOUNT_PLACES);
    }
}
