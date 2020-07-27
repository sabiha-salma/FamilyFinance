package io.github.zwieback.familyfinance.core.adapter

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding

import io.github.zwieback.familyfinance.core.filter.EntityFolderFilter
import io.github.zwieback.familyfinance.core.listener.EntityFolderClickListener
import io.github.zwieback.familyfinance.core.model.IBaseEntityFolder
import io.requery.Persistable
import io.requery.meta.Type
import io.requery.reactivex.ReactiveEntityStore

abstract class EntityFolderAdapter<
        ENTITY : IBaseEntityFolder,
        FILTER : EntityFolderFilter,
        BINDING : ViewDataBinding,
        LISTENER : EntityFolderClickListener<ENTITY>>
protected constructor(
    type: Type<ENTITY>,
    context: Context,
    clickListener: LISTENER,
    data: ReactiveEntityStore<Persistable>,
    filter: FILTER
) : EntityAdapter<ENTITY, FILTER, BINDING, LISTENER>(type, context, clickListener, data, filter) {

    protected val parentId: Int? = filter.takeParentId()

    @Suppress("UNCHECKED_CAST")
    override fun onClick(view: View) {
        val binding = view.tag as? BINDING ?: return
        val entity = extractEntity(binding)
        if (entity.isFolder) {
            clickListener.onFolderClick(view, entity)
        } else {
            clickListener.onEntityClick(view, entity)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onLongClick(view: View): Boolean {
        val binding = view.tag as? BINDING ?: return false
        val entity = extractEntity(binding)
        if (entity.isFolder) {
            clickListener.onFolderLongClick(view, entity)
        } else {
            clickListener.onEntityLongClick(view, entity)
        }
        return true
    }
}
