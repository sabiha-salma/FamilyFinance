package io.github.zwieback.familyfinance.business.operation.activity

import android.app.Activity
import android.content.Intent
import android.widget.EditText
import com.johnpetitto.validator.ValidatingTextInputLayout
import com.mikepenz.iconics.view.IconicsImageView
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.EXPENSE_ACCOUNT_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.INCOME_ACCOUNT_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_ACCOUNT_ID
import io.github.zwieback.familyfinance.business.operation.service.provider.TransferOperationProvider
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.Account
import io.github.zwieback.familyfinance.core.model.Article
import io.github.zwieback.familyfinance.core.model.Operation
import io.github.zwieback.familyfinance.core.model.type.OperationType
import io.github.zwieback.familyfinance.databinding.ActivityEditTransferOperationBinding
import io.github.zwieback.familyfinance.extension.isEmptyId
import io.github.zwieback.familyfinance.widget.ClearableEditText
import io.reactivex.functions.Consumer
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import java.math.BigDecimal

class TransferOperationEditActivity :
    OperationEditActivity<ActivityEditTransferOperationBinding>() {

    /**
     * internal [entity] is alias for expense operation
     */
    private lateinit var incomeOperation: Operation

    override val titleStringId: Int
        get() = R.string.transfer_operation_activity_edit_title

    override val bindingLayoutId: Int
        get() = R.layout.activity_edit_transfer_operation

    override val extraInputId: String
        get() = INPUT_TRANSFER_OPERATION_ID

    override val extraOutputId: String
        get() = OUTPUT_TRANSFER_OPERATION_ID

    override val operationType: OperationType
        get() = OperationType.TRANSFER_EXPENSE_OPERATION

    override val layoutsForValidation: List<ValidatingTextInputLayout>
        get() = listOf(
            binding.ownerLayout,
            binding.expenseAccountLayout,
            binding.incomeAccountLayout,
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
        return TransferOperationProvider(this)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultIntent)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            EXPENSE_ACCOUNT_CODE -> resultIntent?.let {
                val expenseAccountId = extractOutputId(resultIntent, RESULT_ACCOUNT_ID)
                loadExpenseAccount(expenseAccountId)
            }
            INCOME_ACCOUNT_CODE -> resultIntent?.let {
                val incomeAccountId = extractOutputId(resultIntent, RESULT_ACCOUNT_ID)
                loadIncomeAccount(incomeAccountId)
            }
        }
    }

    private fun onExpenseAccountClick() {
        startAccountActivity(EXPENSE_ACCOUNT_CODE)
    }

    private fun onIncomeAccountClick() {
        startAccountActivity(INCOME_ACCOUNT_CODE)
    }

    private fun onSuccessfulExpenseAccountFound(): Consumer<Account> {
        return Consumer { foundAccount ->
            entity.setAccount(foundAccount)
            foundAccount.owner?.id?.let { ownerId -> loadOwner(ownerId) }
        }
    }

    private fun onSuccessfulIncomeAccountFound(): Consumer<Account> {
        return Consumer { foundAccount -> incomeOperation.setAccount(foundAccount) }
    }

    private fun onSuccessfulArticleFound(): Consumer<Article> {
        return Consumer { foundArticle -> entity.setArticle(foundArticle) }
    }

    private fun loadExpenseAccount(accountId: Int) {
        loadEntity(Account::class.java, accountId, onSuccessfulExpenseAccountFound())
    }

    private fun loadIncomeAccount(accountId: Int) {
        loadEntity(Account::class.java, accountId, onSuccessfulIncomeAccountFound())
    }

    private fun loadDefaultArticle() {
        val transferArticleId = databasePrefs.transferArticleId
        loadEntity(Article::class.java, transferArticleId, onSuccessfulArticleFound())
    }

    private fun extractExpenseAccountId(): Int {
        return extractInputId(INPUT_EXPENSE_ACCOUNT_ID, databasePrefs.accountId)
    }

    private fun extractIncomeAccountId(): Int {
        return extractInputId(INPUT_INCOME_ACCOUNT_ID)
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
        createIncomeOperation()
        loadDefaultArticle()
        loadExpenseAccount(extractExpenseAccountId())
        loadIncomeAccount(extractIncomeAccountId())
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

    private fun createIncomeOperation() {
        incomeOperation = Operation()
            .setCreateDate(LocalDateTime.now())
            .setType(OperationType.TRANSFER_INCOME_OPERATION)
        bindIncomeOperation(incomeOperation)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun loadEntity(expenseOperationId: Int) {
        loadEntity(
            Operation::class.java,
            expenseOperationId
        ) { onSuccessfulExpenseOperationFound(it) }
    }

    private fun onSuccessfulExpenseOperationFound(expenseOperation: Operation) {
        bind(expenseOperation)
        loadEntity(
            Operation::class.java,
            expenseOperation.linkedTransferOperation?.id ?: error("No linked income operation")
        ) { bindIncomeOperation(it) }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun bind(operation: Operation) {
        entity = operation
        binding.expenseOperation = operation
        provider.setupIcon(binding.icon.icon, operation)
        super.bind(operation)
    }

    private fun bindIncomeOperation(operation: Operation) {
        incomeOperation = operation
        binding.incomeOperation = operation
        setupIncomeOperationBindings()
    }

    override fun setupBindings() {
        binding.icon.setOnClickListener { onSelectIconClick() }
        binding.owner.setOnClickListener { onOwnerClick() }
        binding.expenseAccount.setOnClickListener { onExpenseAccountClick() }
        binding.expenseAccount.setOnClearTextListener { entity.setAccount(null) }
        binding.date.setOnClickListener { onDateClick() }
        binding.currency.setOnClickListener { onCurrencyClick() }
        binding.exchangeRate.setOnClickListener { onExchangeRateClick() }
        super.setupBindings()
    }

    private fun setupIncomeOperationBindings() {
        binding.incomeAccount.setOnClickListener { onIncomeAccountClick() }
        binding.incomeAccount.setOnClearTextListener { incomeOperation.setAccount(null) }
    }

    override fun updateEntityProperties(operation: Operation) {
        super.updateEntityProperties(operation)
        incomeOperation.setLastChangeDate(LocalDateTime.now())
        incomeOperation.setArticle(operation.article)
        incomeOperation.setOwner(operation.owner)
        incomeOperation.setExchangeRate(operation.exchangeRate)
        incomeOperation.setDate(operation.date)
        incomeOperation.setValue(operation.value)
        incomeOperation.setDescription(operation.description)
        incomeOperation.setUrl(operation.url)
    }

    override fun onSuccessfulSaving(): Consumer<Operation> {
        return Consumer { expenseOperation ->
            incomeOperation.setLinkedTransferOperation(expenseOperation)
            saveEntity(incomeOperation, onSuccessfulIncomeOperationSaving(expenseOperation))
        }
    }

    private fun onSuccessfulIncomeOperationSaving(expenseOperation: Operation): Consumer<Operation> {
        return Consumer { incomeOperation ->
            expenseOperation.setLinkedTransferOperation(incomeOperation)
            saveEntity(expenseOperation, onSuccessfulExpenseOperationSaving())
        }
    }

    private fun onSuccessfulExpenseOperationSaving(): Consumer<Operation> {
        return Consumer { closeActivity(it) }
    }

    companion object {
        const val INPUT_TRANSFER_OPERATION_ID = "transferExpenseOperationId"
        const val INPUT_EXPENSE_ACCOUNT_ID = "expenseAccountId"
        const val INPUT_INCOME_ACCOUNT_ID = "incomeAccountId"
        const val INPUT_EXPENSE_OWNER_ID = "expenseOwnerId"
        const val INPUT_EXPENSE_CURRENCY_ID = "expenseCurrencyId"
        const val INPUT_EXPENSE_EXCHANGE_RATE_ID = "expenseExchangeRateId"
        const val INPUT_EXPENSE_VALUE = "expenseValue"
        const val INPUT_EXPENSE_DATE = "expenseDate"
        const val INPUT_EXPENSE_DESCRIPTION = "expenseDescription"
        const val INPUT_EXPENSE_URL = "expenseUrl"
        const val OUTPUT_TRANSFER_OPERATION_ID = "resultTransferExpenseOperationId"
    }
}
