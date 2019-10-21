package io.github.zwieback.familyfinance.business.operation.dialog;

import android.os.Bundle;
import android.widget.EditText;
import androidx.annotation.StringRes;

import com.johnpetitto.validator.ValidatingTextInputLayout;

import org.jetbrains.annotations.NotNull;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.business.article.activity.IncomeArticleActivity;
import io.github.zwieback.familyfinance.business.operation.filter.IncomeOperationFilter;
import io.github.zwieback.familyfinance.core.dialog.EntityFilterDialog;
import io.github.zwieback.familyfinance.databinding.DialogFilterIncomeOperationBinding;
import io.github.zwieback.familyfinance.widget.ClearableEditText;

import static io.github.zwieback.familyfinance.business.operation.filter.IncomeOperationFilter.INCOME_OPERATION_FILTER;

public class IncomeOperationFilterDialog extends OperationWithArticleFilterDialog<
        IncomeOperationFilter, DialogFilterIncomeOperationBinding, IncomeArticleActivity> {

    public static IncomeOperationFilterDialog newInstance(IncomeOperationFilter filter) {
        IncomeOperationFilterDialog fragment = new IncomeOperationFilterDialog();
        Bundle args = Companion.createArguments(INCOME_OPERATION_FILTER, filter);
        fragment.setArguments(args);
        return fragment;
    }

    public static IncomeOperationFilterDialog newInstance(IncomeOperationFilter filter,
                                                          @StringRes int dialogTitleId) {
        IncomeOperationFilterDialog fragment = new IncomeOperationFilterDialog();
        Bundle args = Companion.createArguments(INCOME_OPERATION_FILTER, filter);
        args.putInt(EntityFilterDialog.DIALOG_TITLE, dialogTitleId);
        fragment.setArguments(args);
        return fragment;
    }

    @NotNull
    @Override
    protected final IncomeOperationFilter createCopyOfFilter(IncomeOperationFilter filter) {
        return new IncomeOperationFilter(filter);
    }

    @Override
    protected final String getInputFilterName() {
        return INCOME_OPERATION_FILTER;
    }

    @Override
    protected final int getDialogTitle() {
        return R.string.income_operation_filter_title;
    }

    @Override
    protected final int getDialogLayoutId() {
        return R.layout.dialog_filter_income_operation;
    }

    @Override
    protected final void bind(@NotNull IncomeOperationFilter filter) {
        getBinding().setFilter(filter);
        super.bind(filter);
    }

    @Override
    final Class<IncomeArticleActivity> getArticleActivityClass() {
        return IncomeArticleActivity.class;
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
