package io.github.zwieback.familyfinance.business.chart.service.builder;

import com.annimon.stream.Stream;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class IdIndexMapStateBuilder {

    /**
     * key - entity id, value - bar index (started from 0)
     */
    private Map<Float, Float> idIndexMap;
    private Map<BigDecimal, Float> sumMap;

    public static IdIndexMapStateBuilder create() {
        return new IdIndexMapStateBuilder();
    }

    public IdIndexMapStateBuilder setSumMap(Map<BigDecimal, Float> sumMap) {
        this.sumMap = sumMap;
        this.idIndexMap = null;
        return this;
    }

    public Map<Float, Float> build() {
        if (sumMap == null) {
            throw new IllegalStateException("sumMap is not set");
        }
        if (idIndexMap == null) {
            idIndexMap = new HashMap<>();
            Stream.of(sumMap)
                    .sortBy(Map.Entry::getKey)
                    .forEachIndexed((index, entry) ->
                            idIndexMap.put(entry.getValue(), (float) index));
        }
        return idIndexMap;
    }
}
