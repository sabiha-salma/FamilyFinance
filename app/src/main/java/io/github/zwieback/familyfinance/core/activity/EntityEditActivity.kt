package io.github.zwieback.familyfinance.core.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.textfield.TextInputLayout
import com.johnpetitto.validator.ValidatingTextInputLayout
import com.johnpetitto.validator.Validators
import com.mikepenz.iconics.view.IconicsImageView
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.ICONICS_CODE
import io.github.zwieback.familyfinance.business.iconics.activity.IconicsActivity
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.github.zwieback.familyfinance.util.DateUtils
import io.github.zwieback.familyfinance.util.NumberUtils
import io.github.zwieback.familyfinance.util.NumberUtils.ID_AS_NULL
import io.github.zwieback.familyfinance.util.NumberUtils.isNullId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import java.math.BigDecimal

abstract class EntityEditActivity<ENTITY, BINDING> : DataActivityWrapper()
        where ENTITY : IBaseEntity,
              BINDING : ViewDataBinding {

    protected lateinit var entity: ENTITY
    protected lateinit var binding: BINDING
    protected lateinit var provider: EntityProvider<ENTITY>

    @get:LayoutRes
    protected abstract val bindingLayoutId: Int

    protected abstract val extraInputId: String

    protected abstract val extraOutputId: String

    protected abstract val entityClass: Class<ENTITY>

    protected abstract val iconView: IconicsImageView

    protected abstract val layoutsForValidation: List<ValidatingTextInputLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provider = createProvider()

        val id = extractInputId(extraInputId)
        if (isNullId(id)) {
            createEntity()
        } else {
            loadEntity(id)
        }
    }

    override fun setupContentView() {
        binding = DataBindingUtil.setContentView(this, bindingLayoutId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit_entity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                saveEntity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultIntent)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            ICONICS_CODE -> {
                val iconName = resultIntent?.getStringExtra(IconicsActivity.OUTPUT_ICON_NAME)
                setupIcon(iconName)
            }
        }
    }

    fun onSelectIconClick(view: View) {
        val intent = Intent(this, IconicsActivity::class.java)
        startActivityForResult(intent, ICONICS_CODE)
    }

    protected abstract fun createProvider(): EntityProvider<ENTITY>

    protected abstract fun createEntity()

    protected open fun loadEntity(entityId: Int) {
        loadEntity(entityClass, entityId, Consumer { this.bind(it) })
    }

    protected fun <T : IBaseEntity> loadEntity(
        entityClass: Class<T>,
        entityId: Int,
        onSuccess: Consumer<T>
    ) {
        super.loadEntity(entityClass, entityId, onSuccess, Functions.ON_ERROR_MISSING)
    }

    /**
     * Must be called at the end of the method.
     *
     * @param entity new instance entity
     */
    @CallSuper
    protected open fun bind(entity: ENTITY) {
        setupBindings()
    }

    protected open fun setupBindings() {
        // do nothing
    }

    /**
     * Change icon of entity.
     *
     * @param iconName a new name of icon
     */
    private fun setupIcon(iconName: String?) {
        val oldIconName = entity.iconName
        try {
            setIconNameByReflection(entity, iconName)
            provider.setupIcon(iconView.icon, entity)
        } catch (e: IllegalArgumentException) {
            setIconNameByReflection(entity, oldIconName)
            val errorMessage = resources.getString(R.string.can_not_find_icon, iconName)
            Log.w(TAG, "setupIcon: $errorMessage", e)
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        } catch (e: NullPointerException) {
            setIconNameByReflection(entity, oldIconName)
            val errorMessage = resources.getString(R.string.can_not_find_icon, iconName)
            Log.w(TAG, "setupIcon: $errorMessage", e)
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setIconNameByReflection(entity: ENTITY, iconName: String?) {
        try {
            val method = entity.javaClass.getDeclaredMethod("setIconName", String::class.java)
            // next method.invoke is analog of the following method:
            // entity.setIconName(iconName);
            method.invoke(entity, iconName)
        } catch (e: Exception) {
            Log.e(TAG, "setIconNameByReflection", e)
        }
    }

    // -----------------------------------------------------------------------------------------
    // Saving
    // -----------------------------------------------------------------------------------------

    private fun saveEntity() {
        if (anyErrorFound()) {
            return
        }
        updateEntityProperties(entity)
        saveEntity(entity, onSuccessfulSaving())
    }

    private fun anyErrorFound(): Boolean {
        return !Validators.validate(layoutsForValidation)
    }

    protected abstract fun updateEntityProperties(entity: ENTITY)

    protected fun saveEntity(entity: ENTITY, onSuccessfulSaving: Consumer<ENTITY>) {
        val single = if (entity.id == 0) data.insert(entity) else data.update(entity)
        addDisposable(
            single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccessfulSaving)
        )
    }

    protected open fun onSuccessfulSaving(): Consumer<ENTITY> {
        return Consumer { this.closeActivity(it) }
    }

    // -----------------------------------------------------------------------------------------
    // Helper methods
    // -----------------------------------------------------------------------------------------

    protected fun closeActivity(entity: ENTITY) {
        val resultIntent = Intent()
        resultIntent.putExtra(extraOutputId, entity.id)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    @JvmOverloads
    protected fun extractInputId(name: String, defaultValue: Int = ID_AS_NULL): Int {
        return intent.getIntExtra(name, defaultValue)
    }

    protected fun extractInputDate(name: String): LocalDate {
        return DateUtils.readLocalDateFromIntent(intent, name)
    }

    protected fun extractInputBigDecimal(name: String): BigDecimal? {
        return NumberUtils.readBigDecimalFromIntent(intent, name)
    }

    protected fun extractInputString(name: String): String? {
        return intent.getStringExtra(name)
    }

    // TODO: move to Companion
    protected fun extractOutputId(resultIntent: Intent, name: String): Int {
        return resultIntent.getIntExtra(name, ID_AS_NULL)
    }

    protected fun disableLayout(layout: TextInputLayout, @StringRes hintId: Int) {
        layout.isEnabled = false
        layout.hint = resources.getString(hintId)
    }

    protected fun disableLayout(layout: LinearLayout) {
        layout.isEnabled = false
    }

    companion object {
        private const val TAG = "EntityEditActivity"
    }
}
