package io.github.zwieback.familyfinance.business.template.filter

import io.github.zwieback.familyfinance.core.filter.EntityFilter
import kotlinx.parcelize.Parcelize

@Parcelize
class TemplateFilter : EntityFilter() {

    companion object {
        const val TEMPLATE_FILTER = "templateFilter"
    }
}
