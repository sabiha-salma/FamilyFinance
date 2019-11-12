package io.github.zwieback.familyfinance.business.sms_pattern.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.SmsPatternView

internal class SmsPatternViewProvider(context: Context) : EntityProvider<SmsPatternView>(context) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun provideDefaultIcon(smsPattern: SmsPatternView): IIcon {
        return if (smsPattern.isCommon) {
            FontAwesome.Icon.faw_comment
        } else {
            FontAwesome.Icon.faw_comment1
        }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun provideDefaultIconColor(smsPattern: SmsPatternView): Int {
        return R.color.colorPrimaryDark
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun provideTextColor(smsPattern: SmsPatternView): Int {
        return ContextCompat.getColor(context, provideDefaultIconColor(smsPattern))
    }
}
