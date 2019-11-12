package io.github.zwieback.familyfinance.business.person.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityFromDestroyer
import io.github.zwieback.familyfinance.core.model.Account
import io.github.zwieback.familyfinance.core.model.Person
import io.requery.Persistable
import io.requery.query.Condition
import io.requery.reactivex.ReactiveEntityStore

class PersonFromAccountsDestroyer(context: Context, data: ReactiveEntityStore<Persistable>) :
    EntityFromDestroyer<Person, Account>(context, data) {

    override val fromClass: Class<Account>
        get() = Account::class.java

    override val alertResourceId: Int
        get() = R.string.accounts_with_owner_exists

    override fun next(): EntityDestroyer<Person>? {
        return PersonFromExpenseOperationsDestroyer(context, data)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun getWhereCondition(person: Person): Condition<*, *> {
        return Account.OWNER_ID.eq(person.id)
    }
}
