package io.github.zwieback.familyfinance.core.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

import io.github.zwieback.familyfinance.core.filter.EntityFilter
import io.github.zwieback.familyfinance.core.listener.EntityClickListener
import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.requery.Persistable
import io.requery.android.QueryRecyclerAdapter
import io.requery.meta.Type
import io.requery.reactivex.ReactiveEntityStore

abstract class EntityAdapter<
        ENTITY : IBaseEntity,
        FILTER : EntityFilter,
        BINDING : ViewDataBinding,
        LISTENER : EntityClickListener<ENTITY>>
protected constructor(
    type: Type<ENTITY>,
    protected val context: Context,
    protected val clickListener: LISTENER,
    protected val data: ReactiveEntityStore<Persistable>,
    protected var filter: FILTER
) : QueryRecyclerAdapter<ENTITY, BindingHolder<BINDING>>(type),
    View.OnClickListener,
    View.OnLongClickListener {

    protected val provider: EntityProvider<ENTITY>

    init {
        this.provider = createProvider(context)
    }

    protected abstract fun createProvider(context: Context): EntityProvider<ENTITY>

    protected abstract fun inflate(inflater: LayoutInflater): BINDING

    protected abstract fun extractEntity(binding: BINDING): ENTITY

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<BINDING> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = inflate(inflater)
        binding.root.tag = binding
        binding.root.setOnClickListener(this)
        binding.root.setOnLongClickListener(this)
        return BindingHolder(binding)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onClick(view: View) {
        val binding = view.tag as? BINDING ?: return
        clickListener.onEntityClick(view, extractEntity(binding))
    }

    @Suppress("UNCHECKED_CAST")
    override fun onLongClick(view: View): Boolean {
        val binding = view.tag as? BINDING ?: return false
        clickListener.onEntityLongClick(view, extractEntity(binding))
        return true
    }

    fun applyFilter(filter: FILTER) {
        this.filter = filter
    }
}
