package io.github.zwieback.familyfinance.business.operation.activity

import android.os.Bundle
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.operation.activity.helper.TransferOperationHelper
import io.github.zwieback.familyfinance.business.operation.dialog.TransferOperationFilterDialog
import io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter
import io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter.Companion.TRANSFER_OPERATION_FILTER
import io.github.zwieback.familyfinance.business.operation.fragment.TransferOperationFragment
import io.github.zwieback.familyfinance.business.operation.lifecycle.destroyer.TransferOperationForceDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.model.Operation
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.extension.operation.filter.applyPreferences

class TransferOperationActivity :
    OperationActivity<TransferOperationFragment, TransferOperationFilter>() {

    private lateinit var operationHelper: TransferOperationHelper

    override val titleStringId: Int
        get() = R.string.transfer_operation_activity_title

    override val filterName: String
        get() = TRANSFER_OPERATION_FILTER

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        operationHelper = TransferOperationHelper(this, data)
    }

    override fun createDefaultFilter() = TransferOperationFilter().apply {
        applyPreferences(this@TransferOperationActivity)
        putArticleId(databasePrefs.transferArticleId)
    }

    override fun createFragment(): TransferOperationFragment {
        return TransferOperationFragment.newInstance(filter)
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
        return TransferOperationForceDestroyer(this, data)
    }

    override fun showFilterDialog() {
        TransferOperationFilterDialog
            .newInstance(filter)
            .show(supportFragmentManager, "TransferOperationFilterDialog")
    }
}
