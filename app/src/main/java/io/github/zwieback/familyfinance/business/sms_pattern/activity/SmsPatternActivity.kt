package io.github.zwieback.familyfinance.business.sms_pattern.activity

import android.content.Intent
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_SMS_PATTERN_ID
import io.github.zwieback.familyfinance.business.sms_pattern.filter.SmsPatternFilter
import io.github.zwieback.familyfinance.business.sms_pattern.filter.SmsPatternFilter.Companion.SMS_PATTERN_FILTER
import io.github.zwieback.familyfinance.business.sms_pattern.fragment.SmsPatternFragment
import io.github.zwieback.familyfinance.business.sms_pattern.lifecycle.destroyer.SmsPatternForceDestroyer
import io.github.zwieback.familyfinance.business.sms_pattern.listener.OnSmsPatternClickListener
import io.github.zwieback.familyfinance.core.activity.EntityActivity
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.model.SmsPattern
import io.github.zwieback.familyfinance.core.model.SmsPatternView

class SmsPatternActivity :
    EntityActivity<SmsPatternView, SmsPattern, SmsPatternFilter, SmsPatternFragment>(),
    OnSmsPatternClickListener {

    override val titleStringId: Int
        get() = R.string.sms_pattern_activity_title

    override val filterName: String
        get() = SMS_PATTERN_FILTER

    override val resultName: String
        get() = RESULT_SMS_PATTERN_ID

    override val fragmentTag: String
        get() = localClassName

    override val classOfRegularEntity: Class<SmsPattern>
        get() = SmsPattern::class.java

    override fun createDefaultFilter(): SmsPatternFilter {
        return SmsPatternFilter()
    }

    override fun createFragment(): SmsPatternFragment {
        return SmsPatternFragment.newInstance(filter)
    }

    override fun addEntity() {
        super.addEntity()
        val intent = Intent(this, SmsPatternEditActivity::class.java)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun editEntity(smsPattern: SmsPatternView) {
        super.editEntity(smsPattern)
        val intent = Intent(this, SmsPatternEditActivity::class.java)
            .putExtra(SmsPatternEditActivity.INPUT_SMS_PATTERN_ID, smsPattern.id)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun createDestroyer(smsPattern: SmsPatternView): EntityDestroyer<SmsPattern> {
        return SmsPatternForceDestroyer(this, data)
    }
}
