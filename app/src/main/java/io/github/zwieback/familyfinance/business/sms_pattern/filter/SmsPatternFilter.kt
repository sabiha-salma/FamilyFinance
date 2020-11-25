package io.github.zwieback.familyfinance.business.sms_pattern.filter

import io.github.zwieback.familyfinance.core.filter.EntityFilter
import kotlinx.parcelize.Parcelize

@Parcelize
class SmsPatternFilter : EntityFilter() {

    companion object {
        const val SMS_PATTERN_FILTER = "smsPatternFilter"
    }
}
