package io.github.zwieback.familyfinance.business.preference.custom.database

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.currency.activity.CurrencyActivity
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.CURRENCY_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_CURRENCY_ID
import io.github.zwieback.familyfinance.core.model.Currency
import io.github.zwieback.familyfinance.core.preference.custom.EntityActivityResultPreference

class CurrencyPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = TypedArrayUtils.getAttr(
        context,
        androidx.preference.R.attr.preferenceStyle,
        android.R.attr.preferenceStyle
    ),
    defStyleRes: Int = 0
) : EntityActivityResultPreference<Currency>(context, attrs, defStyleAttr, defStyleRes) {

    override val requestCode: Int
        get() = CURRENCY_CODE

    override val requestIntent: Intent
        get() = Intent(context, CurrencyActivity::class.java)

    override val resultName: String
        get() = RESULT_CURRENCY_ID

    override val savedEntityId: Int
        get() = databasePrefs.currencyId

    override val entityClass: Class<Currency>
        get() = Currency::class.java

    override val preferenceTitleRes: Int
        get() = R.string.currency_id_preference_title

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun saveEntityId(currencyId: Int) {
        databasePrefs.currencyId = currencyId
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun getEntityName(currency: Currency): String {
        return currency.name
    }
}
