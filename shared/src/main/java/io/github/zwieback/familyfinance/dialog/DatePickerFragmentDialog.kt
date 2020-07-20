package io.github.zwieback.familyfinance.dialog

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import io.github.zwieback.familyfinance.extension.toCalendarDateWithMonthFix
import org.threeten.bp.LocalDate

class DatePickerFragmentDialog : DialogFragment(), OnDateSetListener {

    private lateinit var dateSetListener: OnDateSetListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDateSetListener) {
            this.dateSetListener = context
        } else {
            throw ClassCastException("$context must implement OnDateSetListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = requireArguments().getSerializable(DATE_KEY) as LocalDate
        val (year, month, day) = date.toCalendarDateWithMonthFix()
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        dateSetListener.onDateSet(view, year, month, day)
    }

    companion object {
        private const val DATE_KEY = "date"
        fun newInstance(date: LocalDate): DatePickerFragmentDialog {
            val fragment = DatePickerFragmentDialog()
            val args = Bundle()
            args.putSerializable(DATE_KEY, date)
            fragment.arguments = args
            return fragment
        }
    }
}
