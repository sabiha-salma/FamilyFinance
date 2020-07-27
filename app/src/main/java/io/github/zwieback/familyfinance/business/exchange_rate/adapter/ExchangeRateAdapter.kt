package io.github.zwieback.familyfinance.business.exchange_rate.adapter

import android.content.Context
import android.view.LayoutInflater
import io.github.zwieback.familyfinance.business.exchange_rate.filter.ExchangeRateFilter
import io.github.zwieback.familyfinance.business.exchange_rate.listener.OnExchangeRateClickListener
import io.github.zwieback.familyfinance.business.exchange_rate.query.ExchangeRateQueryBuilder
import io.github.zwieback.familyfinance.core.adapter.BindingHolder
import io.github.zwieback.familyfinance.core.adapter.EntityAdapter
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.ExchangeRateView
import io.github.zwieback.familyfinance.databinding.ItemExchangeRateBinding
import io.requery.Persistable
import io.requery.query.Result
import io.requery.reactivex.ReactiveEntityStore

class ExchangeRateAdapter(
    context: Context,
    clickListener: OnExchangeRateClickListener,
    data: ReactiveEntityStore<Persistable>,
    filter: ExchangeRateFilter
) : EntityAdapter<ExchangeRateView, ExchangeRateFilter, ItemExchangeRateBinding, OnExchangeRateClickListener>(
    ExchangeRateView.`$TYPE`,
    context,
    clickListener,
    data,
    filter
) {

    override fun createProvider(context: Context): EntityProvider<ExchangeRateView> {
        return ExchangeRateViewProvider(context)
    }

    override fun inflate(inflater: LayoutInflater): ItemExchangeRateBinding {
        return ItemExchangeRateBinding.inflate(inflater)
    }

    override fun extractEntity(binding: ItemExchangeRateBinding): ExchangeRateView {
        return binding.exchangeRate as ExchangeRateView
    }

    override fun performQuery(): Result<ExchangeRateView> {
        return ExchangeRateQueryBuilder.create(data)
            .withCurrencyId(filter.takeCurrencyId())
            .withStartDate(filter.startDate)
            .withEndDate(filter.endDate)
            .withStartValue(filter.startValue)
            .withEndValue(filter.endValue)
            .build()
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun onBindViewHolder(
        exchangeRate: ExchangeRateView?,
        holder: BindingHolder<ItemExchangeRateBinding>, position: Int
    ) {
        exchangeRate?.let {
            holder.binding.exchangeRate = exchangeRate
            provider.setupIcon(holder.binding.icon.icon, exchangeRate)
        }
    }
}
