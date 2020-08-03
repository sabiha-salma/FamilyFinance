package io.github.zwieback.familyfinance.business.operation.activity.helper

import android.content.Context
import android.content.Intent
import io.github.zwieback.familyfinance.business.operation.activity.ExpenseOperationEditActivity
import io.github.zwieback.familyfinance.business.operation.activity.ExpenseOperationEditActivity.Companion.INPUT_EXPENSE_ACCOUNT_ID
import io.github.zwieback.familyfinance.business.operation.activity.ExpenseOperationEditActivity.Companion.INPUT_EXPENSE_ARTICLE_ID
import io.github.zwieback.familyfinance.business.operation.activity.ExpenseOperationEditActivity.Companion.INPUT_EXPENSE_CURRENCY_ID
import io.github.zwieback.familyfinance.business.operation.activity.ExpenseOperationEditActivity.Companion.INPUT_EXPENSE_DATE
import io.github.zwieback.familyfinance.business.operation.activity.ExpenseOperationEditActivity.Companion.INPUT_EXPENSE_DESCRIPTION
import io.github.zwieback.familyfinance.business.operation.activity.ExpenseOperationEditActivity.Companion.INPUT_EXPENSE_EXCHANGE_RATE_ID
import io.github.zwieback.familyfinance.business.operation.activity.ExpenseOperationEditActivity.Companion.INPUT_EXPENSE_OPERATION_ID
import io.github.zwieback.familyfinance.business.operation.activity.ExpenseOperationEditActivity.Companion.INPUT_EXPENSE_OWNER_ID
import io.github.zwieback.familyfinance.business.operation.activity.ExpenseOperationEditActivity.Companion.INPUT_EXPENSE_URL
import io.github.zwieback.familyfinance.business.operation.activity.ExpenseOperationEditActivity.Companion.INPUT_EXPENSE_VALUE
import io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter
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

class ExpenseOperationHelper(context: Context, data: ReactiveEntityStore<Persistable>) :
    OperationHelper<ExpenseOperationFilter>(context, data) {

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
        val expensesArticleId = runBlocking(Dispatchers.IO) {
            databasePrefs.expensesArticleId
        }
        if (articleId != expensesArticleId) {
            preparedIntent.putExtra(INPUT_EXPENSE_ARTICLE_ID, articleId)
        }
        accountId?.let {
            preparedIntent.putExtra(INPUT_EXPENSE_ACCOUNT_ID, accountId)
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

    override fun getIntentToAdd(filter: ExpenseOperationFilter?): Intent {
        return if (filter == null) {
            getEmptyIntent()
        } else {
            getIntentToAdd(
                filter.takeArticleId(), filter.takeAccountId(), null,
                filter.takeOwnerId(), filter.takeCurrencyId(), null, null, null, null, null
            )
        }
    }

    override fun getIntentToEdit(operation: OperationView): Intent {
        return getEmptyIntent()
            .putExtra(INPUT_EXPENSE_OPERATION_ID, operation.id)
    }

    override fun getIntentToDuplicate(operation: OperationView): Intent {
        return getIntentToAdd(
            operation.articleId,
            operation.accountId,
            null,
            operation.ownerId,
            operation.currencyId,
            operation.exchangeRateId,
            operation.date,
            operation.value,
            operation.description,
            operation.url
        )
    }

    override fun getEmptyIntent(): Intent {
        return Intent(context, ExpenseOperationEditActivity::class.java)
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
        return (articleId != null
                && accountId != null
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
            .putExtra(INPUT_OPERATION_TYPE, OperationType.EXPENSE_OPERATION)
    }

    override fun addOperationImmediately(
        intent: Intent,
        onSuccess: Consumer<Operation>
    ): Disposable {
        val accountId = intent.getIntExtra(INPUT_EXPENSE_ACCOUNT_ID, 0)
        val articleId = intent.getIntExtra(INPUT_EXPENSE_ARTICLE_ID, 0)
        val ownerId = intent.getIntExtra(INPUT_EXPENSE_OWNER_ID, 0)
        val exchangeRateId = intent.getIntExtra(INPUT_EXPENSE_EXCHANGE_RATE_ID, 0)
        val date = intent.getLocalDateExtra(INPUT_EXPENSE_DATE)
        val value = intent.getBigDecimalExtra(INPUT_EXPENSE_VALUE)
        val description = intent.getStringExtra(INPUT_EXPENSE_DESCRIPTION)
        val url = intent.getStringExtra(INPUT_EXPENSE_URL)

        return Maybe.zip<Account, Article, Person, ExchangeRate, Operation>(
            data.findByKey(Account::class.java, accountId),
            data.findByKey(Article::class.java, articleId),
            data.findByKey(Person::class.java, ownerId),
            data.findByKey(ExchangeRate::class.java, exchangeRateId),
            Function4 { account, article, owner, exchangeRate ->
                Operation()
                    .setCreateDate(LocalDateTime.now())
                    .setLastChangeDate(LocalDateTime.now())
                    .setType(OperationType.EXPENSE_OPERATION)
                    .setAccount(account)
                    .setArticle(article)
                    .setOwner(owner)
                    .setExchangeRate(exchangeRate)
                    .setDate(date)
                    .setValue(value)
                    .setDescription(description)
                    .setUrl(url)
            }
        )
            .flatMapSingle { operation -> data.insert(operation) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }
}
