package io.github.zwieback.familyfinance.business.operation.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityForceDestroyer
import io.github.zwieback.familyfinance.core.model.Operation
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions.emptyConsumer
import io.requery.Persistable
import io.requery.meta.QueryAttribute
import io.requery.reactivex.ReactiveEntityStore

class TransferOperationForceDestroyer(context: Context, data: ReactiveEntityStore<Persistable>) :
    EntityForceDestroyer<Operation>(context, data) {

    override val entityClass: Class<Operation>
        get() = Operation::class.java

    override val idAttribute: QueryAttribute<Operation, Int>
        get() = Operation.ID

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun destroy(operation: Operation, terminalConsumer: Consumer<Int>) {
        super.destroy((operation.linkedTransferOperation as Operation), emptyConsumer())
        super.destroy(operation, terminalConsumer)
    }
}
