package io.github.zwieback.familyfinance.business.person.activity

import android.app.Activity
import android.content.Intent
import com.johnpetitto.validator.ValidatingTextInputLayout
import com.mikepenz.iconics.view.IconicsImageView
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.PERSON_CODE
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_PERSON_ID
import io.github.zwieback.familyfinance.business.person.adapter.PersonProvider
import io.github.zwieback.familyfinance.core.activity.EntityActivity
import io.github.zwieback.familyfinance.core.activity.EntityFolderActivity
import io.github.zwieback.familyfinance.core.activity.EntityFolderEditActivity
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.Person
import io.github.zwieback.familyfinance.databinding.ActivityEditPersonBinding
import io.github.zwieback.familyfinance.util.NumberUtils.nonNullId
import io.github.zwieback.familyfinance.util.NumberUtils.stringToInt
import io.reactivex.functions.Consumer

class PersonEditActivity : EntityFolderEditActivity<Person, ActivityEditPersonBinding>() {

    override val titleStringId: Int
        get() = R.string.person_activity_edit_title

    override val bindingLayoutId: Int
        get() = R.layout.activity_edit_person

    override val extraInputId: String
        get() = INPUT_PERSON_ID

    override val extraOutputId: String
        get() = OUTPUT_PERSON_ID

    override val entityClass: Class<Person>
        get() = Person::class.java

    override val layoutsForValidation: List<ValidatingTextInputLayout>
        get() = listOf(binding.parentLayout, binding.nameLayout, binding.orderCodeLayout)

    override val iconView: IconicsImageView
        get() = binding.icon

    override val parentLayout: ValidatingTextInputLayout
        get() = binding.parentLayout

    override fun createProvider(): EntityProvider<Person> {
        return PersonProvider(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultIntent)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            PERSON_CODE -> resultIntent?.let {
                val parentId = extractOutputId(resultIntent, RESULT_PERSON_ID)
                loadParent(parentId)
            }
        }
    }

    private fun onParentClick() {
        val intent = Intent(this, PersonActivity::class.java)
            .putExtra(EntityActivity.INPUT_REGULAR_SELECTABLE, false)
            .putExtra(EntityFolderActivity.INPUT_PROHIBITED_FOLDER_ID, entity.id)
        startActivityForResult(intent, PERSON_CODE)
    }

    private fun onParentRemoved() {
        entity.setParent(null)
        binding.parentLayout.error = null
    }

    private fun loadParent(parentId: Int) {
        if (nonNullId(parentId)) {
            loadEntity(Person::class.java, parentId, Consumer { foundPerson ->
                entity.setParent(foundPerson)
                binding.parentLayout.error = null
            })
        }
    }

    override fun createEntity() {
        val parentId = extractInputId(INPUT_PARENT_ID)
        val folder = extractInputBoolean(INPUT_IS_FOLDER)
        val person = Person()
        person.setFolder(folder)
        bind(person)
        loadParent(parentId)
        disableLayout(binding.parentLayout, R.string.hint_parent_disabled)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun bind(person: Person) {
        entity = person
        binding.person = person
        provider.setupIcon(binding.icon.icon, person)
        super.bind(person)
    }

    override fun setupBindings() {
        binding.icon.setOnClickListener { onSelectIconClick() }
        binding.parent.setOnClickListener { onParentClick() }
        binding.parent.setOnClearTextListener { onParentRemoved() }
        binding.parentLayout.setValidator { isParentValid(it) }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun updateEntityProperties(person: Person) {
        person.setName(binding.name.text?.toString())
        binding.orderCode.text?.toString()?.let { orderCode ->
            person.setOrderCode(stringToInt(orderCode))
        }
    }

    private fun isParentValid(input: String): Boolean {
        return isParentValid(input, entity.parent as Person?, Person.`$TYPE`.name)
    }

    override fun isParentInsideItself(parentId: Int, newParentId: Int): Boolean {
        return isParentInsideItself(
            newParentId,
            Person.ID,
            Person.PARENT_ID.eq(parentId).and(Person.FOLDER.eq(true))
        )
    }

    companion object {
        const val INPUT_PERSON_ID = "personId"
        const val OUTPUT_PERSON_ID = "resultPersonId"
    }
}
