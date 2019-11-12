package io.github.zwieback.familyfinance.business.currency.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityForceDestroyer
import io.github.zwieback.familyfinance.core.model.Currency
import io.requery.Persistable
import io.requery.meta.QueryAttribute
import io.requery.reactivex.ReactiveEntityStore

internal class CurrencyForceDestroyer(context: Context, data: ReactiveEntityStore<Persistable>) :
    EntityForceDestroyer<Currency>(context, data) {

    override val entityClass: Class<Currency>
        get() = Currency::class.java

    override val idAttribute: QueryAttribute<Currency, Int>
        get() = Currency.ID
}
