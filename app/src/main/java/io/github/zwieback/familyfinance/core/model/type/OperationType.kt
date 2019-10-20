package io.github.zwieback.familyfinance.core.model.type

enum class OperationType {
    EXPENSE_OPERATION,
    INCOME_OPERATION,
    TRANSFER_EXPENSE_OPERATION,
    TRANSFER_INCOME_OPERATION;

    companion object {

        val incomeTypes = listOf(
            INCOME_OPERATION,
            TRANSFER_INCOME_OPERATION
        )
        val expenseTypes = listOf(
            EXPENSE_OPERATION,
            TRANSFER_EXPENSE_OPERATION
        )
    }
}
