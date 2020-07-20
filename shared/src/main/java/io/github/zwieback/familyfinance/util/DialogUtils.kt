package io.github.zwieback.familyfinance.util

import android.app.DatePickerDialog
import android.content.Context
import androidx.fragment.app.FragmentManager
import io.github.zwieback.familyfinance.calculator.dialog.CalculatorDialog
import io.github.zwieback.familyfinance.calculator.dialog.OnCalculationResultListener
import io.github.zwieback.familyfinance.dialog.DatePickerFragmentDialog
import io.github.zwieback.familyfinance.extension.toCalendarDateWithMonthFix
import org.threeten.bp.LocalDate
import java.math.BigDecimal

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
        val (year, month, day) = date.toCalendarDateWithMonthFix()
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
