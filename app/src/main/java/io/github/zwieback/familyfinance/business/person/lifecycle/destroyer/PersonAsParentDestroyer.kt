package io.github.zwieback.familyfinance.business.person.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityAsParentDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.model.Person
import io.requery.Persistable
import io.requery.meta.QueryExpression
import io.requery.reactivex.ReactiveEntityStore

class PersonAsParentDestroyer(context: Context, data: ReactiveEntityStore<Persistable>) :
    EntityAsParentDestroyer<Person>(context, data) {

    override val entityClass: Class<Person>
        get() = Person::class.java

    override val parentIdExpression: QueryExpression<Int>
        get() = Person.PARENT_ID

    override val alertResourceId: Int
        get() = R.string.persons_with_parent_exists

    override fun next(): EntityDestroyer<Person>? {
        return PersonFromAccountsDestroyer(context, data)
    }
}
