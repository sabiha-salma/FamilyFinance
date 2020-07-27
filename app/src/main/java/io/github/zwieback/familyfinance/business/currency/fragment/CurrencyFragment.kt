package io.github.zwieback.familyfinance.business.currency.fragment

import io.github.zwieback.familyfinance.business.currency.adapter.CurrencyAdapter
import io.github.zwieback.familyfinance.business.currency.filter.CurrencyFilter
import io.github.zwieback.familyfinance.business.currency.filter.CurrencyFilter.Companion.CURRENCY_FILTER
import io.github.zwieback.familyfinance.business.currency.listener.OnCurrencyClickListener
import io.github.zwieback.familyfinance.core.fragment.EntityFragment
import io.github.zwieback.familyfinance.core.model.CurrencyView
import io.github.zwieback.familyfinance.databinding.ItemCurrencyBinding

class CurrencyFragment :
    EntityFragment<
            CurrencyView,
            CurrencyFilter,
            ItemCurrencyBinding,
            OnCurrencyClickListener,
            CurrencyAdapter
            >() {

    override val filterName: String
        get() = CURRENCY_FILTER

    override fun createEntityAdapter(): CurrencyAdapter {
        val filter = extractFilter()
        return CurrencyAdapter(requireContext(), clickListener, data, filter)
    }

    companion object {
        fun newInstance(filter: CurrencyFilter) = CurrencyFragment().apply {
            arguments = createArguments(CURRENCY_FILTER, filter)
        }
    }
}
