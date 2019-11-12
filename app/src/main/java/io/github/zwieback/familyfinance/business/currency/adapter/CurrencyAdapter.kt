package io.github.zwieback.familyfinance.business.currency.adapter

import android.content.Context
import android.view.LayoutInflater

import io.github.zwieback.familyfinance.business.currency.filter.CurrencyFilter
import io.github.zwieback.familyfinance.business.currency.listener.OnCurrencyClickListener
import io.github.zwieback.familyfinance.business.currency.query.CurrencyQueryBuilder
import io.github.zwieback.familyfinance.core.adapter.BindingHolder
import io.github.zwieback.familyfinance.core.adapter.EntityAdapter
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.CurrencyView
import io.github.zwieback.familyfinance.databinding.ItemCurrencyBinding
import io.requery.Persistable
import io.requery.query.Result
import io.requery.reactivex.ReactiveEntityStore

class CurrencyAdapter(
    context: Context,
    clickListener: OnCurrencyClickListener,
    data: ReactiveEntityStore<Persistable>,
    filter: CurrencyFilter
) : EntityAdapter<CurrencyView, CurrencyFilter, ItemCurrencyBinding, OnCurrencyClickListener>(
    CurrencyView.`$TYPE`,
    context,
    clickListener,
    data,
    filter
) {

    override fun createProvider(context: Context): EntityProvider<CurrencyView> {
        return CurrencyViewProvider(context)
    }

    override fun inflate(inflater: LayoutInflater): ItemCurrencyBinding {
        return ItemCurrencyBinding.inflate(inflater)
    }

    override fun extractEntity(binding: ItemCurrencyBinding): CurrencyView {
        return binding.currency as CurrencyView
    }

    override fun performQuery(): Result<CurrencyView> {
        return CurrencyQueryBuilder.create(data)
            .build()
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun onBindViewHolder(
        currency: CurrencyView?,
        holder: BindingHolder<ItemCurrencyBinding>,
        position: Int
    ) {
        currency?.let {
            holder.binding.currency = currency
            provider.setupIcon(holder.binding.icon.icon, currency)
        }
    }
}
