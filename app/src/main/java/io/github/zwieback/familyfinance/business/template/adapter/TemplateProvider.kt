package io.github.zwieback.familyfinance.business.template.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.Template
import io.github.zwieback.familyfinance.core.model.type.TemplateType

class TemplateProvider(context: Context) : EntityProvider<Template>(context) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun provideDefaultIcon(template: Template): IIcon {
        return FontAwesome.Icon.faw_file_alt1
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun provideDefaultIconColor(template: Template): Int {
        return when (template.type) {
            TemplateType.EXPENSE_OPERATION -> R.color.colorExpense
            TemplateType.INCOME_OPERATION -> R.color.colorIncome
            TemplateType.TRANSFER_OPERATION -> R.color.colorPrimaryDark
        }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun provideTextColor(template: Template): Int {
        return ContextCompat.getColor(context, provideDefaultIconColor(template))
    }
}
