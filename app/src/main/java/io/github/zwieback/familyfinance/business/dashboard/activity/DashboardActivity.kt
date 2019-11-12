package io.github.zwieback.familyfinance.business.dashboard.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.account.activity.AccountActivity
import io.github.zwieback.familyfinance.business.chart.activity.ChartActivity
import io.github.zwieback.familyfinance.business.dashboard.activity.drawer.DrawerCreator
import io.github.zwieback.familyfinance.business.exchange_rate.filter.ExchangeRateFilter
import io.github.zwieback.familyfinance.business.exchange_rate.filter.ExchangeRateFilter.Companion.EXCHANGE_RATE_FILTER
import io.github.zwieback.familyfinance.business.operation.activity.ExpenseOperationActivity
import io.github.zwieback.familyfinance.business.operation.activity.FlowOfFundsOperationActivity
import io.github.zwieback.familyfinance.business.operation.activity.IncomeOperationActivity
import io.github.zwieback.familyfinance.business.operation.activity.TransferOperationActivity
import io.github.zwieback.familyfinance.business.operation.activity.helper.ExpenseOperationHelper
import io.github.zwieback.familyfinance.business.operation.activity.helper.IncomeOperationHelper
import io.github.zwieback.familyfinance.business.operation.activity.helper.TransferOperationHelper
import io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter
import io.github.zwieback.familyfinance.business.operation.filter.ExpenseOperationFilter.EXPENSE_OPERATION_FILTER
import io.github.zwieback.familyfinance.business.operation.filter.FlowOfFundsOperationFilter
import io.github.zwieback.familyfinance.business.operation.filter.FlowOfFundsOperationFilter.FLOW_OF_FUNDS_OPERATION_FILTER
import io.github.zwieback.familyfinance.business.operation.filter.IncomeOperationFilter
import io.github.zwieback.familyfinance.business.operation.filter.IncomeOperationFilter.INCOME_OPERATION_FILTER
import io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter
import io.github.zwieback.familyfinance.business.operation.filter.TransferOperationFilter.TRANSFER_OPERATION_FILTER
import io.github.zwieback.familyfinance.business.sms_pattern.activity.SmsPatternActivity
import io.github.zwieback.familyfinance.business.template.activity.TemplateActivity
import io.github.zwieback.familyfinance.core.activity.DataActivityWrapper
import io.github.zwieback.familyfinance.core.activity.EntityActivity.Companion.INPUT_READ_ONLY
import io.github.zwieback.familyfinance.core.activity.EntityActivity.Companion.INPUT_REGULAR_SELECTABLE
import io.github.zwieback.familyfinance.core.filter.EntityFilter
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class DashboardActivity : DataActivityWrapper() {

    var exchangeRateFilter: ExchangeRateFilter? = null
        private set

    private lateinit var incomeOperationHelper: IncomeOperationHelper
    private lateinit var expenseOperationHelper: ExpenseOperationHelper
    private lateinit var transferOperationHelper: TransferOperationHelper

    private var expenseOperationFilter: ExpenseOperationFilter? = null
    private var incomeOperationFilter: IncomeOperationFilter? = null
    private var transferOperationFilter: TransferOperationFilter? = null
    private var flowOfFundsOperationFilter: FlowOfFundsOperationFilter? = null

    override val titleStringId: Int
        get() = R.string.app_name

    override val isDisplayHomeAsUpEnabled: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findToolbar()?.let { toolbar ->
            DrawerCreator(this).createDrawer(toolbar)
        }
        init(savedInstanceState)
        bindOnClickListeners()
        registerSmsReceiverWithPermissionCheck()
    }

    override fun setupContentView() {
        setContentView(R.layout.activity_dashboard)
    }

    private fun init(savedInstanceState: Bundle?) {
        exchangeRateFilter = loadFilter(savedInstanceState, EXCHANGE_RATE_FILTER)
        expenseOperationFilter = loadFilter(savedInstanceState, EXPENSE_OPERATION_FILTER)
        incomeOperationFilter = loadFilter(savedInstanceState, INCOME_OPERATION_FILTER)
        transferOperationFilter = loadFilter(savedInstanceState, TRANSFER_OPERATION_FILTER)
        flowOfFundsOperationFilter = loadFilter(savedInstanceState, FLOW_OF_FUNDS_OPERATION_FILTER)
        incomeOperationHelper = IncomeOperationHelper(this, data)
        expenseOperationHelper = ExpenseOperationHelper(this, data)
        transferOperationHelper = TransferOperationHelper(this, data)
    }

    private fun <F : EntityFilter> loadFilter(
        savedInstanceState: Bundle?,
        filterName: String
    ): F? {
        return savedInstanceState?.getParcelable(filterName)
    }

    private fun bindOnClickListeners() {
        findViewById<View>(R.id.select_account).setOnClickListener { onSelectAccountClick() }
        findViewById<View>(R.id.select_expenses).setOnClickListener { onSelectExpensesClick() }
        findViewById<View>(R.id.add_expense).setOnClickListener { onAddExpenseClick() }
        findViewById<View>(R.id.select_income).setOnClickListener { onSelectIncomesClick() }
        findViewById<View>(R.id.add_income).setOnClickListener { onAddIncomeClick() }
        findViewById<View>(R.id.select_transfers).setOnClickListener { onSelectTransfersClick() }
        findViewById<View>(R.id.add_transfer).setOnClickListener { onAddTransferClick() }
        findViewById<View>(R.id.select_flow_of_funds).setOnClickListener { onSelectFlowOfFundsClick() }
        findViewById<View>(R.id.select_templates).setOnClickListener { onSelectTemplatesClick() }
        findViewById<View>(R.id.select_sms_patterns).setOnClickListener { onSelectSmsPatternsClick() }
        findViewById<View>(R.id.select_charts).setOnClickListener { onSelectChartsClick() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXCHANGE_RATE_FILTER, exchangeRateFilter)
        outState.putParcelable(EXPENSE_OPERATION_FILTER, expenseOperationFilter)
        outState.putParcelable(INCOME_OPERATION_FILTER, incomeOperationFilter)
        outState.putParcelable(TRANSFER_OPERATION_FILTER, transferOperationFilter)
        outState.putParcelable(FLOW_OF_FUNDS_OPERATION_FILTER, flowOfFundsOperationFilter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultIntent)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            EXCHANGE_RATE_CODE -> resultIntent?.let {
                if (resultIntent.hasExtra(EXCHANGE_RATE_FILTER)) {
                    exchangeRateFilter = resultIntent.getParcelableExtra(EXCHANGE_RATE_FILTER)
                }
            }
            PERSON_CODE -> {
            }
            ACCOUNT_CODE -> {
            }
            INCOME_ARTICLE_CODE -> {
            }
            EXPENSE_ARTICLE_CODE -> {
            }
            INCOME_OPERATION_CODE -> resultIntent?.let {
                if (resultIntent.hasExtra(INCOME_OPERATION_FILTER)) {
                    incomeOperationFilter = resultIntent.getParcelableExtra(INCOME_OPERATION_FILTER)
                }
            }
            EXPENSE_OPERATION_CODE -> resultIntent?.let {
                if (resultIntent.hasExtra(EXPENSE_OPERATION_FILTER)) {
                    expenseOperationFilter =
                        resultIntent.getParcelableExtra(EXPENSE_OPERATION_FILTER)
                }
            }
            TRANSFER_OPERATION_CODE -> resultIntent?.let {
                if (resultIntent.hasExtra(TRANSFER_OPERATION_FILTER)) {
                    transferOperationFilter =
                        resultIntent.getParcelableExtra(TRANSFER_OPERATION_FILTER)
                }
            }
            FLOW_OF_FUNDS_OPERATION_CODE -> resultIntent?.let {
                if (resultIntent.hasExtra(FLOW_OF_FUNDS_OPERATION_FILTER)) {
                    flowOfFundsOperationFilter =
                        resultIntent.getParcelableExtra(FLOW_OF_FUNDS_OPERATION_FILTER)
                }
            }
            TEMPLATE_CODE -> {
            }
            SMS_CODE -> {
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        this.onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.RECEIVE_SMS)
    fun registerSmsReceiver() {
        // empty method because SmsReceiver already registered in the manifest
        // this method is required only for the PermissionsDispatcher
    }

    @OnShowRationale(Manifest.permission.RECEIVE_SMS)
    internal fun showRationaleForReceiveSms(request: PermissionRequest) {
        AlertDialog.Builder(this)
            .setMessage(R.string.permission_receive_sms_rationale)
            .setPositiveButton(R.string.button_allow) { _, _ -> request.proceed() }
            .setNegativeButton(R.string.button_deny) { _, _ -> request.cancel() }
            .show()
    }

    private fun onSelectAccountClick() {
        val intent = Intent(this, AccountActivity::class.java)
            .putExtra(INPUT_READ_ONLY, false)
        startActivityForResult(intent, ACCOUNT_CODE)
    }

    private fun onSelectExpensesClick() {
        val intent = Intent(this, ExpenseOperationActivity::class.java)
            .putExtra(EXPENSE_OPERATION_FILTER, expenseOperationFilter)
            .putExtra(INPUT_READ_ONLY, false)
        startActivityForResult(intent, EXPENSE_OPERATION_CODE)
    }

    private fun onSelectIncomesClick() {
        val intent = Intent(this, IncomeOperationActivity::class.java)
            .putExtra(INCOME_OPERATION_FILTER, incomeOperationFilter)
            .putExtra(INPUT_READ_ONLY, false)
        startActivityForResult(intent, INCOME_OPERATION_CODE)
    }

    private fun onSelectTransfersClick() {
        val intent = Intent(this, TransferOperationActivity::class.java)
            .putExtra(TRANSFER_OPERATION_FILTER, transferOperationFilter)
            .putExtra(INPUT_READ_ONLY, false)
        startActivityForResult(intent, TRANSFER_OPERATION_CODE)
    }

    private fun onSelectFlowOfFundsClick() {
        val intent = Intent(this, FlowOfFundsOperationActivity::class.java)
            .putExtra(FLOW_OF_FUNDS_OPERATION_FILTER, flowOfFundsOperationFilter)
            .putExtra(INPUT_READ_ONLY, false)
        startActivityForResult(intent, FLOW_OF_FUNDS_OPERATION_CODE)
    }

    private fun onSelectTemplatesClick() {
        val intent = Intent(this, TemplateActivity::class.java)
            .putExtra(INPUT_READ_ONLY, false)
            .putExtra(INPUT_REGULAR_SELECTABLE, false)
        startActivityForResult(intent, TEMPLATE_CODE)
    }

    private fun onSelectSmsPatternsClick() {
        val intent = Intent(this, SmsPatternActivity::class.java)
            .putExtra(INPUT_READ_ONLY, false)
        startActivityForResult(intent, SMS_CODE)
    }

    private fun onSelectChartsClick() {
        val intent = Intent(this, ChartActivity::class.java)
        startActivity(intent)
    }

    private fun onAddExpenseClick() {
        val intent = expenseOperationHelper.getIntentToAdd(expenseOperationFilter)
        startActivityForResult(intent, EXPENSE_OPERATION_EDIT_CODE)
    }

    private fun onAddIncomeClick() {
        val intent = incomeOperationHelper.getIntentToAdd(incomeOperationFilter)
        startActivityForResult(intent, INCOME_OPERATION_EDIT_CODE)
    }

    private fun onAddTransferClick() {
        val intent = transferOperationHelper.getIntentToAdd(transferOperationFilter)
        startActivityForResult(intent, TRANSFER_OPERATION_EDIT_CODE)
    }

    companion object {
        const val RESULT_CURRENCY_ID = "resultCurrencyId"
        const val RESULT_EXCHANGE_RATE_ID = "resultExchangeRateId"
        const val RESULT_PERSON_ID = "resultPersonId"
        const val RESULT_ACCOUNT_ID = "resultAccountId"
        const val RESULT_ARTICLE_ID = "resultArticleId"
        const val RESULT_OPERATION_ID = "resultOperationId"
        const val RESULT_TEMPLATE_ID = "resultTemplateId"
        const val RESULT_SMS_PATTERN_ID = "resultSmsPatternId"

        const val CURRENCY_CODE = 101
        const val CURRENCY_EDIT_CODE = 102
        const val EXCHANGE_RATE_CODE = 201
        const val EXCHANGE_RATE_EDIT_CODE = 202
        const val PERSON_CODE = 301
        const val PERSON_EDIT_CODE = 302
        const val ACCOUNT_CODE = 401
        const val INCOME_ACCOUNT_CODE = 402
        const val EXPENSE_ACCOUNT_CODE = 403
        const val ACCOUNT_EDIT_CODE = 404
        const val ARTICLE_CODE = 501
        const val INCOME_ARTICLE_CODE = 502
        const val EXPENSE_ARTICLE_CODE = 503
        const val ARTICLE_EDIT_CODE = 505
        const val INCOME_OPERATION_CODE = 601
        const val EXPENSE_OPERATION_CODE = 602
        const val TRANSFER_OPERATION_CODE = 603
        const val FLOW_OF_FUNDS_OPERATION_CODE = 604
        const val INCOME_OPERATION_EDIT_CODE = 605
        const val EXPENSE_OPERATION_EDIT_CODE = 606
        const val TRANSFER_OPERATION_EDIT_CODE = 607
        const val ICONICS_CODE = 701
        const val BACKUP_PATH_CODE = 801
        const val TEMPLATE_CODE = 901
        const val TEMPLATE_EDIT_CODE = 902
        const val SMS_CODE = 1001
        const val SMS_EDIT_CODE = 1002
    }
}
