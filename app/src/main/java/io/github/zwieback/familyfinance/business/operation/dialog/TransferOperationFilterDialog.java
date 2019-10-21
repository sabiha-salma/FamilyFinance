package io.github.zwieback.familyfinance.business.operation.dialog;

import android.os.Bundle;
import android.widget.EditText;

import com.johnpetitto.validator.ValidatingTextInputLayout;

import org.jetbrains.annotations.NotNull;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter;
import io.github.zwieback.familyfinance.databinding.DialogFilterTransferOperationBinding;
import io.github.zwieback.familyfinance.widget.ClearableEditText;

import static io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter.TRANSFER_OPERATION_FILTER;

public class TransferOperationFilterDialog extends OperationFilterDialog<TransferOperationFilter,
        DialogFilterTransferOperationBinding> {

    public static TransferOperationFilterDialog newInstance(TransferOperationFilter filter) {
        TransferOperationFilterDialog fragment = new TransferOperationFilterDialog();
        Bundle args = Companion.createArguments(TRANSFER_OPERATION_FILTER, filter);
        fragment.setArguments(args);
        return fragment;
    }

    @NotNull
    @Override
    protected final TransferOperationFilter createCopyOfFilter(TransferOperationFilter filter) {
        return new TransferOperationFilter(filter);
    }

    @Override
    protected final String getInputFilterName() {
        return TRANSFER_OPERATION_FILTER;
    }

    @Override
    protected final int getDialogTitle() {
        return R.string.transfer_operation_filter_title;
    }

    @Override
    protected final int getDialogLayoutId() {
        return R.layout.dialog_filter_transfer_operation;
    }

    @Override
    protected void bind(@NotNull TransferOperationFilter filter) {
        getBinding().setFilter(filter);
        loadArticle(getDatabasePrefs().getTransferArticleId());
        super.bind(filter);
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
