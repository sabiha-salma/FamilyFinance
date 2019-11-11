package io.github.zwieback.familyfinance.business.account.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityForceDestroyer
import io.github.zwieback.familyfinance.core.model.Account
import io.requery.Persistable
import io.requery.meta.QueryAttribute
import io.requery.reactivex.ReactiveEntityStore

internal class AccountForceDestroyer(context: Context, data: ReactiveEntityStore<Persistable>) :
    EntityForceDestroyer<Account>(context, data) {

    override val entityClass: Class<Account>
        get() = Account::class.java

    override val idAttribute: QueryAttribute<Account, Int>
        get() = Account.ID
}
