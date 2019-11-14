package io.github.zwieback.familyfinance.business.operation.service.calculator

import io.github.zwieback.familyfinance.core.model.OperationView

internal class CurrencyEntry(operation: OperationView) {

    val name: String = operation.currencyName
    private val id: Int = operation.currencyId

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CurrencyEntry

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}
