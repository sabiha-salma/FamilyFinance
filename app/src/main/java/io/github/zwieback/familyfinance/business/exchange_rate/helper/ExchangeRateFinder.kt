package io.github.zwieback.familyfinance.business.exchange_rate.helper

import io.github.zwieback.familyfinance.core.model.ExchangeRate
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class ExchangeRateFinder(private val data: ReactiveEntityStore<Persistable>) {

    fun findLastExchangeRate(currencyId: Int): ExchangeRate? {
        return data
            .select(ExchangeRate::class.java)
            .where(ExchangeRate.CURRENCY_ID.eq(currencyId))
            .orderBy(ExchangeRate.DATE.desc())
            .get()
            .firstOrNull()
    }
}
