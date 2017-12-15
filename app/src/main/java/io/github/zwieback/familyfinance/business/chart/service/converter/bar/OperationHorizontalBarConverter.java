package io.github.zwieback.familyfinance.business.chart.service.converter.bar;

import android.content.Context;
import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.zwieback.familyfinance.business.chart.service.converter.OperationConverter;
import io.github.zwieback.familyfinance.business.chart.service.converter.OperationSumConverter;
import io.github.zwieback.familyfinance.core.model.OperationView;
import io.github.zwieback.familyfinance.util.CollectionUtils;

public class OperationHorizontalBarConverter implements OperationConverter<BarEntry> {

    final OperationSumConverter sumConverter;
    /**
     * key - entity id, value - bar index (started from 0)
     */
    Map<Float, Float> idIndexMap;

    public OperationHorizontalBarConverter(@NonNull Context context) {
        this.sumConverter = new OperationSumConverter(context);
    }

    /**
     * Result.X - bar index.<br/>
     * Result.Y - sum of operations.
     *
     * @param operations key - entity id, value - list of operations
     * @return list of entries to display in horizontal bar chart
     */
    @Override
    public List<BarEntry> convertToEntries(Map<Float, List<OperationView>> operations) {
        Map<Float, BigDecimal> sumMap = sumConverter.convertToSumMap(operations);
        Map<BigDecimal, Float> swappedSumMap = CollectionUtils.swapMap(sumMap);
        buildIdIndexMap(swappedSumMap);

        return Stream.of(swappedSumMap)
                .map(entry -> {
                    Float barIndex = idIndexMap.get(entry.getValue());
                    Float sumOfOperations = entry.getKey().floatValue();
                    return new BarEntry(barIndex, sumOfOperations);
                })
                .sortBy(Entry::getX)
                .collect(Collectors.toList());
    }

    /**
     * Build {@link #idIndexMap}.
     *
     * @param swappedSumMap Map.Key - sum of operations, Map.Value - entity id
     */
    void buildIdIndexMap(Map<BigDecimal, Float> swappedSumMap) {
        idIndexMap = new HashMap<>();
        Stream.of(swappedSumMap)
                .sortBy(Map.Entry::getKey)
                .forEachIndexed((index, entry) -> idIndexMap.put(entry.getValue(), (float) index));
    }

    /**
     * Dirty solution, because the converter should not save the state.
     *
     * @return {@link #idIndexMap}
     * @implNote TODO rewrite this
     */
    public Map<Float, Float> getIdIndexMap() {
        return idIndexMap;
    }
}
