package io.github.zwieback.familyfinance.business.operation.activity

import android.os.Bundle
import android.view.MenuItem
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.operation.activity.helper.ExpenseOperationHelper
import io.github.zwieback.familyfinance.business.operation.activity.helper.IncomeOperationHelper
import io.github.zwieback.familyfinance.business.operation.activity.helper.OperationHelper
import io.github.zwieback.familyfinance.business.operation.activity.helper.TransferOperationHelper
import io.github.zwieback.familyfinance.business.operation.dialog.FlowOfFundsOperationFilterDialog
import io.github.zwieback.familyfinance.business.operation.filter.FlowOfFundsOperationFilter
import io.github.zwieback.familyfinance.business.operation.filter.FlowOfFundsOperationFilter.Companion.FLOW_OF_FUNDS_OPERATION_FILTER
import io.github.zwieback.familyfinance.business.operation.fragment.FlowOfFundsOperationFragment
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.model.Operation
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.core.model.type.OperationType
import io.github.zwieback.familyfinance.extension.operation.filter.applyPreferences

class FlowOfFundsOperationActivity :
    OperationActivity<FlowOfFundsOperationFragment, FlowOfFundsOperationFilter>() {

    private lateinit var incomeOperationHelper: IncomeOperationHelper
    private lateinit var expenseOperationHelper: ExpenseOperationHelper
    private lateinit var transferOperationHelper: TransferOperationHelper

    override val titleStringId: Int
        get() = R.string.flow_of_funds_activity_title

    override val filterName: String
        get() = FLOW_OF_FUNDS_OPERATION_FILTER

    override fun collectMenuIds(): List<Int> {
        return if (!readOnly) {
            listOf(R.menu.menu_entity_flow_of_funds_operation)
        } else {
            emptyList()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_expense -> {
                addExpenseOperation()
                true
            }
            R.id.action_add_income -> {
                addIncomeOperation()
                true
            }
            R.id.action_add_transfer -> {
                addTransferOperation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        incomeOperationHelper = IncomeOperationHelper(this, data)
        expenseOperationHelper = ExpenseOperationHelper(this, data)
        transferOperationHelper = TransferOperationHelper(this, data)
    }

    override fun createDefaultFilter() = FlowOfFundsOperationFilter().apply {
        applyPreferences(this@FlowOfFundsOperationActivity)
    }

    override fun createFragment(): FlowOfFundsOperationFragment {
        return FlowOfFundsOperationFragment.newInstance(filter)
    }

    private fun addExpenseOperation() {
        super.addEntity()
        val intent = expenseOperationHelper.getIntentToAdd()
        startActivity(intent)
    }

    private fun addIncomeOperation() {
        super.addEntity()
        val intent = incomeOperationHelper.getIntentToAdd()
        startActivity(intent)
    }

    private fun addTransferOperation() {
        super.addEntity()
        val intent = transferOperationHelper.getIntentToAdd()
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun editEntity(operation: OperationView) {
        super.editEntity(operation)
        val intent = determineHelper(operation).getIntentToEdit(operation)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun duplicateEntity(operation: OperationView) {
        super.duplicateEntity(operation)
        val intent = determineHelper(operation).getIntentToDuplicate(operation)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun createDestroyer(operation: OperationView): EntityDestroyer<Operation> {
        return determineHelper(operation).createDestroyer(operation)
    }

    override fun showFilterDialog() {
        FlowOfFundsOperationFilterDialog
            .newInstance(filter)
            .show(supportFragmentManager, "FlowOfFundsOperationFilterDialog")
    }

    private fun determineHelper(operation: OperationView): OperationHelper<*> {
        return when (operation.type) {
            OperationType.EXPENSE_OPERATION -> expenseOperationHelper
            OperationType.INCOME_OPERATION -> incomeOperationHelper
            OperationType.TRANSFER_EXPENSE_OPERATION,
            OperationType.TRANSFER_INCOME_OPERATION -> transferOperationHelper
        }
    }
}
