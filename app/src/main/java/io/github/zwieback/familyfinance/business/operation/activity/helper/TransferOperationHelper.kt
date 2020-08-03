package io.github.zwieback.familyfinance.business.operation.activity.helper

import android.content.Context
import android.content.Intent
import io.github.zwieback.familyfinance.business.operation.activity.TransferOperationEditActivity
import io.github.zwieback.familyfinance.business.operation.activity.TransferOperationEditActivity.Companion.INPUT_EXPENSE_ACCOUNT_ID
import io.github.zwieback.familyfinance.business.operation.activity.TransferOperationEditActivity.Companion.INPUT_EXPENSE_CURRENCY_ID
import io.github.zwieback.familyfinance.business.operation.activity.TransferOperationEditActivity.Companion.INPUT_EXPENSE_DATE
import io.github.zwieback.familyfinance.business.operation.activity.TransferOperationEditActivity.Companion.INPUT_EXPENSE_DESCRIPTION
import io.github.zwieback.familyfinance.business.operation.activity.TransferOperationEditActivity.Companion.INPUT_EXPENSE_EXCHANGE_RATE_ID
import io.github.zwieback.familyfinance.business.operation.activity.TransferOperationEditActivity.Companion.INPUT_EXPENSE_OWNER_ID
import io.github.zwieback.familyfinance.business.operation.activity.TransferOperationEditActivity.Companion.INPUT_EXPENSE_URL
import io.github.zwieback.familyfinance.business.operation.activity.TransferOperationEditActivity.Companion.INPUT_EXPENSE_VALUE
import io.github.zwieback.familyfinance.business.operation.activity.TransferOperationEditActivity.Companion.INPUT_INCOME_ACCOUNT_ID
import io.github.zwieback.familyfinance.business.operation.activity.TransferOperationEditActivity.Companion.INPUT_TRANSFER_OPERATION_ID
import io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter
import io.github.zwieback.familyfinance.business.operation.lifecycle.destroyer.TransferOperationForceDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.model.*
import io.github.zwieback.familyfinance.core.model.type.OperationType
import io.github.zwieback.familyfinance.extension.getBigDecimalExtra
import io.github.zwieback.familyfinance.extension.getLocalDateExtra
import io.github.zwieback.familyfinance.extension.putBigDecimalExtra
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import java.math.BigDecimal

class TransferOperationHelper(context: Context, data: ReactiveEntityStore<Persistable>) :
    OperationHelper<TransferOperationFilter>(context, data) {

    override fun getIntentToAdd(): Intent {
        return getEmptyIntent()
    }

    override fun getIntentToAdd(
        articleId: Int?,
        accountId: Int?,
        transferAccountId: Int?,
        ownerId: Int?,
        currencyId: Int?,
        exchangeRateId: Int?,
        date: LocalDate?,
        value: BigDecimal?,
        description: String?,
        url: String?
    ): Intent {
        val intent = getEmptyIntent()
        return getIntentToAdd(
            intent,
            articleId,
            accountId, transferAccountId,
            ownerId,
            currencyId, exchangeRateId,
            date, value,
            description, url
        )
    }

    override fun getIntentToAdd(
        preparedIntent: Intent,
        articleId: Int?,
        accountId: Int?,
        transferAccountId: Int?,
        ownerId: Int?,
        currencyId: Int?,
        exchangeRateId: Int?,
        date: LocalDate?,
        value: BigDecimal?,
        description: String?,
        url: String?
    ): Intent {
        accountId?.let {
            preparedIntent.putExtra(INPUT_EXPENSE_ACCOUNT_ID, accountId)
        }
        transferAccountId?.let {
            preparedIntent.putExtra(INPUT_INCOME_ACCOUNT_ID, transferAccountId)
        }
        ownerId?.let {
            preparedIntent.putExtra(INPUT_EXPENSE_OWNER_ID, ownerId)
        }
        currencyId?.let {
            preparedIntent.putExtra(INPUT_EXPENSE_CURRENCY_ID, currencyId)
        }
        exchangeRateId?.let {
            preparedIntent.putExtra(INPUT_EXPENSE_EXCHANGE_RATE_ID, exchangeRateId)
        }
        date?.let {
            preparedIntent.putExtra(INPUT_EXPENSE_DATE, date)
        }
        value?.let {
            preparedIntent.putBigDecimalExtra(INPUT_EXPENSE_VALUE, value)
        }
        if (!description.isNullOrEmpty()) {
            preparedIntent.putExtra(INPUT_EXPENSE_DESCRIPTION, description)
        }
        if (!url.isNullOrEmpty()) {
            preparedIntent.putExtra(INPUT_EXPENSE_URL, url)
        }
        return preparedIntent
    }

    override fun getIntentToAdd(filter: TransferOperationFilter?): Intent {
        return if (filter == null) {
            getEmptyIntent()
        } else {
            getIntentToAdd(
                null, filter.takeAccountId(), null,
                filter.takeOwnerId(), filter.takeCurrencyId(), null, null, null, null, null
            )
        }
    }

    override fun getIntentToEdit(operation: OperationView): Intent {
        val intent = getEmptyIntent()
        intent.putExtra(
            INPUT_TRANSFER_OPERATION_ID,
            TransferOperationQualifier.determineTransferExpenseOperationId(operation)
        )
        return intent
    }

    override fun getIntentToDuplicate(operation: OperationView): Intent {
        val expenseOperation = TransferOperationFinder.findExpenseOperation(data, operation)
        val incomeOperation = TransferOperationFinder.findIncomeOperation(data, operation)
        return getIntentToAdd(
            null,
            expenseOperation.accountId,
            incomeOperation.accountId,
            expenseOperation.ownerId,
            expenseOperation.currencyId,
            expenseOperation.exchangeRateId,
            expenseOperation.date,
            expenseOperation.value,
            expenseOperation.description,
            expenseOperation.url
        )
    }

    override fun getEmptyIntent(): Intent {
        return Intent(context, TransferOperationEditActivity::class.java)
    }

    override fun validToAddImmediately(
        articleId: Int?,
        accountId: Int?,
        transferAccountId: Int?,
        ownerId: Int?,
        currencyId: Int?,
        exchangeRateId: Int?,
        date: LocalDate?,
        value: BigDecimal?,
        description: String?,
        url: String?
    ): Boolean {
        return (accountId != null
                && transferAccountId != null
                && ownerId != null
                && exchangeRateId != null
                && date != null
                && value != null)
    }

    override fun getIntentToAddImmediately(
        articleId: Int?,
        accountId: Int?,
        transferAccountId: Int?,
        ownerId: Int?,
        currencyId: Int?,
        exchangeRateId: Int?,
        date: LocalDate?,
        value: BigDecimal?,
        description: String?,
        url: String?
    ): Intent {
        val intent = getEmptyIntentToAddImmediately()
        return getIntentToAdd(
            intent,
            articleId,
            accountId, transferAccountId,
            ownerId,
            currencyId, exchangeRateId,
            date, value,
            description, url
        )
    }

    override fun getEmptyIntentToAddImmediately(): Intent {
        return super.getEmptyIntentToAddImmediately()
            .putExtra(INPUT_OPERATION_TYPE, OperationType.TRANSFER_EXPENSE_OPERATION)
    }

    /**
     * What's going on here:
     *
     * 1. Created transfer expense operation without linked (transfer income)
     * operation.
     * 2. Created duplicate of transfer expense operation with income type and
     * transfer expense operation as linked operation, that was created on
     * the previous step.
     * 3. Updated transfer expense operation with transfer income operation as
     * linked operation, that was created on the previous step.
     */
    override fun addOperationImmediately(
        intent: Intent,
        onSuccess: Consumer<Operation>
    ): Disposable {
        val transferArticleId = runBlocking(Dispatchers.IO) {
            databasePrefs.transferArticleId
        }
        val expenseAccountId = intent.getIntExtra(INPUT_EXPENSE_ACCOUNT_ID, 0)
        val incomeAccountId = intent.getIntExtra(INPUT_INCOME_ACCOUNT_ID, 0)
        val ownerId = intent.getIntExtra(INPUT_EXPENSE_OWNER_ID, 0)
        val exchangeRateId = intent.getIntExtra(INPUT_EXPENSE_EXCHANGE_RATE_ID, 0)
        val date = intent.getLocalDateExtra(INPUT_EXPENSE_DATE)
        val value = intent.getBigDecimalExtra(INPUT_EXPENSE_VALUE)
        val description = intent.getStringExtra(INPUT_EXPENSE_DESCRIPTION)
        val url = intent.getStringExtra(INPUT_EXPENSE_URL)
        return Maybe.zip<Account, Article, Person, ExchangeRate, Operation>(
            data.findByKey(Account::class.java, expenseAccountId),
            data.findByKey(Article::class.java, transferArticleId),
            data.findByKey(Person::class.java, ownerId),
            data.findByKey(ExchangeRate::class.java, exchangeRateId),
            Function4 { expenseAccount, article, owner, exchangeRate ->
                Operation()
                    .setCreateDate(LocalDateTime.now())
                    .setLastChangeDate(LocalDateTime.now())
                    .setType(OperationType.TRANSFER_EXPENSE_OPERATION)
                    .setAccount(expenseAccount)
                    .setArticle(article)
                    .setOwner(owner)
                    .setExchangeRate(exchangeRate)
                    .setDate(date)
                    .setValue(value)
                    .setDescription(description)
                    .setUrl(url)
            }
        )
            .flatMapSingle { transferExpenseOperation ->
                data.insert(transferExpenseOperation)
                    .flatMap {
                        data.findByKey(Account::class.java, incomeAccountId)
                            .flatMapSingle { incomeAccount ->
                                data.insert(
                                    Operation()
                                        .setCreateDate(LocalDateTime.now())
                                        .setLastChangeDate(LocalDateTime.now())
                                        .setType(OperationType.TRANSFER_INCOME_OPERATION)
                                        .setAccount(incomeAccount)
                                        .setLinkedTransferOperation(transferExpenseOperation)
                                        .setArticle(transferExpenseOperation.article)
                                        .setOwner(transferExpenseOperation.owner)
                                        .setExchangeRate(transferExpenseOperation.exchangeRate)
                                        .setDate(date)
                                        .setValue(value)
                                        .setDescription(description)
                                        .setUrl(url)
                                )
                                    .flatMap { transferIncomeOperation ->
                                        transferExpenseOperation.setLinkedTransferOperation(
                                            transferIncomeOperation
                                        )
                                        data.update(transferExpenseOperation)
                                    }
                            }
                    }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    override fun createDestroyer(operation: OperationView): EntityDestroyer<Operation> {
        return TransferOperationForceDestroyer(context, data)
    }
}
