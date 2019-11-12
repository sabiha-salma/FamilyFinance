package io.github.zwieback.familyfinance.business.sms_pattern.adapter

import android.content.Context
import android.view.LayoutInflater
import io.github.zwieback.familyfinance.business.sms_pattern.filter.SmsPatternFilter
import io.github.zwieback.familyfinance.business.sms_pattern.listener.OnSmsPatternClickListener
import io.github.zwieback.familyfinance.business.sms_pattern.query.SmsPatternQueryBuilder
import io.github.zwieback.familyfinance.core.adapter.BindingHolder
import io.github.zwieback.familyfinance.core.adapter.EntityAdapter
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.SmsPatternView
import io.github.zwieback.familyfinance.databinding.ItemSmsPatternBinding
import io.requery.Persistable
import io.requery.query.Result
import io.requery.reactivex.ReactiveEntityStore

class SmsPatternAdapter(
    context: Context,
    clickListener: OnSmsPatternClickListener,
    data: ReactiveEntityStore<Persistable>,
    filter: SmsPatternFilter
) : EntityAdapter<SmsPatternView, SmsPatternFilter, ItemSmsPatternBinding, OnSmsPatternClickListener>(
    SmsPatternView.`$TYPE`,
    context,
    clickListener,
    data,
    filter
) {

    override fun createProvider(context: Context): EntityProvider<SmsPatternView> {
        return SmsPatternViewProvider(context)
    }

    override fun inflate(inflater: LayoutInflater): ItemSmsPatternBinding {
        return ItemSmsPatternBinding.inflate(inflater)
    }

    override fun extractEntity(binding: ItemSmsPatternBinding): SmsPatternView {
        return binding.smsPattern as SmsPatternView
    }

    override fun performQuery(): Result<SmsPatternView> {
        return SmsPatternQueryBuilder.create(data)
            .build()
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun onBindViewHolder(
        smsPattern: SmsPatternView?,
        holder: BindingHolder<ItemSmsPatternBinding>,
        position: Int
    ) {
        smsPattern?.let {
            holder.binding.smsPattern = smsPattern
            provider.setupIcon(holder.binding.icon.icon, smsPattern)
        }
    }
}
