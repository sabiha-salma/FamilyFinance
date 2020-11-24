package io.github.zwieback.familyfinance.business.exchange_rate.activity

import android.app.Activity
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.widget.DatePicker
import com.johnpetitto.validator.ValidatingTextInputLayout
import com.mikepenz.iconics.view.IconicsImageView
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.currency.activity.CurrencyActivity
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.CURRENCY_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_CURRENCY_ID
import io.github.zwieback.familyfinance.business.exchange_rate.adapter.ExchangeRateProvider
import io.github.zwieback.familyfinance.core.activity.EntityEditActivity
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.Currency
import io.github.zwieback.familyfinance.core.model.ExchangeRate
import io.github.zwieback.familyfinance.databinding.ActivityEditExchangeRateBinding
import io.github.zwieback.familyfinance.extension.*
import io.github.zwieback.familyfinance.util.DialogUtils.showDatePickerDialog
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class ExchangeRateEditActivity :
    EntityEditActivity<ExchangeRate, ActivityEditExchangeRateBinding>(), OnDateSetListener {

    override val titleStringId: Int
        get() = R.string.exchange_rate_activity_edit_title

    override val bindingLayoutId: Int
        get() = R.layout.activity_edit_exchange_rate

    override val extraInputId: String
        get() = INPUT_EXCHANGE_RATE_ID

    override val extraOutputId: String
        get() = OUTPUT_EXCHANGE_RATE_ID

    override val entityClass: Class<ExchangeRate>
        get() = ExchangeRate::class.java

    private val isCorrectDate: Boolean
        get() = binding.date.text?.toString().isLocalDate()

    override val layoutsForValidation: List<ValidatingTextInputLayout>
        get() = listOf(binding.currencyLayout, binding.valueLayout, binding.dateLayout)

    override val iconView: IconicsImageView
        get() = binding.icon

    override fun createProvider(): EntityProvider<ExchangeRate> {
        return ExchangeRateProvider(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultIntent)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            CURRENCY_CODE -> resultIntent?.let {
                val currencyId = extractOutputId(resultIntent, RESULT_CURRENCY_ID)
                loadCurrency(currencyId)
            }
        }
    }

    private fun onCurrencyClick() {
        val intent = Intent(this, CurrencyActivity::class.java)
        startActivityForResult(intent, CURRENCY_CODE)
    }

    private fun onDateClick() {
        val date = determineDate()
        showDatePickerDialog(supportFragmentManager, date)
    }

    /**
     * Don't check for `null` because the check was completed in [isCorrectDate].
     */
    private fun determineDate(): LocalDate {
        return if (isCorrectDate) {
            binding.date.text?.toString().toLocalDateOrNull() ?: error("Date is not correct")
        } else {
            entity.date
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val date = CalendarDate(year, month, day).toLocalDateWithMonthFix()
        binding.date.setText(date.toStringOrEmpty())
    }

    private fun loadCurrency(currencyId: Int) {
        loadEntity(
            Currency::class.java,
            currencyId
        ) { foundCurrency -> entity.setCurrency(foundCurrency) }
    }

    override fun createEntity() {
        val currencyId = extractInputId(INPUT_CURRENCY_ID)
        val exchangeRate = ExchangeRate()
            .setCreateDate(LocalDateTime.now())
            .setDate(LocalDate.now())
        bind(exchangeRate)
        if (currencyId.isNotEmptyId()) {
            loadCurrency(currencyId)
        }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun bind(exchangeRate: ExchangeRate) {
        entity = exchangeRate
        binding.exchangeRate = exchangeRate
        provider.setupIcon(binding.icon.icon, exchangeRate)
        super.bind(exchangeRate)
    }

    override fun setupBindings() {
        binding.icon.setOnClickListener { onSelectIconClick() }
        binding.currency.setOnClickListener { onCurrencyClick() }
        binding.currency.setOnClearTextListener { entity.setCurrency(null) }
        binding.date.setOnClickListener { onDateClick() }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun updateEntityProperties(exchangeRate: ExchangeRate) {
        exchangeRate.setLastChangeDate(LocalDateTime.now())
        exchangeRate.setValue(binding.value.text?.toString()?.toBigDecimalOrNull())
        exchangeRate.setDate(binding.date.text?.toString().toLocalDateOrNull())
    }

    companion object {
        const val INPUT_EXCHANGE_RATE_ID = "exchangeRateId"
        const val INPUT_CURRENCY_ID = "currencyId"
        const val OUTPUT_EXCHANGE_RATE_ID = "resultExchangeRateId"
    }
}
