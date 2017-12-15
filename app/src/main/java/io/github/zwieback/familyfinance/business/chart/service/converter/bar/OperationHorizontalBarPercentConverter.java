package io.github.zwieback.familyfinance.business.chart.service.converter.bar;

import android.content.Context;
import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.github.zwieback.familyfinance.business.chart.service.builder.IdIndexMapStateBuilder;
import io.github.zwieback.familyfinance.core.model.OperationView;
import io.github.zwieback.familyfinance.util.CollectionUtils;

public class OperationHorizontalBarPercentConverter extends OperationHorizontalBarConverter {

    private static final BigDecimal MAX_PERCENT = BigDecimal.valueOf(100);

    public OperationHorizontalBarPercentConverter(@NonNull Context context,
                                                  @NonNull IdIndexMapStateBuilder builder) {
        super(context, builder);
    }

    /**
     * Result.X - bar index.<br/>
     * Result.Y - sum of operations in percent.
     *
     * @param operations key - entity id, value - list of operations
     * @return list of entries to display in horizontal bar chart
     */
    @Override
    public List<BarEntry> convertToEntries(Map<Float, List<OperationView>> operations) {
        Map<Float, BigDecimal> sumMap = sumConverter.convertToSumMap(operations);
        Map<BigDecimal, Float> swappedSumMap = CollectionUtils.swapMap(sumMap);
        Map<BigDecimal, Float> percentSumMap = convertToPercentMap(swappedSumMap);
        Map<Float, Float> idIndexMap = builder.setSumMap(percentSumMap).build();

        return Stream.of(percentSumMap)
                .map(entry -> {
                    Float barIndex = idIndexMap.get(entry.getValue());
                    Float sumOfOperationsInPercent = entry.getKey().floatValue();
                    return new BarEntry(barIndex, sumOfOperationsInPercent);
                })
                .sortBy(Entry::getX)
                .collect(Collectors.toList());
    }

    private Map<BigDecimal, Float> convertToPercentMap(Map<BigDecimal, Float> swappedSumMap) {
        BigDecimal totalSum = calculateTotalSum(swappedSumMap.keySet());
        return Stream.of(swappedSumMap)
                .collect(Collectors.toMap(
                        entry -> calculatePercent(totalSum, entry.getKey()),
                        Map.Entry::getValue
                ));
    }

    private BigDecimal calculatePercent(BigDecimal totalSum, BigDecimal sumOfOperations) {
        return sumOfOperations.multiply(MAX_PERCENT).divide(totalSum, RoundingMode.DOWN);
    }

    private BigDecimal calculateTotalSum(Collection<BigDecimal> collectionWithSums) {
        return Stream.of(collectionWithSums)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
