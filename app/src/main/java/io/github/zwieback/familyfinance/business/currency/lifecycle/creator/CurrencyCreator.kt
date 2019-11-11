package io.github.zwieback.familyfinance.business.currency.lifecycle.creator

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.creator.EntityCreator
import io.github.zwieback.familyfinance.core.model.Currency
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class CurrencyCreator(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityCreator<Currency>(context, data) {

    override fun buildEntities(): Iterable<Currency> {
        return sortedSetOf(
            this,
            createCurrency(
                getString(R.string.default_currency_name),
                getString(R.string.default_currency_description)
            )
        )
    }

    override fun compare(left: Currency, right: Currency): Int {
        return left.name.compareTo(right.name)
    }

    private fun createCurrency(name: String, description: String): Currency {
        return Currency()
            .setName(name)
            .setDescription(description)
    }
}
