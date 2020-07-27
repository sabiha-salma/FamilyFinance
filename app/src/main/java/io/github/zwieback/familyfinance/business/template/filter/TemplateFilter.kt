package io.github.zwieback.familyfinance.business.template.filter

import io.github.zwieback.familyfinance.core.filter.EntityFilter
import kotlinx.android.parcel.Parcelize

@Parcelize
class TemplateFilter : EntityFilter() {

    companion object {
        const val TEMPLATE_FILTER = "templateFilter"
    }
}
