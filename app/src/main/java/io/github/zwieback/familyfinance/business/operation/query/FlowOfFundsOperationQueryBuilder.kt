package io.github.zwieback.familyfinance.business.operation.query

import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class FlowOfFundsOperationQueryBuilder private constructor(data: ReactiveEntityStore<Persistable>) :
    OperationQueryBuilder<FlowOfFundsOperationQueryBuilder>(data) {

    companion object {
        fun create(data: ReactiveEntityStore<Persistable>): FlowOfFundsOperationQueryBuilder {
            return FlowOfFundsOperationQueryBuilder(data)
        }
    }
}
