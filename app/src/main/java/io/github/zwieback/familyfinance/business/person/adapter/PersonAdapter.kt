package io.github.zwieback.familyfinance.business.person.adapter

import android.content.Context
import android.view.LayoutInflater
import io.github.zwieback.familyfinance.business.person.filter.PersonFilter
import io.github.zwieback.familyfinance.business.person.listener.OnPersonClickListener
import io.github.zwieback.familyfinance.business.person.query.PersonQueryBuilder
import io.github.zwieback.familyfinance.core.adapter.BindingHolder
import io.github.zwieback.familyfinance.core.adapter.EntityFolderAdapter
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.PersonView
import io.github.zwieback.familyfinance.databinding.ItemPersonBinding
import io.requery.Persistable
import io.requery.query.Result
import io.requery.reactivex.ReactiveEntityStore

class PersonAdapter(
    context: Context,
    clickListener: OnPersonClickListener,
    data: ReactiveEntityStore<Persistable>,
    filter: PersonFilter
) : EntityFolderAdapter<PersonView, PersonFilter, ItemPersonBinding, OnPersonClickListener>(
    PersonView.`$TYPE`,
    context,
    clickListener,
    data,
    filter
) {

    override fun createProvider(context: Context): EntityProvider<PersonView> {
        return PersonViewProvider(context)
    }

    override fun inflate(inflater: LayoutInflater): ItemPersonBinding {
        return ItemPersonBinding.inflate(inflater)
    }

    override fun extractEntity(binding: ItemPersonBinding): PersonView {
        return binding.person as PersonView
    }

    override fun performQuery(): Result<PersonView> {
        return PersonQueryBuilder.create(data)
            .withParentId(parentId)
            .build()
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun onBindViewHolder(
        person: PersonView?,
        holder: BindingHolder<ItemPersonBinding>,
        position: Int
    ) {
        person?.let {
            holder.binding.person = person
            provider.setupIcon(holder.binding.icon.icon, person)
        }
    }
}
