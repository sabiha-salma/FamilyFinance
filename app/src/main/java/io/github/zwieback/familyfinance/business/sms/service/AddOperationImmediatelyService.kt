package io.github.zwieback.familyfinance.business.sms.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import io.github.zwieback.familyfinance.app.FamilyFinanceApplication
import io.github.zwieback.familyfinance.business.operation.activity.helper.ExpenseOperationHelper
import io.github.zwieback.familyfinance.business.operation.activity.helper.IncomeOperationHelper
import io.github.zwieback.familyfinance.business.operation.activity.helper.OperationHelper
import io.github.zwieback.familyfinance.business.operation.activity.helper.TransferOperationHelper
import io.github.zwieback.familyfinance.business.sms.common.SmsConst.SMS_NOTIFICATION_ID
import io.github.zwieback.familyfinance.core.model.type.OperationType
import io.reactivex.disposables.CompositeDisposable

class AddOperationImmediatelyService : Service() {

    private lateinit var incomeOperationHelper: IncomeOperationHelper
    private lateinit var expenseOperationHelper: ExpenseOperationHelper
    private lateinit var transferOperationHelper: TransferOperationHelper
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate() {
        super.onCreate()
        val application = application as FamilyFinanceApplication
        val data = application.data
        incomeOperationHelper = IncomeOperationHelper(this, data)
        expenseOperationHelper = ExpenseOperationHelper(this, data)
        transferOperationHelper = TransferOperationHelper(this, data)
        compositeDisposable = CompositeDisposable()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.extras?.let { extras ->
            val operationType =
                extras.getSerializable(OperationHelper.INPUT_OPERATION_TYPE) as OperationType?
                    ?: throw IllegalArgumentException("INPUT_OPERATION_TYPE extra must be not null")
            val operationHelper = determineOperationHelper(operationType)
            compositeDisposable.add(
                operationHelper.addOperationImmediately(intent) {
                    closeNotification(this)
                }
            )
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun closeNotification(context: Context) {
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)
            ?.cancel(SMS_NOTIFICATION_ID)
    }

    private fun determineOperationHelper(operationType: OperationType): OperationHelper<*> {
        return when (operationType) {
            OperationType.EXPENSE_OPERATION -> expenseOperationHelper
            OperationType.INCOME_OPERATION -> incomeOperationHelper
            OperationType.TRANSFER_EXPENSE_OPERATION,
            OperationType.TRANSFER_INCOME_OPERATION -> transferOperationHelper
        }
    }
}
