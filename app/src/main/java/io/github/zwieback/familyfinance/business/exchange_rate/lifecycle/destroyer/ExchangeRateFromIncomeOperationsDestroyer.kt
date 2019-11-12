package io.github.zwieback.familyfinance.business.exchange_rate.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityFromDestroyer
import io.github.zwieback.familyfinance.core.model.ExchangeRate
import io.github.zwieback.familyfinance.core.model.Operation
import io.github.zwieback.familyfinance.core.model.type.OperationType
import io.requery.Persistable
import io.requery.query.Condition
import io.requery.reactivex.ReactiveEntityStore

internal class ExchangeRateFromIncomeOperationsDestroyer(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityFromDestroyer<ExchangeRate, Operation>(context, data) {

    override val fromClass: Class<Operation>
        get() = Operation::class.java

    override val alertResourceId: Int
        get() = R.string.income_operations_with_exchange_rate_exists

    override fun next(): EntityDestroyer<ExchangeRate>? {
        return ExchangeRateFromTransferOperationsDestroyer(context, data)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun getWhereCondition(exchangeRate: ExchangeRate): Condition<*, *> {
        return Operation.TYPE.eq(OperationType.INCOME_OPERATION)
            .and(Operation.EXCHANGE_RATE_ID.eq(exchangeRate.id))
    }
}
