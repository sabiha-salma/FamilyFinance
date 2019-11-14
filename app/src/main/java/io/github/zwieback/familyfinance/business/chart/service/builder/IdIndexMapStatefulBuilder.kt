package io.github.zwieback.familyfinance.business.chart.service.builder

import android.util.Pair
import java.math.BigDecimal

class IdIndexMapStatefulBuilder {

    private lateinit var sumMap: List<Pair<BigDecimal, Float>>
    /**
     * key - unique entity id, value - unique bar index (started from 0)
     */
    private var idIndexMap: MutableMap<Float, Float> = mutableMapOf()

    fun withSumMap(sumMap: List<Pair<BigDecimal, Float>>): IdIndexMapStatefulBuilder {
        return apply {
            this.sumMap = sumMap
            this.idIndexMap.clear()
        }
    }

    fun build(): Map<Float, Float> {
        if (idIndexMap.isEmpty()) {
            sumMap
                .asSequence()
                .sortedBy { pair -> pair.first }
                .forEachIndexed { index, pair ->
                    idIndexMap[pair.second] = index.toFloat()
                }
        }
        return idIndexMap
    }

    companion object {
        fun create(): IdIndexMapStatefulBuilder {
            return IdIndexMapStatefulBuilder()
        }
    }
}
