package io.github.zwieback.familyfinance.business.operation.service.provider

import android.content.Context
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.core.model.type.OperationType

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class TransferOperationViewProvider(context: Context) : EntityProvider<OperationView>(context) {

    private val incomeProvider: IncomeOperationViewProvider =
        IncomeOperationViewProvider(context)
    private val expenseProvider: ExpenseOperationViewProvider =
        ExpenseOperationViewProvider(context)

    override fun provideDefaultIcon(operation: OperationView): IIcon {
        return FontAwesome.Icon.faw_exchange_alt
    }

    override fun provideDefaultIconColor(operation: OperationView): Int {
        return if (operation.type === OperationType.TRANSFER_EXPENSE_OPERATION) {
            expenseProvider.provideDefaultIconColor(operation)
        } else {
            incomeProvider.provideDefaultIconColor(operation)
        }
    }

    override fun provideTextColor(operation: OperationView): Int {
        return if (operation.type === OperationType.TRANSFER_EXPENSE_OPERATION) {
            expenseProvider.provideTextColor(operation)
        } else {
            incomeProvider.provideTextColor(operation)
        }
    }
}
