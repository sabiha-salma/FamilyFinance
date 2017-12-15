package io.github.zwieback.familyfinance.util;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.Map;

public final class CollectionUtils {

    /**
     * Swap keys and values in the map.
     *
     * @param map source map
     * @param <K> source type of key
     * @param <V> source type of value
     * @return map with swapped keys and values
     */
    public static <K, V> Map<V, K> swapMap(Map<K, V> map) {
        return Stream.of(map)
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    private CollectionUtils() {
    }
}
