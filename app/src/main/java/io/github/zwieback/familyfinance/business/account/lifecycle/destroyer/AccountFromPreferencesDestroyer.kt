package io.github.zwieback.familyfinance.business.account.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityFromPreferencesDestroyer
import io.github.zwieback.familyfinance.core.model.Account
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

internal class AccountFromPreferencesDestroyer(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityFromPreferencesDestroyer<Account>(context, data) {

    override val alertResourceId: Int
        get() = R.string.preferences_contains_account

    override fun next(): EntityDestroyer<Account>? {
        return AccountForceDestroyer(context, data)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun preferencesContainsEntity(account: Account): Boolean {
        val defaultAccountId = runBlocking(Dispatchers.IO) {
            databasePrefs.accountId
        }
        return defaultAccountId == account.id
    }
}
