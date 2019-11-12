package io.github.zwieback.familyfinance.business.person.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityFromPreferencesDestroyer
import io.github.zwieback.familyfinance.core.model.Person
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

internal class PersonFromPreferencesDestroyer(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityFromPreferencesDestroyer<Person>(context, data) {

    override val alertResourceId: Int
        get() = R.string.preferences_contains_person

    override fun next(): EntityDestroyer<Person>? {
        return PersonForceDestroyer(context, data)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun preferencesContainsEntity(person: Person): Boolean {
        val defaultPersonId = databasePrefs.personId
        return defaultPersonId == person.id
    }
}
