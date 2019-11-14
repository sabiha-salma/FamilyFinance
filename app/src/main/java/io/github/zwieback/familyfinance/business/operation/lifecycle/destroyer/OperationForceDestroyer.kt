package io.github.zwieback.familyfinance.business.operation.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityForceDestroyer
import io.github.zwieback.familyfinance.core.model.Operation
import io.requery.Persistable
import io.requery.meta.QueryAttribute
import io.requery.reactivex.ReactiveEntityStore

class OperationForceDestroyer(context: Context, data: ReactiveEntityStore<Persistable>) :
    EntityForceDestroyer<Operation>(context, data) {

    override val entityClass: Class<Operation>
        get() = Operation::class.java

    override val idAttribute: QueryAttribute<Operation, Int>
        get() = Operation.ID
}
