package io.github.zwieback.familyfinance.business.currency.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityFromDestroyer
import io.github.zwieback.familyfinance.core.model.Account
import io.github.zwieback.familyfinance.core.model.Currency
import io.requery.Persistable
import io.requery.query.Condition
import io.requery.reactivex.ReactiveEntityStore

class CurrencyFromAccountsDestroyer(context: Context, data: ReactiveEntityStore<Persistable>) :
    EntityFromDestroyer<Currency, Account>(context, data) {

    override val fromClass: Class<Account>
        get() = Account::class.java

    override val alertResourceId: Int
        get() = R.string.accounts_with_currency_exists

    override fun next(): EntityDestroyer<Currency>? {
        return CurrencyFromExchangeRatesDestroyer(context, data)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun getWhereCondition(currency: Currency): Condition<*, *> {
        return Account.CURRENCY_ID.eq(currency.id)
    }
}
