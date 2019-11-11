package io.github.zwieback.familyfinance.business.account.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityFromDestroyer
import io.github.zwieback.familyfinance.core.model.Account
import io.github.zwieback.familyfinance.core.model.Operation
import io.github.zwieback.familyfinance.core.model.type.OperationType.TRANSFER_EXPENSE_OPERATION
import io.github.zwieback.familyfinance.core.model.type.OperationType.TRANSFER_INCOME_OPERATION
import io.requery.Persistable
import io.requery.query.Condition
import io.requery.reactivex.ReactiveEntityStore

internal class AccountFromTransferOperationsDestroyer(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityFromDestroyer<Account, Operation>(context, data) {

    override val fromClass: Class<Operation>
        get() = Operation::class.java

    override val alertResourceId: Int
        get() = R.string.transfer_operations_with_account_exists

    override fun next(): EntityDestroyer<Account>? {
        return AccountFromPreferencesDestroyer(context, data)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun getWhereCondition(account: Account): Condition<*, *> {
        return Operation.TYPE.`in`(TRANSFER_EXPENSE_OPERATION, TRANSFER_INCOME_OPERATION)
            .and(Operation.ACCOUNT_ID.eq(account.id))
    }
}
