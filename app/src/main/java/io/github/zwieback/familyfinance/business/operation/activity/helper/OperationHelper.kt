package io.github.zwieback.familyfinance.business.operation.activity.helper

import android.content.Context
import android.content.Intent
import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter
import io.github.zwieback.familyfinance.business.operation.lifecycle.destroyer.OperationForceDestroyer
import io.github.zwieback.familyfinance.business.sms.service.AddOperationImmediatelyService
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.model.Operation
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.core.preference.config.DatabasePrefs
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore
import org.threeten.bp.LocalDate
import java.math.BigDecimal

abstract class OperationHelper<FILTER : OperationFilter>(
    protected val context: Context,
    protected val data: ReactiveEntityStore<Persistable>
) {
    protected val databasePrefs: DatabasePrefs = DatabasePrefs.with(context)

    abstract fun getIntentToAdd(): Intent

    abstract fun getIntentToAdd(
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
    ): Intent

    protected abstract fun getIntentToAdd(
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
    ): Intent

    abstract fun getIntentToAdd(filter: FILTER?): Intent

    abstract fun getIntentToEdit(operation: OperationView): Intent

    abstract fun getIntentToDuplicate(operation: OperationView): Intent

    protected abstract fun getEmptyIntent(): Intent

    abstract fun validToAddImmediately(
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
    ): Boolean

    abstract fun getIntentToAddImmediately(
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
    ): Intent

    abstract fun addOperationImmediately(intent: Intent, onSuccess: Consumer<Operation>): Disposable

    protected open fun getEmptyIntentToAddImmediately(): Intent {
        return Intent(context, AddOperationImmediatelyService::class.java)
            .setAction("ACTION_ADD_OPERATION_IMMEDIATELY")
    }

    open fun createDestroyer(operation: OperationView): EntityDestroyer<Operation> {
        return OperationForceDestroyer(context, data)
    }

    companion object {
        const val INPUT_OPERATION_TYPE = "operationType"
    }
}
