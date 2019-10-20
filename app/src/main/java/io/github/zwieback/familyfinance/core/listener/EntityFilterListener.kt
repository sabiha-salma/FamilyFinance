package io.github.zwieback.familyfinance.core.listener

import io.github.zwieback.familyfinance.core.filter.EntityFilter

interface EntityFilterListener<F : EntityFilter> {

    fun onApplyFilter(filter: F)
}
