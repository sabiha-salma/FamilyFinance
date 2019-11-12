package io.github.zwieback.familyfinance.business.template.activity.helper

import android.content.Context
import io.github.zwieback.familyfinance.business.operation.activity.helper.ExpenseOperationHelper
import io.github.zwieback.familyfinance.business.operation.activity.helper.IncomeOperationHelper
import io.github.zwieback.familyfinance.business.operation.activity.helper.OperationHelper
import io.github.zwieback.familyfinance.business.operation.activity.helper.TransferOperationHelper
import io.github.zwieback.familyfinance.core.model.TemplateView
import io.github.zwieback.familyfinance.core.model.type.TemplateType
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class TemplateQualifier(context: Context, data: ReactiveEntityStore<Persistable>) {

    private val incomeOperationHelper = IncomeOperationHelper(context, data)
    private val expenseOperationHelper = ExpenseOperationHelper(context, data)
    private val transferOperationHelper = TransferOperationHelper(context, data)

    fun determineHelper(template: TemplateView): OperationHelper<*> {
        return when (template.type) {
            TemplateType.EXPENSE_OPERATION -> expenseOperationHelper
            TemplateType.INCOME_OPERATION -> incomeOperationHelper
            TemplateType.TRANSFER_OPERATION -> transferOperationHelper
        }
    }
}
