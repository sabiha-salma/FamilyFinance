package io.github.zwieback.familyfinance.app.lifecycle.creator

import android.content.Context
import android.util.Log
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.account.lifecycle.creator.AccountCreator
import io.github.zwieback.familyfinance.business.article.lifecycle.creator.ArticleEntriesCreator
import io.github.zwieback.familyfinance.business.article.lifecycle.creator.ArticleFoldersCreator
import io.github.zwieback.familyfinance.business.article.lifecycle.creator.ArticleRootCreator
import io.github.zwieback.familyfinance.business.article.lifecycle.creator.exception.NoArticleFoundException
import io.github.zwieback.familyfinance.business.currency.lifecycle.creator.CurrencyCreator
import io.github.zwieback.familyfinance.business.exchange_rate.lifecycle.creator.ExchangeRateCreator
import io.github.zwieback.familyfinance.business.person.lifecycle.creator.PersonCreator
import io.github.zwieback.familyfinance.business.sms_pattern.lifecycle.creator.SmsPatternCreator
import io.github.zwieback.familyfinance.business.template.lifecycle.creator.TemplateCreator
import io.github.zwieback.familyfinance.core.lifecycle.creator.EntityCreator
import io.github.zwieback.familyfinance.core.model.*
import io.github.zwieback.familyfinance.core.preference.config.DatabasePrefs
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class DatabaseTableCreator(
    private val context: Context,
    private val data: ReactiveEntityStore<Persistable>
) {
    private val databasePrefs: DatabasePrefs = runBlocking(Dispatchers.IO) {
        DatabasePrefs.with(context)
    }

    fun createTables() {
        val currencyCreator = CurrencyCreator(context, data)
        createTable(currencyCreator, onCurrenciesCreated())
    }

    private fun <E : IBaseEntity> createTable(
        creator: EntityCreator<E>,
        onEntitiesCreated: Consumer<Iterable<E>>
    ) {
        Single.fromCallable(creator)
            .flatMap { single -> single }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe(onEntitiesCreated)
    }

    private fun onCurrenciesCreated(): Consumer<Iterable<Currency>> {
        return Consumer { currencies ->
            val currency = currencies.iterator().next()
            databasePrefs.currencyId = currency.id
            logFinishOfCreator(CurrencyCreator::class.java)

            createTable(ExchangeRateCreator(context, data), onExchangeRatesCreated())
            createTable(PersonCreator(context, data), onPeopleCreated())
        }
    }

    private fun onExchangeRatesCreated(): Consumer<Iterable<ExchangeRate>> {
        return Consumer {
            logFinishOfCreator(ExchangeRateCreator::class.java)
        }
    }

    private fun onPeopleCreated(): Consumer<Iterable<Person>> {
        return Consumer { people ->
            val chief = people.iterator().next()
            databasePrefs.personId = chief.id
            logFinishOfCreator(PersonCreator::class.java)

            createTable(AccountCreator(context, data), onAccountsCreated())
            createTable(ArticleRootCreator(context, data), onArticleRootsCreated())
        }
    }

    private fun onAccountsCreated(): Consumer<Iterable<Account>> {
        return Consumer { accounts ->
            val account = accounts.iterator().next()
            databasePrefs.accountId = account.id
            logFinishOfCreator(AccountCreator::class.java)
        }
    }

    private fun onArticleRootsCreated(): Consumer<Iterable<Article>> {
        return Consumer { articles ->
            val incomesName = context.resources.getString(R.string.article_incomes)
            val expensesName = context.resources.getString(R.string.article_expenses)

            val incomesArticle = findArticle(articles, incomesName)
            val expensesArticle = findArticle(articles, expensesName)
            databasePrefs.incomesArticleId = incomesArticle.id
            databasePrefs.expensesArticleId = expensesArticle.id
            logFinishOfCreator(ArticleRootCreator::class.java)

            createTable(ArticleFoldersCreator(context, data), onArticleFoldersCreated())
        }
    }

    private fun onArticleFoldersCreated(): Consumer<Iterable<Article>> {
        return Consumer {
            logFinishOfCreator(ArticleFoldersCreator::class.java)

            createTable(ArticleEntriesCreator(context, data), onArticleEntriesCreated())
        }
    }

    private fun onArticleEntriesCreated(): Consumer<Iterable<Article>> {
        return Consumer { articles ->
            val transferName = context.resources.getString(R.string.article_transfer)
            val transferArticle = findArticle(articles, transferName)
            databasePrefs.transferArticleId = transferArticle.id

            logFinishOfCreator(ArticleEntriesCreator::class.java)

            createTable(TemplateCreator(context, data), onTemplatesCreated())
        }
    }

    private fun onTemplatesCreated(): Consumer<Iterable<Template>> {
        return Consumer {
            logFinishOfCreator(TemplateCreator::class.java)

            createTable(SmsPatternCreator(context, data), onSmsPatternCreated())
        }
    }

    private fun onSmsPatternCreated(): Consumer<Iterable<SmsPattern>> {
        return Consumer {
            logFinishOfCreator(SmsPatternCreator::class.java)

            Log.d(TAG, "Tables were created")
        }
    }

    companion object {
        private const val TAG = "DatabaseTableCreator"

        private fun findArticle(articles: Iterable<Article>, name: String): Article {
            return articles
                .find { article -> name == article.name }
                ?: throw NoArticleFoundException(name)
        }

        private fun logFinishOfCreator(creatorClass: Class<*>) {
            Log.d(TAG, "Creator '${creatorClass.simpleName}' is finished")
        }
    }
}
