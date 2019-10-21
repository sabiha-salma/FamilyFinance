package io.github.zwieback.familyfinance.business.operation.dialog;

import android.os.Bundle;
import android.widget.EditText;
import androidx.annotation.StringRes;

import com.johnpetitto.validator.ValidatingTextInputLayout;

import org.jetbrains.annotations.NotNull;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.business.article.activity.ExpenseArticleActivity;
import io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter;
import io.github.zwieback.familyfinance.core.dialog.EntityFilterDialog;
import io.github.zwieback.familyfinance.databinding.DialogFilterExpenseOperationBinding;
import io.github.zwieback.familyfinance.widget.ClearableEditText;

import static io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter.EXPENSE_OPERATION_FILTER;

public class ExpenseOperationFilterDialog extends OperationWithArticleFilterDialog<
        ExpenseOperationFilter, DialogFilterExpenseOperationBinding, ExpenseArticleActivity> {

    public static ExpenseOperationFilterDialog newInstance(ExpenseOperationFilter filter) {
        ExpenseOperationFilterDialog fragment = new ExpenseOperationFilterDialog();
        Bundle args = Companion.createArguments(EXPENSE_OPERATION_FILTER, filter);
        fragment.setArguments(args);
        return fragment;
    }

    public static ExpenseOperationFilterDialog newInstance(ExpenseOperationFilter filter,
                                                           @StringRes int dialogTitleId) {
        ExpenseOperationFilterDialog fragment = new ExpenseOperationFilterDialog();
        Bundle args = Companion.createArguments(EXPENSE_OPERATION_FILTER, filter);
        args.putInt(EntityFilterDialog.DIALOG_TITLE, dialogTitleId);
        fragment.setArguments(args);
        return fragment;
    }

    @NotNull
    @Override
    protected final ExpenseOperationFilter createCopyOfFilter(ExpenseOperationFilter filter) {
        return new ExpenseOperationFilter(filter);
    }

    @Override
    protected final String getInputFilterName() {
        return EXPENSE_OPERATION_FILTER;
    }

    @Override
    protected final int getDialogTitle() {
        return R.string.expense_operation_filter_title;
    }

    @Override
    protected final int getDialogLayoutId() {
        return R.layout.dialog_filter_expense_operation;
    }

    @Override
    protected final void bind(@NotNull ExpenseOperationFilter filter) {
        getBinding().setFilter(filter);
        super.bind(filter);
    }

    @Override
    final Class<ExpenseArticleActivity> getArticleActivityClass() {
        return ExpenseArticleActivity.class;
    }

    @Override
    final ClearableEditText getArticleEdit() {
        return getBinding().article;
    }

    @Override
    final ClearableEditText getAccountEdit() {
        return getBinding().account;
    }

    @Override
    final ClearableEditText getOwnerEdit() {
        return getBinding().owner;
    }

    @Override
    final ClearableEditText getCurrencyEdit() {
        return getBinding().currency;
    }

    @Override
    final EditText getStartDateEdit() {
        return getBinding().startDate;
    }

    @Override
    final EditText getEndDateEdit() {
        return getBinding().endDate;
    }

    @Override
    final EditText getStartValueEdit() {
        return getBinding().startValue;
    }

    @Override
    final EditText getEndValueEdit() {
        return getBinding().endValue;
    }

    @Override
    final ValidatingTextInputLayout getStartDateLayout() {
        return getBinding().startDateLayout;
    }

    @Override
    final ValidatingTextInputLayout getEndDateLayout() {
        return getBinding().endDateLayout;
    }

    @Override
    final ValidatingTextInputLayout getStartValueLayout() {
        return getBinding().startValueLayout;
    }

    @Override
    final ValidatingTextInputLayout getEndValueLayout() {
        return getBinding().endValueLayout;
    }
}
