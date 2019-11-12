package io.github.zwieback.familyfinance.business.account.activity

import android.content.Intent
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.account.filter.AccountFilter
import io.github.zwieback.familyfinance.business.account.filter.AccountFilter.Companion.ACCOUNT_FILTER
import io.github.zwieback.familyfinance.business.account.fragment.AccountFragment
import io.github.zwieback.familyfinance.business.account.lifecycle.destroyer.AccountAsParentDestroyer
import io.github.zwieback.familyfinance.business.account.lifecycle.destroyer.AccountFromExpenseOperationsDestroyer
import io.github.zwieback.familyfinance.business.account.listener.OnAccountClickListener
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_ACCOUNT_ID
import io.github.zwieback.familyfinance.core.activity.EntityFolderActivity
import io.github.zwieback.familyfinance.core.activity.EntityFolderEditActivity.Companion.INPUT_IS_FOLDER
import io.github.zwieback.familyfinance.core.activity.EntityFolderEditActivity.Companion.INPUT_PARENT_ID
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.model.Account
import io.github.zwieback.familyfinance.core.model.AccountView

class AccountActivity :
    EntityFolderActivity<AccountView, Account, AccountFilter, AccountFragment>(),
    OnAccountClickListener {

    override val titleStringId: Int
        get() = R.string.account_activity_title

    override val filterName: String
        get() = ACCOUNT_FILTER

    override val resultName: String
        get() = RESULT_ACCOUNT_ID

    override val fragmentTag: String
        get() = String.format("%s_%s", localClassName, filter.getParentId())

    override val classOfRegularEntity: Class<Account>
        get() = Account::class.java

    override fun createDefaultFilter(): AccountFilter {
        val filter = AccountFilter()
        filter.isOnlyActive = extractBoolean(intent.extras, INPUT_ONLY_ACTIVE, false)
        return filter
    }

    override fun createFragment(): AccountFragment {
        return AccountFragment.newInstance(filter)
    }

    override fun addEntity(parentId: Int, isFolder: Boolean) {
        super.addEntity(parentId, isFolder)
        val intent = Intent(this, AccountEditActivity::class.java)
            .putExtra(INPUT_PARENT_ID, parentId)
            .putExtra(INPUT_IS_FOLDER, isFolder)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun editEntity(account: AccountView) {
        super.editEntity(account)
        val intent = Intent(this, AccountEditActivity::class.java)
            .putExtra(AccountEditActivity.INPUT_ACCOUNT_ID, account.id)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun createDestroyer(account: AccountView): EntityDestroyer<Account> {
        return if (account.isFolder) {
            AccountAsParentDestroyer(this, data)
        } else {
            AccountFromExpenseOperationsDestroyer(this, data)
        }
    }

    companion object {
        const val INPUT_ONLY_ACTIVE = "inputOnlyActive"
    }
}
