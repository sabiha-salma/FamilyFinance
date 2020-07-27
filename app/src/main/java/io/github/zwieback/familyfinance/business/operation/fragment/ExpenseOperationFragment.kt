package io.github.zwieback.familyfinance.business.operation.fragment

import io.github.zwieback.familyfinance.business.operation.adapter.ExpenseOperationAdapter
import io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter
import io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter.Companion.EXPENSE_OPERATION_FILTER

class ExpenseOperationFragment : OperationFragment<ExpenseOperationFilter>() {

    override val filterName: String
        get() = EXPENSE_OPERATION_FILTER

    override fun createEntityAdapter(): ExpenseOperationAdapter {
        val filter = extractFilter()
        return ExpenseOperationAdapter(requireContext(), clickListener, data, filter)
    }

    companion object {
        fun newInstance(filter: ExpenseOperationFilter) = ExpenseOperationFragment().apply {
            arguments = createArguments(EXPENSE_OPERATION_FILTER, filter)
        }
    }
}
