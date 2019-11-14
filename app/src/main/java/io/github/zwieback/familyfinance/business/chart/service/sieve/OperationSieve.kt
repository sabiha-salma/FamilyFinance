package io.github.zwieback.familyfinance.business.chart.service.sieve

import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.core.model.type.OperationType

class OperationSieve {

    fun filterByTypes(
        operations: Map<Float, List<OperationView>>,
        types: List<OperationType>
    ): Map<Float, List<OperationView>> {
        return operations.mapValues { entry -> internalFilterByType(entry.value, types) }
    }

    private fun internalFilterByType(
        operations: List<OperationView>,
        types: List<OperationType>
    ): List<OperationView> {
        return operations.filter { operation -> operation.type in types }
    }
}
