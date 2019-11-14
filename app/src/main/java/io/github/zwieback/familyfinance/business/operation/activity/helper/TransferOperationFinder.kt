package io.github.zwieback.familyfinance.business.operation.activity.helper

import io.github.zwieback.familyfinance.business.operation.activity.helper.TransferOperationQualifier.determineTransferExpenseOperationId
import io.github.zwieback.familyfinance.business.operation.activity.helper.TransferOperationQualifier.determineTransferIncomeOperationId
import io.github.zwieback.familyfinance.core.model.OperationView
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

internal object TransferOperationFinder {

    fun findExpenseOperation(
        data: ReactiveEntityStore<Persistable>,
        operation: OperationView
    ): OperationView {
        val expenseOperationId = determineTransferExpenseOperationId(operation)
        return if (expenseOperationId == operation.id) {
            operation
        } else {
            findOperationById(data, expenseOperationId)
        }
    }

    fun findIncomeOperation(
        data: ReactiveEntityStore<Persistable>,
        operation: OperationView
    ): OperationView {
        val incomeOperationId = determineTransferIncomeOperationId(operation)
        return if (incomeOperationId == operation.id) {
            operation
        } else {
            findOperationById(data, incomeOperationId)
        }
    }

    private fun findOperationById(
        data: ReactiveEntityStore<Persistable>,
        operationId: Int
    ): OperationView {
        return data.select(OperationView::class.java)
            .where(OperationView.ID.eq(operationId))
            .get()
            .first()
    }
}
