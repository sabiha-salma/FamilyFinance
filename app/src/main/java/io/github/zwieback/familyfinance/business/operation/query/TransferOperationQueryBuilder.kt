package io.github.zwieback.familyfinance.business.operation.query

import io.github.zwieback.familyfinance.core.model.type.OperationType.TRANSFER_EXPENSE_OPERATION
import io.github.zwieback.familyfinance.core.model.type.OperationType.TRANSFER_INCOME_OPERATION
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class TransferOperationQueryBuilder private constructor(data: ReactiveEntityStore<Persistable>) :
    OperationQueryBuilder<TransferOperationQueryBuilder>(data) {

    companion object {
        fun create(data: ReactiveEntityStore<Persistable>): TransferOperationQueryBuilder {
            return TransferOperationQueryBuilder(data)
                .withTypes(listOf(TRANSFER_EXPENSE_OPERATION, TRANSFER_INCOME_OPERATION))
        }
    }
}
