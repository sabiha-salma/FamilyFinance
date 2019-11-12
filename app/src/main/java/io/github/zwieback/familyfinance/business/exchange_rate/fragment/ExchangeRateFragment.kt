package io.github.zwieback.familyfinance.business.exchange_rate.fragment

import io.github.zwieback.familyfinance.business.exchange_rate.adapter.ExchangeRateAdapter
import io.github.zwieback.familyfinance.business.exchange_rate.filter.ExchangeRateFilter
import io.github.zwieback.familyfinance.business.exchange_rate.filter.ExchangeRateFilter.Companion.EXCHANGE_RATE_FILTER
import io.github.zwieback.familyfinance.business.exchange_rate.listener.OnExchangeRateClickListener
import io.github.zwieback.familyfinance.core.fragment.EntityFragment
import io.github.zwieback.familyfinance.core.model.ExchangeRateView
import io.github.zwieback.familyfinance.databinding.ItemExchangeRateBinding

class ExchangeRateFragment :
    EntityFragment<
            ExchangeRateView,
            ExchangeRateFilter,
            ItemExchangeRateBinding,
            OnExchangeRateClickListener,
            ExchangeRateAdapter
            >() {

    override fun createEntityAdapter(): ExchangeRateAdapter {
        val filter = extractFilter(EXCHANGE_RATE_FILTER)
        return ExchangeRateAdapter(requireContext(), clickListener, data, filter)
    }

    companion object {
        fun newInstance(filter: ExchangeRateFilter) = ExchangeRateFragment().apply {
            arguments = createArguments(EXCHANGE_RATE_FILTER, filter)
        }
    }
}
