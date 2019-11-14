package io.github.zwieback.familyfinance.business.operation.fragment

import io.github.zwieback.familyfinance.business.operation.adapter.TransferOperationAdapter
import io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter
import io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter.Companion.TRANSFER_OPERATION_FILTER

class TransferOperationFragment : OperationFragment<TransferOperationFilter>() {

    override fun createEntityAdapter(): TransferOperationAdapter {
        val filter = extractFilter(TRANSFER_OPERATION_FILTER)
        return TransferOperationAdapter(requireContext(), clickListener, data, filter)
    }

    companion object {
        fun newInstance(filter: TransferOperationFilter) = TransferOperationFragment().apply {
            arguments = createArguments(TRANSFER_OPERATION_FILTER, filter)
        }
    }
}
