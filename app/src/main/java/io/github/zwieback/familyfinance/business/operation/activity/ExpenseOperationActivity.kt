package io.github.zwieback.familyfinance.business.operation.activity

import android.os.Bundle
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.operation.activity.helper.ExpenseOperationHelper
import io.github.zwieback.familyfinance.business.operation.dialog.ExpenseOperationFilterDialog
import io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter
import io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter.Companion.EXPENSE_OPERATION_FILTER
import io.github.zwieback.familyfinance.business.operation.fragment.ExpenseOperationFragment
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.model.Operation
import io.github.zwieback.familyfinance.core.model.OperationView

class ExpenseOperationActivity :
    OperationActivity<ExpenseOperationFragment, ExpenseOperationFilter>() {

    private lateinit var operationHelper: ExpenseOperationHelper

    override val titleStringId: Int
        get() = R.string.expense_operation_activity_title

    override val filterName: String
        get() = EXPENSE_OPERATION_FILTER

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        operationHelper = ExpenseOperationHelper(this, data)
    }

    override fun createDefaultFilter() = ExpenseOperationFilter().apply {
        setArticleId(databasePrefs.expensesArticleId)
    }

    override fun createFragment(): ExpenseOperationFragment {
        return ExpenseOperationFragment.newInstance(filter)
    }

    override fun addEntity() {
        super.addEntity()
        val intent = operationHelper.getIntentToAdd(filter)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun editEntity(operation: OperationView) {
        super.editEntity(operation)
        val intent = operationHelper.getIntentToEdit(operation)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun duplicateEntity(operation: OperationView) {
        super.duplicateEntity(operation)
        val intent = operationHelper.getIntentToDuplicate(operation)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun createDestroyer(operation: OperationView): EntityDestroyer<Operation> {
        return operationHelper.createDestroyer(operation)
    }

    override fun showFilterDialog() {
        ExpenseOperationFilterDialog
            .newInstance(filter)
            .show(supportFragmentManager, "ExpenseOperationFilterDialog")
    }
}
