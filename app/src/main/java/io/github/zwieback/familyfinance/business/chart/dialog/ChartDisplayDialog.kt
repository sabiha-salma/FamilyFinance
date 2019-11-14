package io.github.zwieback.familyfinance.business.chart.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

import io.github.zwieback.familyfinance.business.chart.display.ChartDisplay
import io.github.zwieback.familyfinance.business.chart.listener.ChartDisplayListener

abstract class ChartDisplayDialog<D : ChartDisplay<D>, B : ViewDataBinding> : DialogFragment() {

    protected lateinit var binding: B
    protected lateinit var display: D
    private lateinit var listener: ChartDisplayListener<D>

    protected abstract val inputDisplayName: String

    @get:StringRes
    protected abstract val dialogTitle: Int

    @get:LayoutRes
    protected abstract val dialogLayoutId: Int

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ChartDisplayListener<*>) {
            listener = context as ChartDisplayListener<D>
        } else {
            throw ClassCastException("$context must implement ChartDisplayListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inputDisplay = extractDisplay()
        display = createCopyOfDisplay(inputDisplay)
        binding = createBinding()
        bind(display)
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle(extractDialogTitle())
            .setPositiveButton(android.R.string.ok) { _, _ ->
                updateDisplayProperties()
                listener.onApplyDisplay(display)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                // default behavior
            }
            .create()
    }

    private fun extractDisplay(): D {
        return requireArguments().getParcelable(inputDisplayName)
            ?: error("No display with $inputDisplayName name")
    }

    @StringRes
    private fun extractDialogTitle(): Int {
        return requireArguments().getInt(DIALOG_TITLE, dialogTitle)
    }

    /**
     * A copy is needed not to overwrite the values of the original display's
     * fields.
     *
     * @param display an input display
     * @return a copy of input display
     */
    protected abstract fun createCopyOfDisplay(display: D): D

    private fun createBinding(): B {
        val inflater = LayoutInflater.from(context)
        return DataBindingUtil.inflate(inflater, dialogLayoutId, null, false)
    }

    protected abstract fun bind(display: D)

    protected abstract fun updateDisplayProperties()

    companion object {
        const val DIALOG_TITLE = "dialogTitle"

        fun <D : ChartDisplay<*>> createArguments(
            displayName: String,
            display: D
        ): Bundle {
            return Bundle().apply {
                putParcelable(displayName, display)
            }
        }

        fun <D : ChartDisplay<*>> createArguments(
            displayName: String,
            display: D,
            @StringRes dialogTitleId: Int
        ): Bundle {
            return Bundle().apply {
                putParcelable(displayName, display)
                putInt(DIALOG_TITLE, dialogTitleId)
            }
        }
    }
}
