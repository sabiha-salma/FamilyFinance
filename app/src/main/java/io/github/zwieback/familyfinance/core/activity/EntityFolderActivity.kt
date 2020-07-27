package io.github.zwieback.familyfinance.core.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.widget.PopupMenu
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.constant.IdConstants.EMPTY_ID
import io.github.zwieback.familyfinance.core.filter.EntityFolderFilter
import io.github.zwieback.familyfinance.core.fragment.EntityFolderFragment
import io.github.zwieback.familyfinance.core.listener.EntityFolderClickListener
import io.github.zwieback.familyfinance.core.model.IBaseEntityFolder
import io.github.zwieback.familyfinance.extension.toEmptyId

abstract class EntityFolderActivity<ENTITY, REGULAR_ENTITY, FILTER, FRAGMENT> :
    EntityActivity<ENTITY, REGULAR_ENTITY, FILTER, FRAGMENT>(),
    EntityFolderClickListener<ENTITY>
        where ENTITY : IBaseEntityFolder,
              REGULAR_ENTITY : IBaseEntityFolder,
              FILTER : EntityFolderFilter,
              FRAGMENT : EntityFolderFragment<ENTITY, FILTER, *, *, *> {

    protected var parentFolderId: Int? = null
    private var prohibitedFolderId: Int? = null
    private var folderSelectable: Boolean = false

    // -----------------------------------------------------------------------------------------
    // Menu methods
    // -----------------------------------------------------------------------------------------

    override fun collectMenuIds(): List<Int> {
        val menuIds = mutableListOf<Int>()
        menuIds.addAll(super.collectMenuIds())
        if (readOnly) {
            menuIds.add(R.menu.menu_entity_folder_read_only)
        } else {
            menuIds.add(R.menu.menu_entity_folder)
        }
        return menuIds
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_folder -> {
                addFolder()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // -----------------------------------------------------------------------------------------
    // Init methods
    // -----------------------------------------------------------------------------------------

    override fun init(savedInstanceState: Bundle?) {
        parentFolderId = extractId(intent.extras, INPUT_PARENT_FOLDER_ID)
        prohibitedFolderId = extractId(intent.extras, INPUT_PROHIBITED_FOLDER_ID)
        folderSelectable = extractBoolean(intent.extras, INPUT_FOLDER_SELECTABLE, true)
        super.init(savedInstanceState)
    }

    // -----------------------------------------------------------------------------------------
    // Fragment methods
    // -----------------------------------------------------------------------------------------

    override val isFirstFrame: Boolean
        get() = filter.takeParentId() == null

    // -----------------------------------------------------------------------------------------
    // Menu and click methods
    // -----------------------------------------------------------------------------------------

    override fun onFolderClick(view: View, entity: ENTITY) {
        if (accessAllowed(entity)) {
            filter.putParentId(entity.id)
            replaceFragment(true)
        } else {
            showAccessDeniedToast()
        }
    }

    override fun onFolderLongClick(view: View, entity: ENTITY) {
        if (accessAllowed(entity)) {
            showPopup(view, entity)
        } else {
            showAccessDeniedToast()
        }
    }

    private fun accessAllowed(entity: ENTITY): Boolean {
        return entity.id != prohibitedFolderId
    }

    override fun getPopupMenuId(entity: ENTITY): Int {
        return if (entity.isFolder) {
            if (readOnly) {
                if (folderSelectable) {
                    R.menu.popup_entity_folder_select
                } else {
                    R.menu.popup_entity_folder_read_only
                }
            } else {
                R.menu.popup_entity_folder
            }
        } else {
            super.getPopupMenuId(entity)
        }
    }

    override fun getPopupItemClickListener(entity: ENTITY): PopupMenu.OnMenuItemClickListener {
        return PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_select -> {
                    closeActivity(entity)
                    true
                }
                R.id.action_add_nested_entry -> {
                    addNestedEntity(entity)
                    true
                }
                R.id.action_add_nested_folder -> {
                    addNestedFolder(entity)
                    true
                }
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

    override fun addEntity() {
        super.addEntity()
        addEntity(determineValidParentId(), false)
    }

    protected open fun addFolder() {
        addEntity(determineValidParentId(), true)
    }

    protected open fun addNestedEntity(entity: ENTITY) {
        addEntity(entity.id, false)
    }

    protected open fun addNestedFolder(entity: ENTITY) {
        addEntity(entity.id, true)
    }

    private fun determineValidParentId(): Int {
        return findFragment()?.parentId?.toEmptyId() ?: EMPTY_ID
    }

    @CallSuper
    protected open fun addEntity(parentId: Int, isFolder: Boolean) {
        checkReadOnly()
    }

    // -----------------------------------------------------------------------------------------
    // Helper methods
    // -----------------------------------------------------------------------------------------

    private fun showAccessDeniedToast() {
        Toast.makeText(this, R.string.access_to_this_folder_is_denied, Toast.LENGTH_SHORT).show()
    }

    companion object {
        /**
         * Flag indicating that the folder with the specified id should be opened
         */
        const val INPUT_PARENT_FOLDER_ID = "inputParentFolderId"

        /**
         * Flag indicating that the folder with the specified id cannot be selected
         */
        const val INPUT_PROHIBITED_FOLDER_ID = "inputProhibitedFolderId"

        /**
         * Flag indicating that the folder can be selected
         */
        const val INPUT_FOLDER_SELECTABLE = "inputFolderSelectable"
    }
}
