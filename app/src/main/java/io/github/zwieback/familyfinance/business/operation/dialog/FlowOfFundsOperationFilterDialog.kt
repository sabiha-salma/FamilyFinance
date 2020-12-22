package io.github.zwieback.familyfinance.business.operation.dialog

import android.widget.CheckBox
import android.widget.EditText
import androidx.annotation.StringRes
import com.johnpetitto.validator.ValidatingTextInputLayout
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.article.activity.AllArticleActivity
import io.github.zwieback.familyfinance.business.operation.filter.FlowOfFundsOperationFilter
import io.github.zwieback.familyfinance.business.operation.filter.FlowOfFundsOperationFilter.Companion.FLOW_OF_FUNDS_OPERATION_FILTER
import io.github.zwieback.familyfinance.databinding.DialogFilterFlowOfFundsOperationBinding
import io.github.zwieback.familyfinance.widget.ClearableEditText

class FlowOfFundsOperationFilterDialog :
    OperationWithArticleFilterDialog<FlowOfFundsOperationFilter, DialogFilterFlowOfFundsOperationBinding, AllArticleActivity>() {

    override val inputFilterName: String
        get() = FLOW_OF_FUNDS_OPERATION_FILTER

    override val dialogTitle: Int
        get() = R.string.flow_of_funds_operation_filter_title

    override val dialogLayoutId: Int
        get() = R.layout.dialog_filter_flow_of_funds_operation

    override val articleActivityClass: Class<AllArticleActivity>
        get() = AllArticleActivity::class.java

    override val articleEdit: ClearableEditText
        get() = binding.article

    override val accountEdit: ClearableEditText
        get() = binding.account

    override val ownerEdit: ClearableEditText
        get() = binding.owner

    override val toWhomIsNullCheckBox: CheckBox
        get() = binding.toWhomIsNullCheckBox

    override val toWhomEdit: ClearableEditText
        get() = binding.toWhom

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

    override fun createCopyOfFilter(filter: FlowOfFundsOperationFilter): FlowOfFundsOperationFilter {
        return filter.copy()
    }

    override fun bind(filter: FlowOfFundsOperationFilter) {
        binding.filter = filter
        super.bind(filter)
    }

    companion object {
        fun newInstance(filter: FlowOfFundsOperationFilter): FlowOfFundsOperationFilterDialog {
            return FlowOfFundsOperationFilterDialog().apply {
                arguments = createArguments(FLOW_OF_FUNDS_OPERATION_FILTER, filter)
            }
        }

        fun newInstance(
            filter: FlowOfFundsOperationFilter,
            @StringRes dialogTitleId: Int
        ): FlowOfFundsOperationFilterDialog {
            return FlowOfFundsOperationFilterDialog().apply {
                arguments = createArguments(FLOW_OF_FUNDS_OPERATION_FILTER, filter).apply {
                    putInt(DIALOG_TITLE, dialogTitleId)
                }
            }
        }
    }
}
