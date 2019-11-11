package io.github.zwieback.familyfinance.business.account.adapter.calculator

import io.github.zwieback.familyfinance.core.model.AccountView
import io.reactivex.functions.Consumer
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore
import java.math.BigDecimal

abstract class AccountBalanceCalculator internal constructor(
    protected val data: ReactiveEntityStore<Persistable>,
    protected val account: AccountView
) {

    abstract fun calculateBalance(showBalanceConsumer: Consumer<BigDecimal>)
}
