package io.github.zwieback.familyfinance.util

import android.util.Pair

object CollectionUtils {

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
    @JvmStatic
    fun <K, V> swapMap(map: Map<K, V>): List<Pair<V, K>> {
        return map.map { entry -> Pair.create(entry.value, entry.key) }
    }
}
