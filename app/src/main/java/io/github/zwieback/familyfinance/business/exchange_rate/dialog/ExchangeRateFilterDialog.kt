package io.github.zwieback.familyfinance.business.exchange_rate.dialog

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.widget.EditText
import com.johnpetitto.validator.ValidatingTextInputLayout
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.currency.activity.CurrencyActivity
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.CURRENCY_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_CURRENCY_ID
import io.github.zwieback.familyfinance.business.exchange_rate.filter.ExchangeRateFilter
import io.github.zwieback.familyfinance.business.exchange_rate.filter.ExchangeRateFilter.Companion.EXCHANGE_RATE_FILTER
import io.github.zwieback.familyfinance.core.dialog.EntityFilterDialog
import io.github.zwieback.familyfinance.core.model.Currency
import io.github.zwieback.familyfinance.databinding.DialogFilterExchangeRateBinding
import io.github.zwieback.familyfinance.util.DateUtils.calendarDateToLocalDate
import io.github.zwieback.familyfinance.util.DateUtils.localDateToString
import io.github.zwieback.familyfinance.util.DateUtils.stringToLocalDate
import io.github.zwieback.familyfinance.util.DialogUtils.showDatePickerDialog
import io.github.zwieback.familyfinance.util.NumberUtils.stringToBigDecimal
import io.github.zwieback.familyfinance.widget.ClearableEditText
import io.reactivex.functions.Consumer
import org.threeten.bp.LocalDate

class ExchangeRateFilterDialog :
    EntityFilterDialog<ExchangeRateFilter, DialogFilterExchangeRateBinding>() {

    override val inputFilterName: String
        get() = EXCHANGE_RATE_FILTER

    override val dialogTitle: Int
        get() = R.string.exchange_rate_filter_title

    override val dialogLayoutId: Int
        get() = R.layout.dialog_filter_exchange_rate

    override val layoutsForValidation: List<ValidatingTextInputLayout>
        get() {
            val layouts = mutableListOf<ValidatingTextInputLayout>()
            if (!startDateEdit.text?.toString().isNullOrEmpty()) {
                layouts.add(startDateLayout)
            }
            if (!endDateEdit.text?.toString().isNullOrEmpty()) {
                layouts.add(endDateLayout)
            }
            if (!startValueEdit.text?.toString().isNullOrEmpty()) {
                layouts.add(startValueLayout)
            }
            if (!endValueEdit.text?.toString().isNullOrEmpty()) {
                layouts.add(endValueLayout)
            }
            return layouts
        }

    private val currencyEdit: ClearableEditText
        get() = binding.currency

    private val startDateEdit: EditText
        get() = binding.startDate

    private val endDateEdit: EditText
        get() = binding.endDate

    private val startValueEdit: EditText
        get() = binding.startValue

    private val endValueEdit: EditText
        get() = binding.endValue

    private val startDateLayout: ValidatingTextInputLayout
        get() = binding.startDateLayout

    private val endDateLayout: ValidatingTextInputLayout
        get() = binding.endDateLayout

    private val startValueLayout: ValidatingTextInputLayout
        get() = binding.startValueLayout

    private val endValueLayout: ValidatingTextInputLayout
        get() = binding.endValueLayout

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultIntent)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            CURRENCY_CODE -> resultIntent?.let {
                val currencyId = extractId(resultIntent, RESULT_CURRENCY_ID)
                loadCurrency(currencyId)
            }
        }
    }

    override fun createCopyOfFilter(filter: ExchangeRateFilter): ExchangeRateFilter {
        return ExchangeRateFilter(filter)
    }

    override fun bind(filter: ExchangeRateFilter) {
        binding.filter = filter

        currencyEdit.setOnClickListener { onCurrencyClick() }
        currencyEdit.setOnClearTextListener { onCurrencyRemoved() }
        startDateEdit.setOnClickListener { onStartDateClick() }
        endDateEdit.setOnClickListener { onEndDateClick() }

        loadCurrency(filter.getCurrencyId())

        super.bind(filter)
    }

    private fun onCurrencyClick() {
        val intent = Intent(context, CurrencyActivity::class.java)
        startActivityForResult(intent, CURRENCY_CODE)
    }

    private fun onStartDateClick() {
        val startDate = determineDate(startDateEdit, filter.startDate)
        showDatePickerDialog(
            requireContext(),
            startDate,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val date = calendarDateToLocalDate(year, month, day)
                loadStartDate(date)
            })
    }

    private fun onEndDateClick() {
        val endDate = determineDate(endDateEdit, filter.endDate)
        showDatePickerDialog(
            requireContext(),
            endDate,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val date = calendarDateToLocalDate(year, month, day)
                loadEndDate(date)
            })
    }

    private fun onCurrencyRemoved() {
        filter.setCurrencyId(null)
    }

    private fun onSuccessfulCurrencyFound(): Consumer<Currency> {
        return Consumer { foundCurrency ->
            filter.setCurrencyId(foundCurrency.id)
            currencyEdit.setText(foundCurrency.name)
        }
    }

    private fun loadCurrency(currencyId: Int?) {
        currencyId?.let {
            loadEntity(Currency::class.java, currencyId, onSuccessfulCurrencyFound())
        }
    }

    private fun loadStartDate(date: LocalDate?) {
        filter.startDate = date
        startDateEdit.setText(localDateToString(date))
    }

    private fun loadEndDate(date: LocalDate?) {
        filter.endDate = date
        endDateEdit.setText(localDateToString(date))
    }

    /**
     * Update filter.
     * Don't check for `null` because the check was completed in [noneErrorFound].
     */
    override fun updateFilterProperties() {
        filter.startDate = stringToLocalDate(startDateEdit.text?.toString())
        filter.endDate = stringToLocalDate(endDateEdit.text?.toString())
        filter.startValue = stringToBigDecimal(startValueEdit.text?.toString())
        filter.endValue = stringToBigDecimal(endValueEdit.text?.toString())
    }

    companion object {
        fun newInstance(filter: ExchangeRateFilter) = ExchangeRateFilterDialog().apply {
            arguments = createArguments(EXCHANGE_RATE_FILTER, filter)
        }
    }
}
