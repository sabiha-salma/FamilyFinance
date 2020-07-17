package io.github.zwieback.familyfinance.business.operation.activity

import android.app.Activity
import android.content.Intent
import android.widget.EditText
import com.johnpetitto.validator.ValidatingTextInputLayout
import com.mikepenz.iconics.view.IconicsImageView
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.article.activity.ExpenseArticleActivity
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.ACCOUNT_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.ARTICLE_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_ACCOUNT_ID
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_ARTICLE_ID
import io.github.zwieback.familyfinance.business.operation.service.provider.ExpenseOperationProvider
import io.github.zwieback.familyfinance.core.activity.EntityActivity
import io.github.zwieback.familyfinance.core.activity.EntityFolderActivity
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.Account
import io.github.zwieback.familyfinance.core.model.Article
import io.github.zwieback.familyfinance.core.model.Operation
import io.github.zwieback.familyfinance.core.model.type.OperationType
import io.github.zwieback.familyfinance.databinding.ActivityEditExpenseOperationBinding
import io.github.zwieback.familyfinance.extension.isEmptyId
import io.github.zwieback.familyfinance.util.NumberUtils.bigDecimalToString
import io.github.zwieback.familyfinance.widget.ClearableEditText
import io.reactivex.functions.Consumer
import org.threeten.bp.LocalDate
import java.math.BigDecimal

class ExpenseOperationEditActivity : OperationEditActivity<ActivityEditExpenseOperationBinding>() {

    override val titleStringId: Int
        get() = R.string.expense_operation_activity_edit_title

    override val bindingLayoutId: Int
        get() = R.layout.activity_edit_expense_operation

    override val extraInputId: String
        get() = INPUT_EXPENSE_OPERATION_ID

    override val extraOutputId: String
        get() = OUTPUT_EXPENSE_OPERATION_ID

    override val operationType: OperationType
        get() = OperationType.EXPENSE_OPERATION

    override val layoutsForValidation: List<ValidatingTextInputLayout>
        get() = listOf(
            binding.articleNameLayout,
            binding.articleCategoryLayout,
            binding.expenseAccountLayout,
            binding.ownerLayout,
            binding.valueLayout,
            binding.dateLayout,
            binding.currencyLayout,
            binding.exchangeRateLayout
        )

    override val iconView: IconicsImageView
        get() = binding.icon

    override val ownerEdit: ClearableEditText
        get() = binding.owner

    override val currencyEdit: ClearableEditText
        get() = binding.currency

    override val exchangeRateEdit: ClearableEditText
        get() = binding.exchangeRate

    override val dateEdit: EditText
        get() = binding.date

    override val valueEdit: EditText
        get() = binding.value

    override val descriptionEdit: EditText
        get() = binding.description

    override val urlEdit: EditText
        get() = binding.url

    override fun createProvider(): EntityProvider<Operation> {
        return ExpenseOperationProvider(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultIntent)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            ACCOUNT_CODE -> resultIntent?.let {
                val accountId = extractOutputId(resultIntent, RESULT_ACCOUNT_ID)
                loadAccount(accountId)
            }
            ARTICLE_CODE -> resultIntent?.let {
                val articleId = extractOutputId(resultIntent, RESULT_ARTICLE_ID)
                loadArticle(articleId)
            }
        }
    }

    private fun onAccountClick() {
        startAccountActivity(ACCOUNT_CODE)
    }

    private fun onArticleClick() {
        val intent = Intent(this, ExpenseArticleActivity::class.java)
            .putExtra(EntityActivity.INPUT_READ_ONLY, false)
        startActivityForResult(intent, ARTICLE_CODE)
    }

    /**
     * Suppress here because article may be null after creation
     */
    @Suppress("UNNECESSARY_SAFE_CALL")
    private fun onArticleCategoryClick() {
        val intent = Intent(this, ExpenseArticleActivity::class.java)
        intent.putExtra(EntityActivity.INPUT_READ_ONLY, false)
        entity.article?.parent?.id?.let { articleParentId ->
            intent.putExtra(EntityFolderActivity.INPUT_PARENT_FOLDER_ID, articleParentId)
        }
        startActivityForResult(intent, ARTICLE_CODE)
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
            foundAccount.owner?.id?.let { ownerId -> loadOwner(ownerId) }
        }
    }

    private fun loadArticle(articleId: Int) {
        loadEntity(Article::class.java, articleId, onSuccessfulArticleFound())
    }

    private fun loadAccount(accountId: Int) {
        loadEntity(Account::class.java, accountId, onSuccessfulAccountFound())
    }

    private fun extractExpenseAccountId(): Int {
        return extractInputId(INPUT_EXPENSE_ACCOUNT_ID, databasePrefs.accountId)
    }

    private fun extractExpenseArticleId(): Int {
        return extractInputId(INPUT_EXPENSE_ARTICLE_ID)
    }

    private fun extractExpenseOwnerId(): Int {
        return extractInputId(INPUT_EXPENSE_OWNER_ID, databasePrefs.personId)
    }

    private fun extractExpenseCurrencyId(): Int {
        return extractInputId(INPUT_EXPENSE_CURRENCY_ID, databasePrefs.currencyId)
    }

    private fun extractExpenseExchangeRateId(): Int {
        return extractInputId(INPUT_EXPENSE_EXCHANGE_RATE_ID)
    }

    private fun extractExpenseDate(): LocalDate {
        return extractInputDate(INPUT_EXPENSE_DATE)
    }

    private fun extractExpenseValue(): BigDecimal? {
        return extractInputBigDecimal(INPUT_EXPENSE_VALUE)
    }

    private fun extractExpenseDescription(): String? {
        return extractInputString(INPUT_EXPENSE_DESCRIPTION)
    }

    private fun extractExpenseUrl(): String? {
        return extractInputString(INPUT_EXPENSE_URL)
    }

    override fun createEntity() {
        super.createEntity()
        loadAccount(extractExpenseAccountId())
        loadArticle(extractExpenseArticleId())
        loadOwner(extractExpenseOwnerId())
        val exchangeRateId = extractExpenseExchangeRateId()
        if (exchangeRateId.isEmptyId()) {
            loadCurrency(extractExpenseCurrencyId())
        } else {
            loadExchangeRate(exchangeRateId)
        }
    }

    override fun createOperation(): Operation {
        return super.createOperation()
            .setDate(extractExpenseDate())
            .setValue(extractExpenseValue())
            .setDescription(extractExpenseDescription())
            .setUrl(extractExpenseUrl())
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun bind(operation: Operation) {
        entity = operation
        binding.operation = operation
        binding.value.setTextColor(provider.provideTextColor(operation))
        provider.setupIcon(binding.icon.icon, operation)
        super.bind(operation)
    }

    override fun setupBindings() {
        binding.icon.setOnClickListener { onSelectIconClick() }
        binding.articleName.setOnClickListener { onArticleClick() }
        binding.articleName.setOnClearTextListener { entity.setArticle(null) }
        binding.articleCategory.setOnClickListener { onArticleCategoryClick() }
        binding.articleCategory.setOnClearTextListener { entity.setArticle(null) }
        binding.account.setOnClickListener { onAccountClick() }
        binding.account.setOnClearTextListener { entity.setAccount(null) }
        binding.owner.setOnClickListener { onOwnerClick() }
        binding.date.setOnClickListener { onDateClick() }
        binding.currency.setOnClickListener { onCurrencyClick() }
        binding.exchangeRate.setOnClickListener { onExchangeRateClick() }
        super.setupBindings()
    }

    companion object {
        const val INPUT_EXPENSE_OPERATION_ID = "expenseOperationId"
        const val INPUT_EXPENSE_ACCOUNT_ID = "expenseAccountId"
        const val INPUT_EXPENSE_ARTICLE_ID = "expenseArticleId"
        const val INPUT_EXPENSE_OWNER_ID = "expenseOwnerId"
        const val INPUT_EXPENSE_CURRENCY_ID = "expenseCurrencyId"
        const val INPUT_EXPENSE_EXCHANGE_RATE_ID = "expenseExchangeRateId"
        const val INPUT_EXPENSE_VALUE = "expenseValue"
        const val INPUT_EXPENSE_DATE = "expenseDate"
        const val INPUT_EXPENSE_DESCRIPTION = "expenseDescription"
        const val INPUT_EXPENSE_URL = "expenseUrl"
        const val OUTPUT_EXPENSE_OPERATION_ID = "resultExpenseOperationId"
    }
}
