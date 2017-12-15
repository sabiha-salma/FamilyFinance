package io.github.zwieback.familyfinance.business.chart.service.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Map;

import io.github.zwieback.familyfinance.util.StringUtils;

public class IndexNameMapFormatter implements IAxisValueFormatter {

    private final Map<Float, String> indexNameMap;

    public IndexNameMapFormatter(Map<Float, String> indexNameMap) {
        this.indexNameMap = indexNameMap;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (indexNameMap.containsKey(value)) {
            return indexNameMap.get(value);
        }
        return StringUtils.EMPTY;
    }
}
