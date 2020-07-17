package io.github.zwieback.familyfinance.business.template.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.widget.DatePicker
import com.johnpetitto.validator.ValidatingTextInputLayout
import com.mikepenz.iconics.view.IconicsImageView
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.account.activity.AccountActivity
import io.github.zwieback.familyfinance.business.article.activity.ExpenseArticleActivity
import io.github.zwieback.familyfinance.business.article.activity.IncomeArticleActivity
import io.github.zwieback.familyfinance.business.currency.activity.CurrencyActivity
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.ACCOUNT_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.ARTICLE_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.CURRENCY_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.EXCHANGE_RATE_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.INCOME_ACCOUNT_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.PERSON_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_ACCOUNT_ID
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_ARTICLE_ID
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_CURRENCY_ID
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_EXCHANGE_RATE_ID
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_PERSON_ID
import io.github.zwieback.familyfinance.business.exchange_rate.activity.ExchangeRateActivity
import io.github.zwieback.familyfinance.business.exchange_rate.helper.ExchangeRateFinder
import io.github.zwieback.familyfinance.business.person.activity.PersonActivity
import io.github.zwieback.familyfinance.business.template.adapter.TemplateProvider
import io.github.zwieback.familyfinance.business.template.exception.UnsupportedTemplateTypeException
import io.github.zwieback.familyfinance.core.activity.EntityActivity
import io.github.zwieback.familyfinance.core.activity.EntityEditActivity
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.*
import io.github.zwieback.familyfinance.core.model.type.TemplateType
import io.github.zwieback.familyfinance.databinding.ActivityEditTemplateBinding
import io.github.zwieback.familyfinance.extension.EMPTY_ID
import io.github.zwieback.familyfinance.util.DateUtils
import io.github.zwieback.familyfinance.util.DateUtils.calendarDateToLocalDate
import io.github.zwieback.familyfinance.util.DateUtils.isTextAnLocalDate
import io.github.zwieback.familyfinance.util.DateUtils.localDateToString
import io.github.zwieback.familyfinance.util.DateUtils.stringToLocalDate
import io.github.zwieback.familyfinance.util.DialogUtils.showDatePickerDialog
import io.github.zwieback.familyfinance.util.NumberUtils.bigDecimalToString
import io.github.zwieback.familyfinance.util.NumberUtils.stringToBigDecimal
import io.reactivex.functions.Consumer
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class TemplateEditActivity : EntityEditActivity<Template, ActivityEditTemplateBinding>(),
    DatePickerDialog.OnDateSetListener {

    override val titleStringId: Int
        get() = R.string.template_activity_edit_title

    override val bindingLayoutId: Int
        get() = R.layout.activity_edit_template

    override val extraInputId: String
        get() = INPUT_TEMPLATE_ID

    override val extraOutputId: String
        get() = OUTPUT_TEMPLATE_ID

    override val entityClass: Class<Template>
        get() = Template::class.java

    private val isCorrectDate: Boolean
        get() = isTextAnLocalDate(binding.date.text?.toString())

    override val layoutsForValidation: List<ValidatingTextInputLayout>
        get() {
            val layouts = mutableListOf<ValidatingTextInputLayout>()
            layouts.add(binding.nameLayout)
            if (!binding.date.text?.toString().isNullOrEmpty()) {
                layouts.add(binding.dateLayout)
            }
            if (!binding.value.text?.toString().isNullOrEmpty()) {
                layouts.add(binding.valueLayout)
            }
            return layouts
        }

    override val iconView: IconicsImageView
        get() = binding.icon

    override fun createProvider(): EntityProvider<Template> {
        return TemplateProvider(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultIntent)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            ARTICLE_CODE -> resultIntent?.let {
                val articleId = extractOutputId(resultIntent, RESULT_ARTICLE_ID)
                loadArticle(articleId)
            }
            ACCOUNT_CODE -> resultIntent?.let {
                val accountId = extractOutputId(resultIntent, RESULT_ACCOUNT_ID)
                loadAccount(accountId)
            }
            INCOME_ACCOUNT_CODE -> resultIntent?.let {
                val transferAccountId = extractOutputId(resultIntent, RESULT_ACCOUNT_ID)
                loadTransferAccount(transferAccountId)
            }
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

    private fun onDateClick() {
        val date = determineDate()
        showDatePickerDialog(supportFragmentManager, date)
    }

    /**
     * Don't check for `null` because the check was completed in [isCorrectDate].
     */
    private fun determineDate(): LocalDate {
        return if (isCorrectDate) {
            stringToLocalDate(binding.date.text?.toString()) ?: error("Date is not correct")
        } else {
            entity.date ?: DateUtils.now()
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val date = calendarDateToLocalDate(year, month, day)
        binding.date.setText(localDateToString(date))
    }

    private fun onArticleClick() {
        val intent = Intent(this, determineArticleActivityClass())
        intent.putExtra(EntityActivity.INPUT_READ_ONLY, false)
        startActivityForResult(intent, ARTICLE_CODE)
    }

    private fun determineArticleActivityClass(): Class<*> {
        return when (entity.type) {
            TemplateType.EXPENSE_OPERATION -> ExpenseArticleActivity::class.java
            TemplateType.INCOME_OPERATION -> IncomeArticleActivity::class.java
            else -> throw UnsupportedTemplateTypeException(entity.id, entity.type)
        }
    }

    private fun onAccountClick() {
        val intent = Intent(this, AccountActivity::class.java)
            .putExtra(AccountActivity.INPUT_ONLY_ACTIVE, true)
        startActivityForResult(intent, ACCOUNT_CODE)
    }

    private fun onTransferAccountClick() {
        val intent = Intent(this, AccountActivity::class.java)
            .putExtra(AccountActivity.INPUT_ONLY_ACTIVE, true)
        startActivityForResult(intent, INCOME_ACCOUNT_CODE)
    }

    private fun onOwnerClick() {
        val intent = Intent(this, PersonActivity::class.java)
        startActivityForResult(intent, PERSON_CODE)
    }

    private fun onCurrencyClick() {
        val intent = Intent(this, CurrencyActivity::class.java)
        startActivityForResult(intent, CURRENCY_CODE)
    }

    private fun onExchangeRateClick() {
        val intent = Intent(this, ExchangeRateActivity::class.java)
            .putExtra(ExchangeRateActivity.INPUT_CURRENCY_ID, determineCurrencyId())
        startActivityForResult(intent, EXCHANGE_RATE_CODE)
    }

    private fun determineCurrencyId(): Int {
        return entity.exchangeRate?.currency?.id ?: EMPTY_ID
    }

    private fun findLastExchangeRate(currencyId: Int): ExchangeRate? {
        return ExchangeRateFinder(data).findLastExchangeRate(currencyId)
    }

    private fun onSuccessfulArticleFound(): Consumer<Article> {
        return Consumer { foundArticle ->
            entity.setArticle(foundArticle)
            if (binding.value.text?.toString().isNullOrEmpty()) {
                foundArticle.defaultValue?.let { defaultValue ->
                    binding.value.setText(bigDecimalToString(defaultValue))
                }
            }
        }
    }

    private fun onSuccessfulAccountFound(): Consumer<Account> {
        return Consumer { foundAccount ->
            entity.setAccount(foundAccount)
            if (entity.owner == null) {
                foundAccount.owner?.let { owner ->
                    loadOwner(owner.id)
                }
            }
            if (entity.exchangeRate == null) {
                foundAccount.currency?.let { currency ->
                    loadCurrency(currency.id)
                }
            }
        }
    }

    private fun onSuccessfulTransferAccountFound(): Consumer<Account> {
        return Consumer { foundTransferAccount -> entity.setTransferAccount(foundTransferAccount) }
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

    private fun loadArticle(articleId: Int) {
        loadEntity(Article::class.java, articleId, onSuccessfulArticleFound())
    }

    private fun loadAccount(accountId: Int) {
        loadEntity(Account::class.java, accountId, onSuccessfulAccountFound())
    }

    private fun loadTransferAccount(transferAccountId: Int) {
        loadEntity(Account::class.java, transferAccountId, onSuccessfulTransferAccountFound())
    }

    private fun loadOwner(ownerId: Int) {
        loadEntity(Person::class.java, ownerId, onSuccessfulOwnerFound())
    }

    private fun loadCurrency(currencyId: Int) {
        loadEntity(Currency::class.java, currencyId, onSuccessfulCurrencyFound())
    }

    private fun loadExchangeRate(exchangeRateId: Int) {
        loadEntity(ExchangeRate::class.java, exchangeRateId, onSuccessfulExchangeRateFound())
    }

    private fun extractType(): TemplateType {
        val type = extractInputString(INPUT_TEMPLATE_TYPE) ?: error("No type in the input intent")
        return TemplateType.valueOf(type)
    }

    override fun createEntity() {
        val template = Template()
            .setCreateDate(LocalDateTime.now())
            .setType(extractType())
        bind(template)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun bind(template: Template) {
        entity = template
        binding.template = template
        binding.value.setTextColor(provider.provideTextColor(template))
        provider.setupIcon(binding.icon.icon, template)
        super.bind(template)
    }

    override fun setupBindings() {
        binding.icon.setOnClickListener { onSelectIconClick() }
        binding.articleName.setOnClickListener { onArticleClick() }
        binding.articleName.setOnClearTextListener { entity.setArticle(null) }
        binding.articleCategory.setOnClickListener { onArticleClick() }
        binding.articleCategory.setOnClearTextListener { entity.setArticle(null) }
        binding.account.setOnClickListener { onAccountClick() }
        binding.account.setOnClearTextListener { entity.setAccount(null) }
        binding.transferAccount.setOnClickListener { onTransferAccountClick() }
        binding.transferAccount.setOnClearTextListener { entity.setTransferAccount(null) }
        binding.owner.setOnClickListener { onOwnerClick() }
        binding.owner.setOnClearTextListener { entity.setOwner(null) }
        binding.currency.setOnClickListener { onCurrencyClick() }
        binding.currency.setOnClearTextListener { entity.setExchangeRate(null) }
        binding.exchangeRate.setOnClickListener { onExchangeRateClick() }
        binding.exchangeRate.setOnClearTextListener { entity.setExchangeRate(null) }
        binding.date.setOnClickListener { onDateClick() }
        binding.date.setOnClearTextListener { entity.setDate(null) }

        if (TemplateType.TRANSFER_OPERATION === entity.type) {
            loadArticle(databasePrefs.transferArticleId)
            disableLayout(binding.articleNameLayout, R.string.hint_article_name_disabled)
            disableLayout(binding.articleCategoryLayout, R.string.hint_article_category_disabled)
        } else {
            disableLayout(binding.transferAccountLayout, R.string.hint_transfer_account_disabled)
        }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun updateEntityProperties(template: Template) {
        template.setLastChangeDate(LocalDateTime.now())
        template.setName(binding.name.text?.toString())
        template.setDate(stringToLocalDate(binding.date.text?.toString()))
        template.setValue(stringToBigDecimal(binding.value.text?.toString()))
        template.setDescription(binding.description.text?.toString())
        template.setUrl(binding.url.text?.toString())
    }

    companion object {
        const val INPUT_TEMPLATE_ID = "templateId"
        const val INPUT_TEMPLATE_TYPE = "templateType"
        const val OUTPUT_TEMPLATE_ID = "resultTemplateId"
    }
}
