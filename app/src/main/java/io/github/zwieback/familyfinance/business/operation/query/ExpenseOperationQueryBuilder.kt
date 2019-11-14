package io.github.zwieback.familyfinance.business.operation.query

import io.github.zwieback.familyfinance.core.model.type.OperationType
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class ExpenseOperationQueryBuilder private constructor(data: ReactiveEntityStore<Persistable>) :
    OperationQueryBuilder<ExpenseOperationQueryBuilder>(data) {

    companion object {
        fun create(data: ReactiveEntityStore<Persistable>): ExpenseOperationQueryBuilder {
            return ExpenseOperationQueryBuilder(data)
                .withTypes(listOf(OperationType.EXPENSE_OPERATION))
        }
    }
}
