package io.github.zwieback.familyfinance.business.operation.activity

import android.view.View
import androidx.appcompat.widget.PopupMenu
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_OPERATION_ID
import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter
import io.github.zwieback.familyfinance.business.operation.fragment.OperationFragment
import io.github.zwieback.familyfinance.business.operation.listener.OnOperationClickListener
import io.github.zwieback.familyfinance.business.operation.listener.OperationFilterListener
import io.github.zwieback.familyfinance.core.activity.EntityActivity
import io.github.zwieback.familyfinance.core.model.Operation
import io.github.zwieback.familyfinance.core.model.OperationView

abstract class OperationActivity<FRAGMENT, FILTER> :
    EntityActivity<OperationView, Operation, FILTER, FRAGMENT>(),
    OnOperationClickListener,
    OperationFilterListener<FILTER>
        where FRAGMENT : OperationFragment<FILTER>,
              FILTER : OperationFilter {

    override val resultName: String
        get() = RESULT_OPERATION_ID

    override val fragmentTag: String
        get() = localClassName

    override val classOfRegularEntity: Class<Operation>
        get() = Operation::class.java

    override fun addFilterMenuItem(): Boolean {
        return true
    }

    override fun onEntityClick(view: View, entity: OperationView) {
        // do nothing
    }

    override fun getPopupMenuId(entity: OperationView): Int {
        return if (readOnly) {
            super.getPopupMenuId(entity)
        } else {
            R.menu.popup_entity_operation
        }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun getPopupItemClickListener(
        operation: OperationView
    ): PopupMenu.OnMenuItemClickListener {
        return PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_duplicate -> {
                    duplicateEntity(operation)
                    true
                }
                R.id.action_edit -> {
                    editEntity(operation)
                    true
                }
                R.id.action_delete -> {
                    deleteEntity(operation)
                    true
                }
                else -> false
            }
        }
    }
}
