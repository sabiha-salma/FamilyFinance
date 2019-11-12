package io.github.zwieback.familyfinance.business.sms_pattern.fragment

import io.github.zwieback.familyfinance.business.sms_pattern.adapter.SmsPatternAdapter
import io.github.zwieback.familyfinance.business.sms_pattern.filter.SmsPatternFilter
import io.github.zwieback.familyfinance.business.sms_pattern.filter.SmsPatternFilter.Companion.SMS_PATTERN_FILTER
import io.github.zwieback.familyfinance.business.sms_pattern.listener.OnSmsPatternClickListener
import io.github.zwieback.familyfinance.core.fragment.EntityFragment
import io.github.zwieback.familyfinance.core.model.SmsPatternView
import io.github.zwieback.familyfinance.databinding.ItemSmsPatternBinding

class SmsPatternFragment :
    EntityFragment<SmsPatternView, SmsPatternFilter, ItemSmsPatternBinding, OnSmsPatternClickListener, SmsPatternAdapter>() {

    override fun createEntityAdapter(): SmsPatternAdapter {
        val filter = extractFilter(SMS_PATTERN_FILTER)
        return SmsPatternAdapter(requireContext(), clickListener, data, filter)
    }

    companion object {
        fun newInstance(filter: SmsPatternFilter) = SmsPatternFragment().apply {
            arguments = createArguments(SMS_PATTERN_FILTER, filter)
        }
    }
}
