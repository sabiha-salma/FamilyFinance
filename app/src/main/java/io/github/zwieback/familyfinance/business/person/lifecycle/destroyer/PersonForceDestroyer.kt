package io.github.zwieback.familyfinance.business.person.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityForceDestroyer
import io.github.zwieback.familyfinance.core.model.Person
import io.requery.Persistable
import io.requery.meta.QueryAttribute
import io.requery.reactivex.ReactiveEntityStore

internal class PersonForceDestroyer(context: Context, data: ReactiveEntityStore<Persistable>) :
    EntityForceDestroyer<Person>(context, data) {

    override val entityClass: Class<Person>
        get() = Person::class.java

    override val idAttribute: QueryAttribute<Person, Int>
        get() = Person.ID
}
