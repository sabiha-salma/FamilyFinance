package io.github.zwieback.familyfinance.business.operation.service.provider

import android.content.Context
import androidx.core.content.ContextCompat
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.OperationView

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class ExpenseOperationViewProvider(context: Context) : EntityProvider<OperationView>(context) {

    override fun provideDefaultIcon(operation: OperationView): IIcon {
        return FontAwesome.Icon.faw_minus_circle
    }

    override fun provideDefaultIconColor(operation: OperationView): Int {
        return R.color.colorExpense
    }

    override fun provideTextColor(operation: OperationView): Int {
        return ContextCompat.getColor(context, provideDefaultIconColor(operation))
    }
}
