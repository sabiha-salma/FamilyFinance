package io.github.zwieback.familyfinance.business.currency.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityFromDestroyer
import io.github.zwieback.familyfinance.core.model.Currency
import io.github.zwieback.familyfinance.core.model.ExchangeRate
import io.requery.Persistable
import io.requery.query.Condition
import io.requery.reactivex.ReactiveEntityStore

internal class CurrencyFromExchangeRatesDestroyer(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityFromDestroyer<Currency, ExchangeRate>(context, data) {

    override val fromClass: Class<ExchangeRate>
        get() = ExchangeRate::class.java

    override val alertResourceId: Int
        get() = R.string.exchange_rates_with_currency_exists

    override fun next(): EntityDestroyer<Currency>? {
        return CurrencyFromPreferencesDestroyer(context, data)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun getWhereCondition(currency: Currency): Condition<*, *> {
        return ExchangeRate.CURRENCY_ID.eq(currency.id)
    }
}
