package io.github.zwieback.familyfinance.util

import android.app.DatePickerDialog
import android.content.Context
import androidx.fragment.app.FragmentManager
import io.github.zwieback.familyfinance.calculator.dialog.CalculatorDialog
import io.github.zwieback.familyfinance.calculator.dialog.OnCalculationResultListener
import io.github.zwieback.familyfinance.dialog.DatePickerFragmentDialog
import io.github.zwieback.familyfinance.util.DateUtils.localDateToCalendar
import org.threeten.bp.LocalDate
import java.math.BigDecimal
import java.util.*

object DialogUtils {

    fun showDatePickerDialog(fragmentManager: FragmentManager, date: LocalDate) {
        val datePickerFragment = DatePickerFragmentDialog.newInstance(date)
        datePickerFragment.show(fragmentManager, "datePickerDialog")
    }

    fun showDatePickerDialog(
        context: Context,
        date: LocalDate,
        dateSetListener: DatePickerDialog.OnDateSetListener
    ) {
        val calendar = localDateToCalendar(date)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(context, dateSetListener, year, month, day).show()
    }

    fun showCalculatorDialog(
        context: Context,
        listener: OnCalculationResultListener,
        operand: BigDecimal?
    ) {
        CalculatorDialog(context, listener, operand).show()
    }
}
