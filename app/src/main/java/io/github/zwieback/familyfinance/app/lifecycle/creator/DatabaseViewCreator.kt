package io.github.zwieback.familyfinance.app.lifecycle.creator

import android.util.Log
import io.github.zwieback.familyfinance.business.account.lifecycle.creator.AccountViewCreator
import io.github.zwieback.familyfinance.business.article.lifecycle.creator.ArticleViewCreator
import io.github.zwieback.familyfinance.business.currency.lifecycle.creator.CurrencyViewCreator
import io.github.zwieback.familyfinance.business.exchange_rate.lifecycle.creator.ExchangeRateViewCreator
import io.github.zwieback.familyfinance.business.operation.lifecycle.creator.OperationViewCreator
import io.github.zwieback.familyfinance.business.person.lifecycle.creator.PersonViewCreator
import io.github.zwieback.familyfinance.business.sms_pattern.lifecycle.creator.SmsPatternViewCreator
import io.github.zwieback.familyfinance.business.template.lifecycle.creator.TemplateViewCreator
import io.github.zwieback.familyfinance.core.lifecycle.creator.EntityViewCreator
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.sql.Connection

class DatabaseViewCreator(private val connection: Connection) {

    fun createViews() {
        createView(CurrencyViewCreator(connection), onCurrencyViewCreated())
        createView(ExchangeRateViewCreator(connection), onExchangeRateViewCreated())
        createView(PersonViewCreator(connection), onPersonViewCreated())
        createView(AccountViewCreator(connection), onAccountViewCreated())
        createView(ArticleViewCreator(connection), onArticleViewCreated())
        createView(OperationViewCreator(connection), onOperationViewCreated())
        createView(TemplateViewCreator(connection), onTemplateViewCreated())
        createView(SmsPatternViewCreator(connection), onSmsPatternViewCreated())
    }

    private fun createView(creator: EntityViewCreator, onViewCreated: Consumer<Boolean>) {
        Single.fromCallable(creator)
            .flatMap { single -> single }
            .subscribeOn(Schedulers.trampoline())
            .subscribe(onViewCreated)
    }

    private fun onCurrencyViewCreated(): Consumer<Boolean> {
        return Consumer { logFinishOfCreator(CurrencyViewCreator::class.java) }
    }

    private fun onExchangeRateViewCreated(): Consumer<Boolean> {
        return Consumer { logFinishOfCreator(ExchangeRateViewCreator::class.java) }
    }

    private fun onPersonViewCreated(): Consumer<Boolean> {
        return Consumer { logFinishOfCreator(PersonViewCreator::class.java) }
    }

    private fun onAccountViewCreated(): Consumer<Boolean> {
        return Consumer { logFinishOfCreator(AccountViewCreator::class.java) }
    }

    private fun onArticleViewCreated(): Consumer<Boolean> {
        return Consumer { logFinishOfCreator(ArticleViewCreator::class.java) }
    }

    private fun onOperationViewCreated(): Consumer<Boolean> {
        return Consumer { logFinishOfCreator(OperationViewCreator::class.java) }
    }

    private fun onTemplateViewCreated(): Consumer<Boolean> {
        return Consumer { logFinishOfCreator(TemplateViewCreator::class.java) }
    }

    private fun onSmsPatternViewCreated(): Consumer<Boolean> {
        return Consumer { logFinishOfCreator(SmsPatternViewCreator::class.java) }
    }

    companion object {
        private const val TAG = "DatabaseViewCreator"

        private fun logFinishOfCreator(creatorClass: Class<*>) {
            Log.d(TAG, "Creator '${creatorClass.simpleName}' is finished")
        }
    }
}
