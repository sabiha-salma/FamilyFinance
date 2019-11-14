package io.github.zwieback.familyfinance.business.operation.activity.helper

import io.github.zwieback.familyfinance.business.operation.activity.exception.IllegalOperationTypeException
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.core.model.type.OperationType

internal object TransferOperationQualifier {

    fun determineTransferExpenseOperationId(operation: OperationView): Int {
        return when {
            operation.type === OperationType.TRANSFER_EXPENSE_OPERATION ->
                operation.id
            operation.type === OperationType.TRANSFER_INCOME_OPERATION ->
                operation.linkedTransferOperationId ?: error("No linked income operation")
            else ->
                throw IllegalOperationTypeException.notTransferOperation(operation)
        }
    }

    fun determineTransferIncomeOperationId(operation: OperationView): Int {
        return when {
            operation.type === OperationType.TRANSFER_EXPENSE_OPERATION ->
                operation.linkedTransferOperationId ?: error("No linked expense operation")
            operation.type === OperationType.TRANSFER_INCOME_OPERATION ->
                operation.id
            else ->
                throw IllegalOperationTypeException.notTransferOperation(operation)
        }
    }
}
