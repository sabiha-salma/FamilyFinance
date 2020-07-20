package io.github.zwieback.familyfinance.extension

/**
 * Swap keys and values of the map.
 *
 * Return type is List due to possible data loss: the key must be
 * unique, but the value is not, and swap of the keys and the values does
 * not guarantee this
 *
 * @param <K> source type of key
 * @param <V> source type of value
 * @return list of pairs with swapped keys and values of map
 */
fun <K, V> Map<K, V>.swapKeysAndValues(): List<Pair<V, K>> {
    return this.map { entry -> entry.value to entry.key }
}
