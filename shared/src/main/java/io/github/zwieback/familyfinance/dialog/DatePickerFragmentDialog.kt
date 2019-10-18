package io.github.zwieback.familyfinance.dialog

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import io.github.zwieback.familyfinance.util.DateUtils.localDateToCalendar
import io.github.zwieback.familyfinance.util.DateUtils.readLocalDateFromBundle
import io.github.zwieback.familyfinance.util.DateUtils.writeLocalDateToBundle
import org.threeten.bp.LocalDate
import java.util.*

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
        val date = readLocalDateFromBundle(requireArguments())
        val calendar = localDateToCalendar(date)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        dateSetListener.onDateSet(view, year, month, day)
    }

    companion object {
        fun newInstance(date: LocalDate): DatePickerFragmentDialog {
            val fragment = DatePickerFragmentDialog()
            val args = Bundle()
            writeLocalDateToBundle(args, date)
            fragment.arguments = args
            return fragment
        }
    }
}
