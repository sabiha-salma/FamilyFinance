package io.github.zwieback.familyfinance.util;

import android.util.Pair;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.List;
import java.util.Map;

public final class CollectionUtils {

    /**
     * Swap unique keys and unique values in a map to avoid data loss.
     *
     * @param map source map
     * @param <K> source type of key
     * @param <V> source type of value
     * @return map with swapped keys and values
     */
    public static <K, V> Map<V, K> swapUniqueMap(Map<K, V> map) {
        return Stream.of(map)
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    /**
     * Swap keys and values of the map.
     *
     * @param map source map
     * @param <K> source type of key
     * @param <V> source type of value
     * @return list of pairs with swapped keys and values of map
     * @implNote return type is List due to possible data loss: the key must be
     * unique, but the value is not, and swap of the keys and the values does
     * not guarantee this
     */
    public static <K, V> List<Pair<V, K>> swapMap(Map<K, V> map) {
        return Stream.of(map)
                .map(entry -> Pair.create(entry.getValue(), entry.getKey()))
                .collect(Collectors.toList());
    }

    private CollectionUtils() {
    }
}
