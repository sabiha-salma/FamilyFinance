package io.github.zwieback.familyfinance.business.account.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityAsParentDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.model.Account
import io.requery.Persistable
import io.requery.meta.QueryExpression
import io.requery.reactivex.ReactiveEntityStore

class AccountAsParentDestroyer(context: Context, data: ReactiveEntityStore<Persistable>) :
    EntityAsParentDestroyer<Account>(context, data) {

    override val entityClass: Class<Account>
        get() = Account::class.java

    override val parentIdExpression: QueryExpression<Int>
        get() = Account.PARENT_ID

    override val alertResourceId: Int
        get() = R.string.accounts_with_parent_exists

    override fun next(): EntityDestroyer<Account>? {
        return AccountFromExpenseOperationsDestroyer(context, data)
    }
}
