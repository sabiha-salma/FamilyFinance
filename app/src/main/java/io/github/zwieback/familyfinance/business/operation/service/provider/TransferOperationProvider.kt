package io.github.zwieback.familyfinance.business.operation.service.provider

import android.content.Context
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.Operation
import io.github.zwieback.familyfinance.core.model.type.OperationType

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class TransferOperationProvider(context: Context) : EntityProvider<Operation>(context) {

    private val incomeProvider: IncomeOperationProvider = IncomeOperationProvider(context)
    private val expenseProvider: ExpenseOperationProvider = ExpenseOperationProvider(context)

    override fun provideDefaultIcon(operation: Operation): IIcon {
        return FontAwesome.Icon.faw_exchange_alt
    }

    override fun provideDefaultIconColor(operation: Operation): Int {
        return if (operation.type === OperationType.TRANSFER_EXPENSE_OPERATION) {
            expenseProvider.provideDefaultIconColor(operation)
        } else {
            incomeProvider.provideDefaultIconColor(operation)
        }
    }

    override fun provideTextColor(operation: Operation): Int {
        return if (operation.type === OperationType.TRANSFER_EXPENSE_OPERATION) {
            expenseProvider.provideTextColor(operation)
        } else {
            incomeProvider.provideTextColor(operation)
        }
    }
}
