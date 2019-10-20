package io.github.zwieback.familyfinance.core.listener

import android.view.View

import io.github.zwieback.familyfinance.core.model.IBaseEntity

interface EntityClickListener<E : IBaseEntity> {

    fun onEntityClick(view: View, entity: E)

    fun onEntityLongClick(view: View, entity: E)
}
