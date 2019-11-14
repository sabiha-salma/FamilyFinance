package io.github.zwieback.familyfinance.business.operation.dialog

import android.widget.EditText
import androidx.annotation.StringRes
import com.johnpetitto.validator.ValidatingTextInputLayout
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.article.activity.ExpenseArticleActivity
import io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter
import io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter.Companion.EXPENSE_OPERATION_FILTER
import io.github.zwieback.familyfinance.databinding.DialogFilterExpenseOperationBinding
import io.github.zwieback.familyfinance.widget.ClearableEditText

class ExpenseOperationFilterDialog :
    OperationWithArticleFilterDialog<ExpenseOperationFilter, DialogFilterExpenseOperationBinding, ExpenseArticleActivity>() {

    override val inputFilterName: String
        get() = EXPENSE_OPERATION_FILTER

    override val dialogTitle: Int
        get() = R.string.expense_operation_filter_title

    override val dialogLayoutId: Int
        get() = R.layout.dialog_filter_expense_operation

    override val articleActivityClass: Class<ExpenseArticleActivity>
        get() = ExpenseArticleActivity::class.java

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

    override fun createCopyOfFilter(filter: ExpenseOperationFilter): ExpenseOperationFilter {
        return ExpenseOperationFilter(filter)
    }

    override fun bind(filter: ExpenseOperationFilter) {
        binding.filter = filter
        super.bind(filter)
    }

    companion object {
        fun newInstance(filter: ExpenseOperationFilter) = ExpenseOperationFilterDialog().apply {
            arguments = createArguments(EXPENSE_OPERATION_FILTER, filter)
        }

        fun newInstance(
            filter: ExpenseOperationFilter,
            @StringRes dialogTitleId: Int
        ): ExpenseOperationFilterDialog {
            return ExpenseOperationFilterDialog().apply {
                arguments = createArguments(EXPENSE_OPERATION_FILTER, filter).apply {
                    putInt(DIALOG_TITLE, dialogTitleId)
                }
            }
        }
    }
}
