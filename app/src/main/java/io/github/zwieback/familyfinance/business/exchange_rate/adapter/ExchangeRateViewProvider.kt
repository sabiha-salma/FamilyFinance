package io.github.zwieback.familyfinance.business.exchange_rate.adapter

import android.content.Context
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.ExchangeRateView

internal class ExchangeRateViewProvider(context: Context) :
    EntityProvider<ExchangeRateView>(context) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun provideDefaultIcon(exchangeRate: ExchangeRateView): IIcon {
        return CommunityMaterial.Icon.cmd_cash
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun provideDefaultIconColor(exchangeRate: ExchangeRateView): Int {
        return R.color.colorDollar
    }
}
