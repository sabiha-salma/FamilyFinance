package io.github.zwieback.familyfinance.business.operation.service.provider

import android.content.Context
import com.mikepenz.iconics.typeface.IIcon
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.core.model.type.OperationType

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class FlowOfFundsOperationViewProvider(context: Context) : EntityProvider<OperationView>(context) {

    private val incomeProvider: IncomeOperationViewProvider =
        IncomeOperationViewProvider(context)
    private val expenseProvider: ExpenseOperationViewProvider =
        ExpenseOperationViewProvider(context)
    private val transferProvider: TransferOperationViewProvider =
        TransferOperationViewProvider(context)

    override fun provideDefaultIcon(operation: OperationView): IIcon {
        return determineProvider(operation).provideDefaultIcon(operation)
    }

    override fun provideDefaultIconColor(operation: OperationView): Int {
        return determineProvider(operation).provideDefaultIconColor(operation)
    }

    override fun provideTextColor(operation: OperationView): Int {
        return determineProvider(operation).provideTextColor(operation)
    }

    private fun determineProvider(operation: OperationView): EntityProvider<OperationView> {
        return when (operation.type) {
            OperationType.EXPENSE_OPERATION -> expenseProvider
            OperationType.INCOME_OPERATION -> incomeProvider
            OperationType.TRANSFER_EXPENSE_OPERATION,
            OperationType.TRANSFER_INCOME_OPERATION -> transferProvider
        }
    }
}
