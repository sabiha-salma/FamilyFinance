package io.github.zwieback.familyfinance.business.operation.lifecycle.creator

import android.content.Context
import io.github.zwieback.familyfinance.core.lifecycle.creator.EntityCreator
import io.github.zwieback.familyfinance.core.model.Operation
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class OperationCreator(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityCreator<Operation>(context, data) {

    override fun buildEntities(): Iterable<Operation> {
        return sortedSetOf(this)
    }

    override fun compare(left: Operation, right: Operation): Int {
        return left.date.compareTo(right.date)
    }
}
