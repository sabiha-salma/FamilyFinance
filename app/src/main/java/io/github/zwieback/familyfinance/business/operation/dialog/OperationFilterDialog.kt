package io.github.zwieback.familyfinance.business.operation.dialog

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.widget.CheckBox
import android.widget.EditText
import androidx.databinding.ViewDataBinding
import com.johnpetitto.validator.ValidatingTextInputLayout
import io.github.zwieback.familyfinance.business.account.activity.AccountActivity
import io.github.zwieback.familyfinance.business.currency.activity.CurrencyActivity
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.ACCOUNT_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.CURRENCY_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.PERSON_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_ACCOUNT_ID
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_CURRENCY_ID
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_PERSON_ID
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_TO_WHOM_ID
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.TO_WHOM_CODE
import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter
import io.github.zwieback.familyfinance.business.person.activity.PersonActivity
import io.github.zwieback.familyfinance.business.person.activity.ToWhomActivity
import io.github.zwieback.familyfinance.core.dialog.EntityFilterDialog
import io.github.zwieback.familyfinance.core.model.Account
import io.github.zwieback.familyfinance.core.model.Article
import io.github.zwieback.familyfinance.core.model.Currency
import io.github.zwieback.familyfinance.core.model.Person
import io.github.zwieback.familyfinance.extension.*
import io.github.zwieback.familyfinance.util.DialogUtils.showDatePickerDialog
import io.github.zwieback.familyfinance.widget.ClearableEditText
import io.reactivex.functions.Consumer
import org.threeten.bp.LocalDate
import java.math.BigDecimal

abstract class OperationFilterDialog<F, B> :
    EntityFilterDialog<F, B>()
        where F : OperationFilter,
              B : ViewDataBinding {

    override val layoutsForValidation: List<ValidatingTextInputLayout>
        get() {
            val layouts = mutableListOf<ValidatingTextInputLayout>()
            layouts.add(startDateLayout)
            layouts.add(endDateLayout)
            if (!startValueEdit.text?.toString().isNullOrEmpty()) {
                layouts.add(startValueLayout)
            }
            if (!endValueEdit.text?.toString().isNullOrEmpty()) {
                layouts.add(endValueLayout)
            }
            return layouts
        }

    protected abstract val articleEdit: ClearableEditText

    protected abstract val accountEdit: ClearableEditText

    protected abstract val ownerEdit: ClearableEditText

    protected abstract val toWhomIsNullCheckBox: CheckBox

    protected abstract val toWhomEdit: ClearableEditText

    protected abstract val currencyEdit: ClearableEditText

    protected abstract val startDateEdit: EditText

    protected abstract val endDateEdit: EditText

    protected abstract val startValueEdit: EditText

    protected abstract val endValueEdit: EditText

    protected abstract val startDateLayout: ValidatingTextInputLayout

    protected abstract val endDateLayout: ValidatingTextInputLayout

    protected abstract val startValueLayout: ValidatingTextInputLayout

    protected abstract val endValueLayout: ValidatingTextInputLayout

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultIntent)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            ACCOUNT_CODE -> resultIntent?.let {
                val accountId = extractId(resultIntent, RESULT_ACCOUNT_ID)
                loadAccount(accountId)
            }
            PERSON_CODE -> resultIntent?.let {
                val ownerId = extractId(resultIntent, RESULT_PERSON_ID)
                loadOwner(ownerId)
            }
            TO_WHOM_CODE -> resultIntent?.let {
                val toWhomId = extractId(resultIntent, RESULT_TO_WHOM_ID)
                loadToWhom(toWhomId)
            }
            CURRENCY_CODE -> resultIntent?.let {
                val currencyId = extractId(resultIntent, RESULT_CURRENCY_ID)
                loadCurrency(currencyId)
            }
        }
    }

    override fun bind(filter: F) {
        accountEdit.setOnClickListener { onAccountClick() }
        accountEdit.setOnClearTextListener { onAccountRemoved() }
        ownerEdit.setOnClickListener { onOwnerClick() }
        ownerEdit.setOnClearTextListener { onOwnerRemoved() }
        toWhomIsNullCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onToWhomIsNullCheckedChange(isChecked)
        }
        toWhomEdit.setOnClickListener { onToWhomClick() }
        toWhomEdit.setOnClearTextListener { onToWhomRemoved() }
        currencyEdit.setOnClickListener { onCurrencyClick() }
        currencyEdit.setOnClearTextListener { onCurrencyRemoved() }
        startDateEdit.setOnClickListener { onStartDateClick() }
        endDateEdit.setOnClickListener { onEndDateClick() }

        loadAccount(filter.takeAccountId())
        loadOwner(filter.takeOwnerId())
        loadToWhomIsNull(filter.toWhomIsNull)
        loadToWhom(filter.takeToWhomId())
        loadCurrency(filter.takeCurrencyId())
        loadStartDate(filter.startDate)
        loadEndDate(filter.endDate)
        loadStartValue(filter.startValue)
        loadEndValue(filter.endValue)

        super.bind(filter)
    }

    private fun onAccountClick() {
        val intent = Intent(context, AccountActivity::class.java)
        startActivityForResult(intent, ACCOUNT_CODE)
    }

    private fun onOwnerClick() {
        val intent = Intent(context, PersonActivity::class.java)
        startActivityForResult(intent, PERSON_CODE)
    }

    private fun onToWhomIsNullCheckedChange(isChecked: Boolean) {
        filter.toWhomIsNull = isChecked
        toWhomEdit.isEnabled = !isChecked
    }

    private fun onToWhomClick() {
        val intent = Intent(context, ToWhomActivity::class.java)
        startActivityForResult(intent, TO_WHOM_CODE)
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
                val date = CalendarDate(year, month, day).toLocalDateWithMonthFix()
                loadStartDate(date)
            }
        )
    }

    private fun onEndDateClick() {
        val endDate = determineDate(endDateEdit, filter.endDate)
        showDatePickerDialog(
            requireContext(),
            endDate,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val date = CalendarDate(year, month, day).toLocalDateWithMonthFix()
                loadEndDate(date)
            }
        )
    }

    private fun onAccountRemoved() {
        filter.putAccountId(null)
    }

    private fun onOwnerRemoved() {
        filter.putOwnerId(null)
    }

    private fun onToWhomRemoved() {
        filter.putToWhomId(null)
    }

    private fun onCurrencyRemoved() {
        filter.putCurrencyId(null)
    }

    private fun onSuccessfulArticleFound(): Consumer<Article> {
        return Consumer { foundArticle ->
            filter.putArticleId(foundArticle.id)
            articleEdit.setText(foundArticle.name)
        }
    }

    private fun onSuccessfulAccountFound(): Consumer<Account> {
        return Consumer { foundAccount ->
            filter.putAccountId(foundAccount.id)
            accountEdit.setText(foundAccount.name)
        }
    }

    private fun onSuccessfulOwnerFound(): Consumer<Person> {
        return Consumer { foundOwner ->
            filter.putOwnerId(foundOwner.id)
            ownerEdit.setText(foundOwner.name)
        }
    }

    private fun onSuccessfulToWhomFound(): Consumer<Person> {
        return Consumer { foundPerson ->
            filter.putToWhomId(foundPerson.id)
            toWhomEdit.setText(foundPerson.name)
        }
    }

    private fun onSuccessfulCurrencyFound(): Consumer<Currency> {
        return Consumer { foundCurrency ->
            filter.putCurrencyId(foundCurrency.id)
            currencyEdit.setText(foundCurrency.name)
        }
    }

    fun loadArticle(articleId: Int?) {
        articleId?.let {
            loadEntity(Article::class.java, articleId, onSuccessfulArticleFound())
        }
    }

    private fun loadAccount(accountId: Int?) {
        accountId?.let {
            loadEntity(Account::class.java, accountId, onSuccessfulAccountFound())
        }
    }

    private fun loadOwner(ownerId: Int?) {
        ownerId?.let {
            loadEntity(Person::class.java, ownerId, onSuccessfulOwnerFound())
        }
    }

    private fun loadToWhomIsNull(toWhomIsNull: Boolean) {
        toWhomIsNullCheckBox.isChecked = toWhomIsNull
        toWhomEdit.isEnabled = !toWhomIsNull
    }

    private fun loadToWhom(toWhomId: Int?) {
        toWhomId?.let {
            loadEntity(Person::class.java, toWhomId, onSuccessfulToWhomFound())
        }
    }

    private fun loadCurrency(currencyId: Int?) {
        currencyId?.let {
            loadEntity(Currency::class.java, currencyId, onSuccessfulCurrencyFound())
        }
    }

    private fun loadStartDate(date: LocalDate) {
        filter.startDate = date
        startDateEdit.setText(date.toStringOrEmpty())
    }

    private fun loadEndDate(date: LocalDate) {
        filter.endDate = date
        endDateEdit.setText(date.toStringOrEmpty())
    }

    private fun loadStartValue(value: BigDecimal?) {
        startValueEdit.setText(value.toStringOrEmpty())
    }

    private fun loadEndValue(value: BigDecimal?) {
        endValueEdit.setText(value.toStringOrEmpty())
    }

    /**
     * Update filter.
     * Don't check for `null` because the check was completed in [noneErrorFound].
     */
    override fun updateFilterProperties() {
        filter.startDate = startDateEdit.text?.toString().toLocalDateOrNull()
            ?: error("Are you check the noneErrorFound() method?")
        filter.endDate = endDateEdit.text?.toString().toLocalDateOrNull()
            ?: error("Are you check the noneErrorFound() method?")
        filter.startValue = startValueEdit.text?.toString()?.toBigDecimalOrNull()
        filter.endValue = endValueEdit.text?.toString()?.toBigDecimalOrNull()
    }
}
