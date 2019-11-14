package io.github.zwieback.familyfinance.business.operation.dialog

import android.widget.EditText
import androidx.annotation.StringRes
import com.johnpetitto.validator.ValidatingTextInputLayout
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.article.activity.IncomeArticleActivity
import io.github.zwieback.familyfinance.business.operation.filter.IncomeOperationFilter
import io.github.zwieback.familyfinance.business.operation.filter.IncomeOperationFilter.Companion.INCOME_OPERATION_FILTER
import io.github.zwieback.familyfinance.databinding.DialogFilterIncomeOperationBinding
import io.github.zwieback.familyfinance.widget.ClearableEditText

class IncomeOperationFilterDialog :
    OperationWithArticleFilterDialog<IncomeOperationFilter, DialogFilterIncomeOperationBinding, IncomeArticleActivity>() {

    override val inputFilterName: String
        get() = INCOME_OPERATION_FILTER

    override val dialogTitle: Int
        get() = R.string.income_operation_filter_title

    override val dialogLayoutId: Int
        get() = R.layout.dialog_filter_income_operation

    override val articleActivityClass: Class<IncomeArticleActivity>
        get() = IncomeArticleActivity::class.java

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

    override fun createCopyOfFilter(filter: IncomeOperationFilter): IncomeOperationFilter {
        return IncomeOperationFilter(filter)
    }

    override fun bind(filter: IncomeOperationFilter) {
        binding.filter = filter
        super.bind(filter)
    }

    companion object {
        fun newInstance(filter: IncomeOperationFilter) = IncomeOperationFilterDialog().apply {
            arguments = createArguments(INCOME_OPERATION_FILTER, filter)
        }

        fun newInstance(
            filter: IncomeOperationFilter,
            @StringRes dialogTitleId: Int
        ): IncomeOperationFilterDialog {
            return IncomeOperationFilterDialog().apply {
                arguments = createArguments(INCOME_OPERATION_FILTER, filter).apply {
                    putInt(DIALOG_TITLE, dialogTitleId)
                }
            }
        }
    }
}
