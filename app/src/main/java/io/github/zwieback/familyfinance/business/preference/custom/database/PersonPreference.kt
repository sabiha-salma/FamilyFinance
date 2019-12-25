package io.github.zwieback.familyfinance.business.preference.custom.database

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.PERSON_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_PERSON_ID
import io.github.zwieback.familyfinance.business.person.activity.PersonActivity
import io.github.zwieback.familyfinance.core.activity.EntityFolderActivity
import io.github.zwieback.familyfinance.core.model.Person
import io.github.zwieback.familyfinance.core.preference.custom.EntityActivityResultPreference

class PersonPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = TypedArrayUtils.getAttr(
        context,
        androidx.preference.R.attr.preferenceStyle,
        android.R.attr.preferenceStyle
    ),
    defStyleRes: Int = 0
) : EntityActivityResultPreference<Person>(context, attrs, defStyleAttr, defStyleRes) {

    override val requestCode: Int
        get() = PERSON_CODE

    override val requestIntent: Intent
        get() = Intent(context, PersonActivity::class.java)
            .putExtra(EntityFolderActivity.INPUT_FOLDER_SELECTABLE, false)

    override val resultName: String
        get() = RESULT_PERSON_ID

    override val entityClass: Class<Person>
        get() = Person::class.java

    override val preferenceTitleRes: Int
        get() = R.string.person_id_preference_title

    override suspend fun getSavedEntityId(): Int {
        return databasePrefs.personId
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun saveEntityId(personId: Int) {
        databasePrefs.personId = personId
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun getEntityName(person: Person): String {
        return person.name
    }
}
