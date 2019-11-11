package io.github.zwieback.familyfinance.business.person.lifecycle.creator

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.creator.EntityCreator
import io.github.zwieback.familyfinance.core.model.Person
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class PersonCreator(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityCreator<Person>(context, data) {

    override fun buildEntities(): Iterable<Person> {
        return sortedSetOf(this, createPerson(null, getString(R.string.person_chief), false, 1))
    }

    override fun compare(left: Person, right: Person): Int {
        return left.orderCode.compareTo(right.orderCode)
    }

    private fun createPerson(
        parent: Person?,
        name: String,
        folder: Boolean,
        orderCode: Int
    ): Person {
        return Person()
            .setParent(parent)
            .setName(name)
            .setFolder(folder)
            .setOrderCode(orderCode)
    }
}
