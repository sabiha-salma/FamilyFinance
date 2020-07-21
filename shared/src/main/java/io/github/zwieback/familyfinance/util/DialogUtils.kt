package io.github.zwieback.familyfinance.util

import android.app.DatePickerDialog
import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
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

    fun showSingleChoiceDialog(
        context: Context,
        @StringRes titleId: Int,
        @ArrayRes itemsId: Int,
        onSelectItem: (Int) -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle(titleId)
            .setItems(itemsId) { _, which ->
                // The 'which' argument contains the index position of the selected item
                onSelectItem(which)
            }
            .create()
            .show()
    }
}
