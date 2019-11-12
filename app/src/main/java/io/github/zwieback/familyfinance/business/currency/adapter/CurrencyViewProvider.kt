package io.github.zwieback.familyfinance.business.currency.adapter

import android.content.Context
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.CurrencyView

class CurrencyViewProvider(context: Context) : EntityProvider<CurrencyView>(context) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun provideDefaultIcon(currency: CurrencyView): IIcon {
        return CommunityMaterial.Icon.cmd_currency_sign
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun provideDefaultIconColor(currency: CurrencyView): Int {
        return R.color.colorDollar
    }
}
