package io.github.zwieback.familyfinance.business.operation.fragment

import io.github.zwieback.familyfinance.business.operation.adapter.TransferOperationAdapter
import io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter
import io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter.Companion.TRANSFER_OPERATION_FILTER

class TransferOperationFragment : OperationFragment<TransferOperationFilter>() {

    override val filterName: String
        get() = TRANSFER_OPERATION_FILTER

    override fun createEntityAdapter(): TransferOperationAdapter {
        val filter = extractFilter()
        return TransferOperationAdapter(requireContext(), clickListener, data, filter)
    }

    companion object {
        fun newInstance(filter: TransferOperationFilter) = TransferOperationFragment().apply {
            arguments = createArguments(TRANSFER_OPERATION_FILTER, filter)
        }
    }
}
