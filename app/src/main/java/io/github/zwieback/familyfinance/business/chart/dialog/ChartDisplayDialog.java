package io.github.zwieback.familyfinance.business.chart.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;

import io.github.zwieback.familyfinance.business.chart.display.ChartDisplay;
import io.github.zwieback.familyfinance.business.chart.listener.ChartDisplayListener;

public abstract class ChartDisplayDialog<D extends ChartDisplay, B extends ViewDataBinding>
        extends DialogFragment {

    protected static final String DIALOG_TITLE = "dialogTitle";

    B binding;
    D display;
    private ChartDisplayListener<D> listener;

    @SuppressWarnings("unchecked")
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChartDisplayListener) {
            listener = (ChartDisplayListener<D>) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement ChartDisplayListener");
        }
    }

    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        D inputDisplay = extractDisplay();
        display = createCopyOfDisplay(inputDisplay);
        binding = createBinding();
        bind(display);
        return new AlertDialog.Builder(requireContext())
                .setView(binding.getRoot())
                .setTitle(extractDialogTitle())
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    updateDisplayProperties();
                    listener.onApplyDisplay(display);
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    // default behavior
                })
                .create();
    }

    private D extractDisplay() {
        return requireArguments().getParcelable(getInputDisplayName());
    }

    @StringRes
    private int extractDialogTitle() {
        return requireArguments().getInt(DIALOG_TITLE, getDialogTitle());
    }

    /**
     * A copy is needed not to overwrite the values of the fields of the original display.
     *
     * @param display an input display
     * @return a copy of input display
     */
    protected abstract D createCopyOfDisplay(D display);

    protected abstract String getInputDisplayName();

    @StringRes
    protected abstract int getDialogTitle();

    @LayoutRes
    protected abstract int getDialogLayoutId();

    private B createBinding() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        return DataBindingUtil.inflate(inflater, getDialogLayoutId(), null, false);
    }

    protected abstract void bind(D display);

    protected abstract void updateDisplayProperties();

    protected static <D extends ChartDisplay> Bundle createArguments(String displayName,
                                                                     D display) {
        Bundle args = new Bundle();
        args.putParcelable(displayName, display);
        return args;
    }

    protected static <D extends ChartDisplay> Bundle createArguments(String displayName,
                                                                     D display,
                                                                     @StringRes int dialogTitleId) {
        Bundle args = new Bundle();
        args.putParcelable(displayName, display);
        args.putInt(DIALOG_TITLE, dialogTitleId);
        return args;
    }
}
