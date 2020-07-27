package io.github.zwieback.familyfinance.business.operation.fragment

import io.github.zwieback.familyfinance.business.operation.adapter.FlowOfFundsOperationAdapter
import io.github.zwieback.familyfinance.business.operation.filter.FlowOfFundsOperationFilter
import io.github.zwieback.familyfinance.business.operation.filter.FlowOfFundsOperationFilter.Companion.FLOW_OF_FUNDS_OPERATION_FILTER

class FlowOfFundsOperationFragment : OperationFragment<FlowOfFundsOperationFilter>() {

    override val filterName: String
        get() = FLOW_OF_FUNDS_OPERATION_FILTER

    override fun createEntityAdapter(): FlowOfFundsOperationAdapter {
        val filter = extractFilter()
        return FlowOfFundsOperationAdapter(requireContext(), clickListener, data, filter)
    }

    companion object {
        fun newInstance(filter: FlowOfFundsOperationFilter) = FlowOfFundsOperationFragment().apply {
            arguments = createArguments(FLOW_OF_FUNDS_OPERATION_FILTER, filter)
        }
    }
}
