package io.github.zwieback.familyfinance.business.template.adapter

import android.content.Context
import android.view.LayoutInflater

import io.github.zwieback.familyfinance.business.template.filter.TemplateFilter
import io.github.zwieback.familyfinance.business.template.listener.OnTemplateClickListener
import io.github.zwieback.familyfinance.business.template.query.TemplateQueryBuilder
import io.github.zwieback.familyfinance.core.adapter.BindingHolder
import io.github.zwieback.familyfinance.core.adapter.EntityAdapter
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.TemplateView
import io.github.zwieback.familyfinance.databinding.ItemTemplateBinding
import io.requery.Persistable
import io.requery.query.Result
import io.requery.reactivex.ReactiveEntityStore

class TemplateAdapter(
    context: Context,
    clickListener: OnTemplateClickListener,
    data: ReactiveEntityStore<Persistable>,
    filter: TemplateFilter
) : EntityAdapter<TemplateView, TemplateFilter, ItemTemplateBinding, OnTemplateClickListener>(
    TemplateView.`$TYPE`,
    context,
    clickListener,
    data,
    filter
) {

    override fun createProvider(context: Context): EntityProvider<TemplateView> {
        return TemplateViewProvider(context)
    }

    override fun inflate(inflater: LayoutInflater): ItemTemplateBinding {
        return ItemTemplateBinding.inflate(inflater)
    }

    override fun extractEntity(binding: ItemTemplateBinding): TemplateView {
        return binding.template as TemplateView
    }

    override fun performQuery(): Result<TemplateView> {
        return TemplateQueryBuilder.create(data)
            .build()
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun onBindViewHolder(
        template: TemplateView?,
        holder: BindingHolder<ItemTemplateBinding>,
        position: Int
    ) {
        template?.let {
            holder.binding.template = template
            provider.setupIcon(holder.binding.icon.icon, template)
        }
    }
}
