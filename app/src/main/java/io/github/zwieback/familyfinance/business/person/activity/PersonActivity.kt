package io.github.zwieback.familyfinance.business.person.activity

import android.content.Intent
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_PERSON_ID
import io.github.zwieback.familyfinance.business.person.filter.PersonFilter
import io.github.zwieback.familyfinance.business.person.filter.PersonFilter.Companion.PERSON_FILTER
import io.github.zwieback.familyfinance.business.person.fragment.PersonFragment
import io.github.zwieback.familyfinance.business.person.lifecycle.destroyer.PersonAsParentDestroyer
import io.github.zwieback.familyfinance.business.person.lifecycle.destroyer.PersonFromAccountsDestroyer
import io.github.zwieback.familyfinance.business.person.listener.OnPersonClickListener
import io.github.zwieback.familyfinance.core.activity.EntityFolderActivity
import io.github.zwieback.familyfinance.core.activity.EntityFolderEditActivity.Companion.INPUT_IS_FOLDER
import io.github.zwieback.familyfinance.core.activity.EntityFolderEditActivity.Companion.INPUT_PARENT_ID
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.model.Person
import io.github.zwieback.familyfinance.core.model.PersonView

class PersonActivity :
    EntityFolderActivity<PersonView, Person, PersonFilter, PersonFragment>(),
    OnPersonClickListener {

    override val titleStringId: Int
        get() = R.string.person_activity_title

    override val filterName: String
        get() = PERSON_FILTER

    override val resultName: String
        get() = RESULT_PERSON_ID

    override val fragmentTag: String
        get() = String.format("%s_%s", localClassName, filter.getParentId())

    override val classOfRegularEntity: Class<Person>
        get() = Person::class.java

    override fun createDefaultFilter(): PersonFilter {
        return PersonFilter()
    }

    override fun createFragment(): PersonFragment {
        return PersonFragment.newInstance(filter)
    }

    override fun addEntity(parentId: Int, isFolder: Boolean) {
        super.addEntity(parentId, isFolder)
        val intent = Intent(this, PersonEditActivity::class.java)
            .putExtra(INPUT_PARENT_ID, parentId)
            .putExtra(INPUT_IS_FOLDER, isFolder)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun editEntity(person: PersonView) {
        super.editEntity(person)
        val intent = Intent(this, PersonEditActivity::class.java)
            .putExtra(PersonEditActivity.INPUT_PERSON_ID, person.id)
        startActivity(intent)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun createDestroyer(person: PersonView): EntityDestroyer<Person> {
        return if (person.isFolder) {
            PersonAsParentDestroyer(this, data)
        } else {
            PersonFromAccountsDestroyer(this, data)
        }
    }
}
