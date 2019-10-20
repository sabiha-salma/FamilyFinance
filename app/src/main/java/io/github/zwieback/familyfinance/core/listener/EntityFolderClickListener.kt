package io.github.zwieback.familyfinance.core.listener

import android.view.View

import io.github.zwieback.familyfinance.core.model.IBaseEntityFolder

interface EntityFolderClickListener<E : IBaseEntityFolder> : EntityClickListener<E> {

    fun onFolderClick(view: View, entity: E)

    fun onFolderLongClick(view: View, entity: E)
}
