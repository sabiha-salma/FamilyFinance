package io.github.zwieback.familyfinance.business.person.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityFromDestroyer
import io.github.zwieback.familyfinance.core.model.Operation
import io.github.zwieback.familyfinance.core.model.Person
import io.github.zwieback.familyfinance.core.model.type.OperationType
import io.requery.Persistable
import io.requery.query.Condition
import io.requery.reactivex.ReactiveEntityStore

internal class PersonFromExpenseOperationsDestroyer(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityFromDestroyer<Person, Operation>(context, data) {

    override val fromClass: Class<Operation>
        get() = Operation::class.java

    override val alertResourceId: Int
        get() = R.string.expense_operations_with_owner_exists

    override fun next(): EntityDestroyer<Person>? {
        return PersonFromIncomeOperationsDestroyer(context, data)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun getWhereCondition(person: Person): Condition<*, *> {
        return Operation.TYPE.eq(OperationType.EXPENSE_OPERATION)
            .and(Operation.OWNER_ID.eq(person.id))
    }
}
