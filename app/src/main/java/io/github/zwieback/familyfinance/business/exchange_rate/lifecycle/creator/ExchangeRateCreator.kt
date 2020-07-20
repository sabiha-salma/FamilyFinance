package io.github.zwieback.familyfinance.business.exchange_rate.lifecycle.creator

import android.content.Context
import io.github.zwieback.familyfinance.core.lifecycle.creator.EntityCreator
import io.github.zwieback.familyfinance.core.model.Currency
import io.github.zwieback.familyfinance.core.model.ExchangeRate
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore
import org.threeten.bp.LocalDate
import java.math.BigDecimal

class ExchangeRateCreator(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityCreator<ExchangeRate>(context, data) {

    override fun buildEntities(): Iterable<ExchangeRate> {
        val defaultCurrency = findCurrency(databasePrefs.currencyId)
        return sortedSetOf(
            this,
            createExchangeRate(defaultCurrency, BigDecimal.ONE, LocalDate.now())
        )
    }

    override fun compare(left: ExchangeRate, right: ExchangeRate): Int {
        return left.date.compareTo(right.date)
    }

    private fun findCurrency(currencyId: Int): Currency {
        val currencies = data
            .select(Currency::class.java)
            .where(Currency.ID.eq(currencyId))
            .get()
        return currencies.first()
    }

    private fun createExchangeRate(
        currency: Currency,
        value: BigDecimal,
        date: LocalDate
    ): ExchangeRate {
        return ExchangeRate()
            .setCurrency(currency)
            .setValue(value)
            .setDate(date)
    }
}
