package io.github.zwieback.familyfinance.business.person.activity

import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_TO_WHOM_ID

class ToWhomActivity : PersonActivity() {

    override val resultName: String
        get() = RESULT_TO_WHOM_ID
}
