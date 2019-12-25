package io.github.zwieback.familyfinance.business.currency.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityFromPreferencesDestroyer
import io.github.zwieback.familyfinance.core.model.Currency
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

internal class CurrencyFromPreferencesDestroyer(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityFromPreferencesDestroyer<Currency>(context, data) {

    override val alertResourceId: Int
        get() = R.string.preferences_contains_currency

    override fun next(): EntityDestroyer<Currency>? {
        return CurrencyForceDestroyer(context, data)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun preferencesContainsEntity(currency: Currency): Boolean {
        val defaultCurrencyId = runBlocking(Dispatchers.IO) {
            databasePrefs.currencyId
        }
        return defaultCurrencyId == currency.id
    }
}
