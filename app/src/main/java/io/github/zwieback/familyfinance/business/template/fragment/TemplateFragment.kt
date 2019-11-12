package io.github.zwieback.familyfinance.business.template.fragment

import io.github.zwieback.familyfinance.business.template.adapter.TemplateAdapter
import io.github.zwieback.familyfinance.business.template.filter.TemplateFilter
import io.github.zwieback.familyfinance.business.template.filter.TemplateFilter.Companion.TEMPLATE_FILTER
import io.github.zwieback.familyfinance.business.template.listener.OnTemplateClickListener
import io.github.zwieback.familyfinance.core.fragment.EntityFragment
import io.github.zwieback.familyfinance.core.model.TemplateView
import io.github.zwieback.familyfinance.databinding.ItemTemplateBinding

class TemplateFragment :
    EntityFragment<
            TemplateView,
            TemplateFilter,
            ItemTemplateBinding,
            OnTemplateClickListener,
            TemplateAdapter
            >() {

    override fun createEntityAdapter(): TemplateAdapter {
        val filter = extractFilter(TEMPLATE_FILTER)
        return TemplateAdapter(requireContext(), clickListener, data, filter)
    }

    companion object {
        fun newInstance(filter: TemplateFilter) = TemplateFragment().apply {
            arguments = createArguments(TEMPLATE_FILTER, filter)
        }
    }
}
