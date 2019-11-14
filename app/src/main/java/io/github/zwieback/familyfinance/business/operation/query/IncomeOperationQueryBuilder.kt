package io.github.zwieback.familyfinance.business.operation.query

import io.github.zwieback.familyfinance.core.model.type.OperationType
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class IncomeOperationQueryBuilder private constructor(data: ReactiveEntityStore<Persistable>) :
    OperationQueryBuilder<IncomeOperationQueryBuilder>(data) {

    companion object {
        fun create(data: ReactiveEntityStore<Persistable>): IncomeOperationQueryBuilder {
            return IncomeOperationQueryBuilder(data)
                .withTypes(listOf(OperationType.INCOME_OPERATION))
        }
    }
}
