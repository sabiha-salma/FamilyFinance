package io.github.zwieback.familyfinance.business.operation.dialog

import android.widget.EditText
import com.johnpetitto.validator.ValidatingTextInputLayout
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter
import io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter.Companion.TRANSFER_OPERATION_FILTER
import io.github.zwieback.familyfinance.databinding.DialogFilterTransferOperationBinding
import io.github.zwieback.familyfinance.widget.ClearableEditText

class TransferOperationFilterDialog :
    OperationFilterDialog<TransferOperationFilter, DialogFilterTransferOperationBinding>() {

    override val inputFilterName: String
        get() = TRANSFER_OPERATION_FILTER

    override val dialogTitle: Int
        get() = R.string.transfer_operation_filter_title

    override val dialogLayoutId: Int
        get() = R.layout.dialog_filter_transfer_operation

    override val articleEdit: ClearableEditText
        get() = binding.article

    override val accountEdit: ClearableEditText
        get() = binding.account

    override val ownerEdit: ClearableEditText
        get() = binding.owner

    override val currencyEdit: ClearableEditText
        get() = binding.currency

    override val startDateEdit: EditText
        get() = binding.startDate

    override val endDateEdit: EditText
        get() = binding.endDate

    override val startValueEdit: EditText
        get() = binding.startValue

    override val endValueEdit: EditText
        get() = binding.endValue

    override val startDateLayout: ValidatingTextInputLayout
        get() = binding.startDateLayout

    override val endDateLayout: ValidatingTextInputLayout
        get() = binding.endDateLayout

    override val startValueLayout: ValidatingTextInputLayout
        get() = binding.startValueLayout

    override val endValueLayout: ValidatingTextInputLayout
        get() = binding.endValueLayout

    override fun createCopyOfFilter(filter: TransferOperationFilter): TransferOperationFilter {
        return TransferOperationFilter(filter)
    }

    override fun bind(filter: TransferOperationFilter) {
        binding.filter = filter
        loadArticle(databasePrefs.transferArticleId)
        super.bind(filter)
    }

    companion object {
        fun newInstance(filter: TransferOperationFilter) = TransferOperationFilterDialog().apply {
            arguments = createArguments(TRANSFER_OPERATION_FILTER, filter)
        }
    }
}
