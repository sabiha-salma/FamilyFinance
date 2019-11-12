package io.github.zwieback.familyfinance.business.exchange_rate.activity

import android.content.Intent
import android.os.Bundle
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.RESULT_EXCHANGE_RATE_ID
import io.github.zwieback.familyfinance.business.exchange_rate.dialog.ExchangeRateFilterDialog
import io.github.zwieback.familyfinance.business.exchange_rate.filter.ExchangeRateFilter
import io.github.zwieback.familyfinance.business.exchange_rate.filter.ExchangeRateFilter.Companion.EXCHANGE_RATE_FILTER
import io.github.zwieback.familyfinance.business.exchange_rate.fragment.ExchangeRateFragment
import io.github.zwieback.familyfinance.business.exchange_rate.lifecycle.destroyer.ExchangeRateFromExpenseOperationsDestroyer
import io.github.zwieback.familyfinance.business.exchange_rate.listener.OnExchangeRateClickListener
import io.github.zwieback.familyfinance.core.activity.EntityActivity
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.model.ExchangeRate
import io.github.zwieback.familyfinance.core.model.ExchangeRateView

class ExchangeRateActivity :
    EntityActivity<ExchangeRateView, ExchangeRate, ExchangeRateFilter, ExchangeRateFragment>(),
    OnExchangeRateClickListener {

    override val titleStringId: Int
        get() = R.string.exchange_rate_activity_title

    override val filterName: String
        get() = EXCHANGE_RATE_FILTER

    override val resultName: String
        get() = RESULT_EXCHANGE_RATE_ID

    override val fragmentTag: String
        get() = localClassName

    override val classOfRegularEntity: Class<ExchangeRate>
        get() = ExchangeRate::class.java

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        val currencyId = extractId(intent.extras, INPUT_CURRENCY_ID)
        currencyId?.let {
            filter.setCurrencyId(currencyId)
        }
    }

    override fun addFilterMenuItem(): Boolean {
        return true
    }

    override fun createDefaultFilter(): ExchangeRateFilter {
        return ExchangeRateFilter().apply {
            setCurrencyId(databasePrefs.currencyId)
        }
    }

    override fun createFragment(): ExchangeRateFragment {
        return ExchangeRateFragment.newInstance(filter)
    }

    override fun addEntity() {
        super.addEntity()
        val intent = Intent(this, ExchangeRateEditActivity::class.java)
            .putExtra(ExchangeRateEditActivity.INPUT_CURRENCY_ID, filter.getCurrencyId())
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun editEntity(exchangeRate: ExchangeRateView) {
        super.editEntity(exchangeRate)
        val intent = Intent(this, ExchangeRateEditActivity::class.java)
            .putExtra(ExchangeRateEditActivity.INPUT_EXCHANGE_RATE_ID, exchangeRate.id)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun createDestroyer(exchangeRateView: ExchangeRateView): EntityDestroyer<ExchangeRate> {
        return ExchangeRateFromExpenseOperationsDestroyer(this, data)
    }

    override fun showFilterDialog() {
        ExchangeRateFilterDialog
            .newInstance(filter)
            .show(supportFragmentManager, "ExchangeRateFilterDialog")
    }

    companion object {
        const val INPUT_CURRENCY_ID = "currencyId"
    }
}
