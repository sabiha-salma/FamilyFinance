package io.github.zwieback.familyfinance.business.operation.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.widget.DatePicker
import android.widget.EditText
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import io.github.zwieback.familyfinance.business.account.activity.AccountActivity
import io.github.zwieback.familyfinance.business.currency.activity.CurrencyActivity
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.CURRENCY_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.EXCHANGE_RATE_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.PERSON_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_CURRENCY_ID
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_EXCHANGE_RATE_ID
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_PERSON_ID
import io.github.zwieback.familyfinance.business.exchange_rate.activity.ExchangeRateActivity
import io.github.zwieback.familyfinance.business.exchange_rate.helper.ExchangeRateFinder
import io.github.zwieback.familyfinance.business.person.activity.PersonActivity
import io.github.zwieback.familyfinance.constant.IdConstants.EMPTY_ID
import io.github.zwieback.familyfinance.core.activity.EntityActivity.Companion.INPUT_READ_ONLY
import io.github.zwieback.familyfinance.core.activity.EntityEditActivity
import io.github.zwieback.familyfinance.core.model.Currency
import io.github.zwieback.familyfinance.core.model.ExchangeRate
import io.github.zwieback.familyfinance.core.model.Operation
import io.github.zwieback.familyfinance.core.model.Person
import io.github.zwieback.familyfinance.core.model.type.OperationType
import io.github.zwieback.familyfinance.extension.CalendarDate
import io.github.zwieback.familyfinance.extension.isLocalDate
import io.github.zwieback.familyfinance.extension.toLocalDate
import io.github.zwieback.familyfinance.extension.toLocalDateWithMonthFix
import io.github.zwieback.familyfinance.extension.toStringOrEmpty
import io.github.zwieback.familyfinance.util.DialogUtils.showDatePickerDialog
import io.github.zwieback.familyfinance.util.NumberUtils.stringToBigDecimal
import io.github.zwieback.familyfinance.widget.ClearableEditText
import io.reactivex.functions.Consumer
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

abstract class OperationEditActivity<B : ViewDataBinding> :
    EntityEditActivity<Operation, B>(),
    DatePickerDialog.OnDateSetListener {

    override val entityClass: Class<Operation>
        get() = Operation::class.java

    private val isCorrectDate: Boolean
        get() = dateEdit.text?.toString().isLocalDate()

    protected abstract val operationType: OperationType

    protected abstract val ownerEdit: ClearableEditText

    protected abstract val currencyEdit: ClearableEditText

    protected abstract val exchangeRateEdit: ClearableEditText

    protected abstract val dateEdit: EditText

    protected abstract val valueEdit: EditText

    protected abstract val descriptionEdit: EditText

    protected abstract val urlEdit: EditText

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultIntent)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            PERSON_CODE -> resultIntent?.let {
                val ownerId = extractOutputId(resultIntent, RESULT_PERSON_ID)
                loadOwner(ownerId)
            }
            CURRENCY_CODE -> resultIntent?.let {
                val currencyId = extractOutputId(resultIntent, RESULT_CURRENCY_ID)
                loadCurrency(currencyId)
            }
            EXCHANGE_RATE_CODE -> resultIntent?.let {
                val exchangeRateId = extractOutputId(resultIntent, RESULT_EXCHANGE_RATE_ID)
                loadExchangeRate(exchangeRateId)
            }
        }
    }

    protected fun onDateClick() {
        val date = determineDate()
        showDatePickerDialog(supportFragmentManager, date)
    }

    /**
     * Don't check for `null` because the check was completed in [isCorrectDate].
     */
    private fun determineDate(): LocalDate {
        return if (isCorrectDate) {
            dateEdit.text?.toString().toLocalDate() ?: error("Date is not correct")
        } else {
            entity.date
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val date = CalendarDate(year, month, day).toLocalDateWithMonthFix()
        dateEdit.setText(date.toStringOrEmpty())
    }

    protected fun onOwnerClick() {
        val intent = Intent(this, PersonActivity::class.java)
        startActivityForResult(intent, PERSON_CODE)
    }

    protected fun onCurrencyClick() {
        val intent = Intent(this, CurrencyActivity::class.java)
            .putExtra(INPUT_READ_ONLY, false)
        startActivityForResult(intent, CURRENCY_CODE)
    }

    protected fun onExchangeRateClick() {
        val intent = Intent(this, ExchangeRateActivity::class.java)
            .putExtra(ExchangeRateActivity.INPUT_CURRENCY_ID, determineCurrencyId())
            .putExtra(INPUT_READ_ONLY, false)
        startActivityForResult(intent, EXCHANGE_RATE_CODE)
    }

    protected fun startAccountActivity(requestCode: Int) {
        val intent = Intent(this, AccountActivity::class.java)
            .putExtra(AccountActivity.INPUT_ONLY_ACTIVE, true)
        startActivityForResult(intent, requestCode)
    }

    /**
     * Suppress here because exchangeRate may be null after creation
     */
    @Suppress("UNNECESSARY_SAFE_CALL", "USELESS_ELVIS")
    private fun determineCurrencyId(): Int {
        return entity.exchangeRate?.currency?.id ?: EMPTY_ID
    }

    private fun findLastExchangeRate(currencyId: Int): ExchangeRate? {
        return ExchangeRateFinder(data).findLastExchangeRate(currencyId)
    }

    private fun onSuccessfulOwnerFound(): Consumer<Person> {
        return Consumer { foundOwner -> entity.setOwner(foundOwner) }
    }

    private fun onSuccessfulCurrencyFound(): Consumer<Currency> {
        return Consumer { foundCurrency ->
            val exchangeRate = findLastExchangeRate(foundCurrency.id)
            entity.setExchangeRate(exchangeRate)
        }
    }

    private fun onSuccessfulExchangeRateFound(): Consumer<ExchangeRate> {
        return Consumer { foundExchangeRate -> entity.setExchangeRate(foundExchangeRate) }
    }

    protected fun loadOwner(ownerId: Int) {
        loadEntity(Person::class.java, ownerId, onSuccessfulOwnerFound())
    }

    protected fun loadCurrency(currencyId: Int) {
        loadEntity(Currency::class.java, currencyId, onSuccessfulCurrencyFound())
    }

    protected fun loadExchangeRate(exchangeRateId: Int) {
        loadEntity(ExchangeRate::class.java, exchangeRateId, onSuccessfulExchangeRateFound())
    }

    override fun createEntity() {
        val operation = createOperation()
        bind(operation)
    }

    protected open fun createOperation(): Operation {
        return Operation()
            .setCreateDate(LocalDateTime.now())
            .setType(operationType)
            .setDate(LocalDate.now())
    }

    @CallSuper
    override fun setupBindings() {
        ownerEdit.setOnClearTextListener { entity.setOwner(null) }
        currencyEdit.setOnClearTextListener { entity.setExchangeRate(null) }
        exchangeRateEdit.setOnClearTextListener { entity.setExchangeRate(null) }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun updateEntityProperties(operation: Operation) {
        operation.setLastChangeDate(LocalDateTime.now())
        operation.setDate(dateEdit.text?.toString().toLocalDate())
        operation.setValue(stringToBigDecimal(valueEdit.text?.toString()))
        operation.setDescription(descriptionEdit.text?.toString())
        operation.setUrl(urlEdit.text?.toString())
    }
}
