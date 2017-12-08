package io.github.zwieback.familyfinance.business.chart.service.converter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.github.zwieback.familyfinance.business.operation.service.calculator.OperationCalculator;
import io.github.zwieback.familyfinance.core.model.OperationView;

public class OperationConverter {

    private final OperationCalculator calculator;

    public OperationConverter(@NonNull Context context) {
        this.calculator = new OperationCalculator(context);
    }

    /**
     * Convert operations into the list of {@link BarEntry}.
     * <p>
     * Note: sortBy(Entry::getX) is extremely important!
     *
     * @param groupedOperations operations that grouped by one of the OperationGrouper
     * @return list of entries to display in bar chart
     * @see <a href="https://github.com/PhilJay/MPAndroidChart/issues/983#issuecomment-152299035">
     * setVisibleXRangeMaximum not working as expected</a>
     * @see <a href="https://github.com/PhilJay/MPAndroidChart/wiki/Setting-Data#the-order-of-entries">
     * The order of entries</a>
     */
    public List<BarEntry> convertToBarEntries(Map<Float, List<OperationView>> groupedOperations) {
        Map<Float, BigDecimal> valueMap = convertToValueMap(groupedOperations);
        return Stream.of(valueMap)
                .map(operationEntry -> new BarEntry(operationEntry.getKey(),
                        operationEntry.getValue().floatValue()))
                .sortBy(Entry::getX)
                .collect(Collectors.toList());
    }

    private Map<Float, BigDecimal>
    convertToValueMap(Map<Float, List<OperationView>> groupedOperations) {
        return Stream.of(groupedOperations)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> calculator.calculateSum(entry.getValue())));
    }
}
