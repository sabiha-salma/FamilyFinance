package io.github.zwieback.familyfinance.app.lifecycle.destroyer

import android.util.Log
import io.github.zwieback.familyfinance.business.account.lifecycle.destroyer.AccountViewDestroyer
import io.github.zwieback.familyfinance.business.operation.lifecycle.destroyer.OperationViewDestroyer
import io.github.zwieback.familyfinance.business.sms_pattern.lifecycle.destroyer.SmsPatternViewDestroyer
import io.github.zwieback.familyfinance.business.template.lifecycle.destroyer.TemplateViewDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityViewDestroyer
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.sql.Connection

class DatabaseViewDestroyer(private val connection: Connection) {

    fun destroyViews() {
        destroyView(OperationViewDestroyer(connection))
        destroyView(AccountViewDestroyer(connection))
        destroyView(TemplateViewDestroyer(connection))
        destroyView(SmsPatternViewDestroyer(connection))
    }

    private fun <T : EntityViewDestroyer> destroyView(destroyer: T) {
        Single.fromCallable(destroyer)
            .flatMap { single -> single }
            .subscribeOn(Schedulers.trampoline())
            .subscribe { _ -> logFinishOfDestroyer(destroyer.javaClass) }
    }

    companion object {
        private const val TAG = "DatabaseViewDestroyer"

        private fun logFinishOfDestroyer(destroyerClass: Class<*>) {
            Log.d(TAG, "Destroyer '${destroyerClass.simpleName}' is finished")
        }
    }
}
