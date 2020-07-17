package io.github.zwieback.familyfinance.core.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentTransaction
import com.mikepenz.iconics.utils.IconicsMenuInflaterUtil
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.activity.exception.ReadOnlyException
import io.github.zwieback.familyfinance.core.filter.EntityFilter
import io.github.zwieback.familyfinance.core.fragment.EntityFragment
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.listener.EntityClickListener
import io.github.zwieback.familyfinance.core.listener.EntityFilterListener
import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.github.zwieback.familyfinance.extension.EMPTY_ID
import io.github.zwieback.familyfinance.extension.toNullableId
import io.reactivex.functions.Consumer

abstract class EntityActivity<ENTITY, REGULAR_ENTITY, FILTER, FRAGMENT> :
    DataActivityWrapper(),
    EntityClickListener<ENTITY>,
    EntityFilterListener<FILTER>
        where ENTITY : IBaseEntity,
              REGULAR_ENTITY : IBaseEntity,
              FILTER : EntityFilter,
              FRAGMENT : EntityFragment<ENTITY, FILTER, *, *, *> {

    protected lateinit var filter: FILTER
    protected var readOnly: Boolean = false
    protected var regularSelectable: Boolean = false
    private var filterWasChanged: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
        replaceFragment(!isFirstFrame)
    }

    override fun setupContentView() {
        setContentView(contentLayoutId)
    }

    // -----------------------------------------------------------------------------------------
    // Menu methods
    // -----------------------------------------------------------------------------------------

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        val menuIds = mutableListOf<Int>()
        menuIds.addAll(collectMenuIds())
        if (addFilterMenuItem()) {
            menuIds.add(R.menu.menu_entity_filter)
        }
        menuIds.forEach { menuId ->
            IconicsMenuInflaterUtil.inflate(inflater, this, menuId, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    protected open fun addFilterMenuItem(): Boolean {
        return false
    }

    protected open fun collectMenuIds(): List<Int> {
        return if (readOnly)
            listOf(R.menu.menu_entity_read_only)
        else
            listOf(R.menu.menu_entity)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                showFilterDialog()
                true
            }
            R.id.action_add_entry -> {
                addEntity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // -----------------------------------------------------------------------------------------
    // Init methods
    // -----------------------------------------------------------------------------------------

    protected open val contentLayoutId: Int
        @LayoutRes
        get() = R.layout.activity_entity

    @CallSuper
    protected open fun init(savedInstanceState: Bundle?) {
        readOnly = extractBoolean(intent.extras, INPUT_READ_ONLY, true)
        regularSelectable = extractBoolean(intent.extras, INPUT_REGULAR_SELECTABLE, true)
        val localFilter = extractFilter<FILTER>(intent.extras, filterName)
        filter = loadFilter(savedInstanceState, localFilter)
    }

    // -----------------------------------------------------------------------------------------
    // Filter methods
    // -----------------------------------------------------------------------------------------

    protected abstract val filterName: String

    private fun loadFilter(savedInstanceState: Bundle?, filter: FILTER?): FILTER {
        if (savedInstanceState == null) {
            return filter ?: createDefaultFilter()
        }
        val savedFilter = savedInstanceState.getParcelable<FILTER>(filterName)
        return savedFilter ?: error("Filter wasn't saved early")
    }

    protected abstract fun createDefaultFilter(): FILTER

    override fun onStart() {
        super.onStart()
        applyFilter(filter)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(filterName, filter)
    }

    override fun onApplyFilter(filter: FILTER) {
        if (this.filter != filter) {
            filterWasChanged = true
        }
        this.filter = filter
        applyFilter(filter)
    }

    protected fun applyFilter(filter: FILTER) {
        val fragment = findFragment()
        fragment?.applyFilter(filter)
    }

    override fun onBackPressed() {
        if (needToReturnFilter()) {
            closeActivity(null)
        }
        super.onBackPressed()
    }

    private fun needToReturnFilter(): Boolean {
        return filterWasChanged && supportFragmentManager.backStackEntryCount == 0
    }

    protected open fun showFilterDialog() {
        // stub
    }

    // -----------------------------------------------------------------------------------------
    // Fragment methods
    // -----------------------------------------------------------------------------------------

    protected open val isFirstFrame: Boolean
        get() = true

    @Suppress("UNCHECKED_CAST")
    protected fun replaceFragment(addToBackStack: Boolean) {
        val tag = fragmentTag
        var fragment = supportFragmentManager.findFragmentByTag(tag) as FRAGMENT?
        if (fragment == null) {
            fragment = createFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(fragmentContainerId, fragment, tag)
            if (addToBackStack) {
                transaction.addToBackStack(null)
            }
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.commit()
        }
    }

    protected abstract val fragmentTag: String

    protected abstract fun createFragment(): FRAGMENT

    protected open val fragmentContainerId: Int
        @IdRes
        get() = R.id.entity_fragment

    private fun refresh() {
        val fragment = findFragment()
        fragment?.refresh()
    }

    @Suppress("UNCHECKED_CAST")
    protected fun findFragment(): FRAGMENT? {
        return supportFragmentManager.findFragmentById(fragmentContainerId) as FRAGMENT?
    }

    // -----------------------------------------------------------------------------------------
    // Menu and click methods
    // -----------------------------------------------------------------------------------------

    override fun onEntityClick(view: View, entity: ENTITY) {
        if (regularSelectable) {
            closeActivity(entity)
        }
    }

    override fun onEntityLongClick(view: View, entity: ENTITY) {
        showPopup(view, entity)
    }

    protected fun showPopup(view: View, entity: ENTITY) {
        val popup = PopupMenu(this, view)
        IconicsMenuInflaterUtil.inflate(
            popup.menuInflater,
            this,
            getPopupMenuId(entity),
            popup.menu
        )
        popup.setOnMenuItemClickListener(getPopupItemClickListener(entity))
        popup.show()
    }

    @MenuRes
    protected open fun getPopupMenuId(entity: ENTITY): Int {
        return if (readOnly) {
            R.menu.popup_entity_read_only
        } else {
            R.menu.popup_entity
        }
    }

    protected open fun getPopupItemClickListener(entity: ENTITY): PopupMenu.OnMenuItemClickListener {
        return PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit -> {
                    editEntity(entity)
                    true
                }
                R.id.action_delete -> {
                    deleteEntity(entity)
                    true
                }
                else -> false
            }
        }
    }

    @CallSuper
    protected open fun addEntity() {
        checkReadOnly()
    }

    @CallSuper
    protected open fun editEntity(entity: ENTITY) {
        checkReadOnly()
    }

    @CallSuper
    protected open fun duplicateEntity(entity: ENTITY) {
        checkReadOnly()
    }

    protected fun deleteEntity(entity: ENTITY) {
        checkReadOnly()
        val regularEntity = findRegularEntity(entity)
        val destroyer = createDestroyer(entity)
        destroyer.destroy(regularEntity, Consumer { refresh() })
    }

    protected fun checkReadOnly() {
        if (readOnly) {
            throw ReadOnlyException()
        }
    }

    private fun findRegularEntity(entity: ENTITY): REGULAR_ENTITY {
        return data.findByKey(classOfRegularEntity, entity.id).blockingGet()
    }

    protected abstract val classOfRegularEntity: Class<REGULAR_ENTITY>

    protected abstract fun createDestroyer(entity: ENTITY): EntityDestroyer<REGULAR_ENTITY>

    // -----------------------------------------------------------------------------------------
    // Close methods
    // -----------------------------------------------------------------------------------------

    protected fun closeActivity(entity: ENTITY?) {
        val resultIntent = Intent()
        entity?.let { resultIntent.putExtra(resultName, entity.id) }
        addExtrasIntoResultIntent(resultIntent)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun addExtrasIntoResultIntent(resultIntent: Intent) {
        if (needToReturnFilter()) {
            resultIntent.putExtra(filterName, filter)
        }
    }

    protected abstract val resultName: String

    companion object {
        /**
         * Flag indicating that the entity is opened for read-only purposes
         * without editing
         */
        const val INPUT_READ_ONLY = "inputReadOnly"

        /**
         * Flag indicating that the entity can be selected
         */
        const val INPUT_REGULAR_SELECTABLE = "inputRegularSelectable"

        // -----------------------------------------------------------------------------------------
        // Helper methods
        // -----------------------------------------------------------------------------------------

        fun extractId(bundle: Bundle?, key: String): Int? {
            return bundle?.let {
                val id = bundle.getInt(key, EMPTY_ID)
                id.toNullableId()
            }
        }

        fun extractBoolean(bundle: Bundle?, key: String, defaultValue: Boolean): Boolean {
            return bundle?.getBoolean(key, defaultValue) ?: defaultValue
        }

        private fun <FILTER : EntityFilter> extractFilter(bundle: Bundle?, key: String): FILTER? {
            return bundle?.getParcelable(key)
        }
    }
}
