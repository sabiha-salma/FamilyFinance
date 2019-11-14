package io.github.zwieback.familyfinance.business.operation.service.provider

import android.content.Context
import androidx.core.content.ContextCompat
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.Operation

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class ExpenseOperationProvider(context: Context) : EntityProvider<Operation>(context) {

    override fun provideDefaultIcon(operation: Operation): IIcon {
        return FontAwesome.Icon.faw_minus_circle
    }

    override fun provideDefaultIconColor(operation: Operation): Int {
        return R.color.colorExpense
    }

    override fun provideTextColor(operation: Operation): Int {
        return ContextCompat.getColor(context, provideDefaultIconColor(operation))
    }
}
