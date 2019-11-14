package io.github.zwieback.familyfinance.business.preference.custom.database

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.account.activity.AccountActivity
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.ACCOUNT_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_ACCOUNT_ID
import io.github.zwieback.familyfinance.core.activity.EntityFolderActivity
import io.github.zwieback.familyfinance.core.model.Account
import io.github.zwieback.familyfinance.core.preference.custom.EntityActivityResultPreference

class AccountPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = TypedArrayUtils.getAttr(
        context,
        androidx.preference.R.attr.preferenceStyle,
        android.R.attr.preferenceStyle
    ),
    defStyleRes: Int = 0
) : EntityActivityResultPreference<Account>(context, attrs, defStyleAttr, defStyleRes) {

    override val requestCode: Int
        get() = ACCOUNT_CODE

    override val requestIntent: Intent
        get() = Intent(context, AccountActivity::class.java)
            .putExtra(AccountActivity.INPUT_ONLY_ACTIVE, true)
            .putExtra(EntityFolderActivity.INPUT_FOLDER_SELECTABLE, false)

    override val resultName: String
        get() = RESULT_ACCOUNT_ID

    override val savedEntityId: Int
        get() = databasePrefs.accountId

    override val entityClass: Class<Account>
        get() = Account::class.java

    override val preferenceTitleRes: Int
        get() = R.string.account_id_preference_title

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun saveEntityId(accountId: Int) {
        databasePrefs.accountId = accountId
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun getEntityName(account: Account): String {
        return account.name
    }
}
