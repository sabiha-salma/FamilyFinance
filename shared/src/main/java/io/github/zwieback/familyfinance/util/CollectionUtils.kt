package io.github.zwieback.familyfinance.util

import android.util.Pair
import com.annimon.stream.Collector
import com.annimon.stream.Collectors
import com.annimon.stream.Stream
import com.annimon.stream.function.BiConsumer
import com.annimon.stream.function.Function
import com.annimon.stream.function.Supplier
import java.util.*

object CollectionUtils {

    /**
     * Determine emptiness of array.
     *
     * @param array source array
     * @param <T>   any type of object
     * @return `true` if array is empty or null, `false` otherwise
     */
    @JvmStatic
    fun <T> isEmpty(array: Array<T>?): Boolean {
        return array.isNullOrEmpty()
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
    @JvmStatic
    fun <K, V> swapMap(map: Map<K, V>): List<Pair<V, K>> {
        return Stream.of(map)
            .map { entry -> Pair.create(entry.value, entry.key) }
            .collect(Collectors.toList())
    }

    /**
     * Copied from [Collectors.toMap] of stream lib version 1.1.9
     */
    @JvmStatic
    fun <T, K, V> toMap(
        keyMapper: Function<in T, out K>,
        valueMapper: Function<in T, out V>
    ): Collector<T, *, MutableMap<K, V>> {
        return toMap(keyMapper, valueMapper, hashMapSupplier())
    }

    /**
     * Copied from [Collectors.toMap] of stream lib version 1.1.9
     */
    @JvmStatic
    fun <T, K, V, M : MutableMap<K, V>> toMap(
        keyMapper: Function<in T, out K>,
        valueMapper: Function<in T, out V>,
        mapFactory: Supplier<M>
    ): Collector<T, *, M> {
        return CollectorsImpl(
            mapFactory,
            BiConsumer { map, t ->
                val key = keyMapper.apply(t)
                val value = valueMapper.apply(t)
                val oldValue = map[key]
                val newValue = oldValue ?: value
                if (newValue == null) {
                    map.remove(key)
                } else {
                    map.put(key, newValue)
                }
            }
        )
    }

    /**
     * Copied from [Collectors.hashMapSupplier] of stream lib version 1.1.9
     */
    @JvmStatic
    private fun <K, V> hashMapSupplier(): Supplier<MutableMap<K, V>> {
        return Supplier { HashMap<K, V>() }
    }

    /**
     * Copied from [Collectors.CollectorsImpl] of stream lib version 1.1.9
     */
    private class CollectorsImpl<T, A, R> @JvmOverloads constructor(
        private val supplier: Supplier<A>,
        private val accumulator: BiConsumer<A, T>,
        private val finisher: Function<A, R>? = null
    ) : Collector<T, A, R> {

        override fun supplier(): Supplier<A> {
            return supplier
        }

        override fun accumulator(): BiConsumer<A, T> {
            return accumulator
        }

        override fun finisher(): Function<A, R>? {
            return finisher
        }
    }
}
