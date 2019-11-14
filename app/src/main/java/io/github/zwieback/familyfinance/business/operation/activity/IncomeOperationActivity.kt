package io.github.zwieback.familyfinance.business.operation.activity

import android.os.Bundle
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.operation.activity.helper.IncomeOperationHelper
import io.github.zwieback.familyfinance.business.operation.dialog.IncomeOperationFilterDialog
import io.github.zwieback.familyfinance.business.operation.filter.IncomeOperationFilter
import io.github.zwieback.familyfinance.business.operation.filter.IncomeOperationFilter.Companion.INCOME_OPERATION_FILTER
import io.github.zwieback.familyfinance.business.operation.fragment.IncomeOperationFragment
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.model.Operation
import io.github.zwieback.familyfinance.core.model.OperationView

class IncomeOperationActivity :
    OperationActivity<IncomeOperationFragment, IncomeOperationFilter>() {

    private lateinit var operationHelper: IncomeOperationHelper

    override val titleStringId: Int
        get() = R.string.income_operation_activity_title

    override val filterName: String
        get() = INCOME_OPERATION_FILTER

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        operationHelper = IncomeOperationHelper(this, data)
    }

    override fun createDefaultFilter() = IncomeOperationFilter().apply {
        setArticleId(databasePrefs.incomesArticleId)
    }

    override fun createFragment(): IncomeOperationFragment {
        return IncomeOperationFragment.newInstance(filter)
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
        IncomeOperationFilterDialog
            .newInstance(filter)
            .show(supportFragmentManager, "IncomeOperationFilterDialog")
    }
}
