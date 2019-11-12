package io.github.zwieback.familyfinance.business.exchange_rate.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityForceDestroyer
import io.github.zwieback.familyfinance.core.model.ExchangeRate
import io.requery.Persistable
import io.requery.meta.QueryAttribute
import io.requery.reactivex.ReactiveEntityStore

internal class ExchangeRateForceDestroyer(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityForceDestroyer<ExchangeRate>(context, data) {

    override val entityClass: Class<ExchangeRate>
        get() = ExchangeRate::class.java

    override val idAttribute: QueryAttribute<ExchangeRate, Int>
        get() = ExchangeRate.ID
}
