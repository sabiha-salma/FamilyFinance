package io.github.zwieback.familyfinance.business.currency.activity

import android.content.Intent
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.currency.activity.CurrencyEditActivity.Companion.INPUT_CURRENCY_ID
import io.github.zwieback.familyfinance.business.currency.filter.CurrencyFilter
import io.github.zwieback.familyfinance.business.currency.filter.CurrencyFilter.Companion.CURRENCY_FILTER
import io.github.zwieback.familyfinance.business.currency.fragment.CurrencyFragment
import io.github.zwieback.familyfinance.business.currency.lifecycle.destroyer.CurrencyFromAccountsDestroyer
import io.github.zwieback.familyfinance.business.currency.listener.OnCurrencyClickListener
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_CURRENCY_ID
import io.github.zwieback.familyfinance.core.activity.EntityActivity
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.model.Currency
import io.github.zwieback.familyfinance.core.model.CurrencyView

class CurrencyActivity :
    EntityActivity<CurrencyView, Currency, CurrencyFilter, CurrencyFragment>(),
    OnCurrencyClickListener {

    override val titleStringId: Int
        get() = R.string.currency_activity_title

    override val filterName: String
        get() = CURRENCY_FILTER

    override val resultName: String
        get() = RESULT_CURRENCY_ID

    override val fragmentTag: String
        get() = localClassName

    override val classOfRegularEntity: Class<Currency>
        get() = Currency::class.java

    override fun createDefaultFilter(): CurrencyFilter {
        return CurrencyFilter()
    }

    override fun createFragment(): CurrencyFragment {
        return CurrencyFragment.newInstance(filter)
    }

    override fun addEntity() {
        super.addEntity()
        val intent = Intent(this, CurrencyEditActivity::class.java)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun editEntity(currency: CurrencyView) {
        super.editEntity(currency)
        val intent = Intent(this, CurrencyEditActivity::class.java)
            .putExtra(INPUT_CURRENCY_ID, currency.id)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun createDestroyer(currency: CurrencyView): EntityDestroyer<Currency> {
        return CurrencyFromAccountsDestroyer(this, data)
    }
}
