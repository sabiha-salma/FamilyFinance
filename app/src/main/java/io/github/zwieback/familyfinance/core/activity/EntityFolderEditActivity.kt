package io.github.zwieback.familyfinance.core.activity

import androidx.databinding.ViewDataBinding
import com.johnpetitto.validator.ValidatingTextInputLayout
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.model.IBaseEntityFolder
import io.github.zwieback.familyfinance.util.SqliteUtils
import io.requery.meta.QueryAttribute
import io.requery.query.Condition

abstract class EntityFolderEditActivity<ENTITY, BINDING> :
    EntityEditActivity<ENTITY, BINDING>()
        where ENTITY : IBaseEntityFolder,
              BINDING : ViewDataBinding {

    protected abstract val parentLayout: ValidatingTextInputLayout

    protected fun isParentValid(input: String, parent: ENTITY?, tableName: String): Boolean {
        if (parent == null || input.isEmpty()) {
            return true
        }
        if (!parent.isFolder) {
            parentLayout.setErrorLabel(R.string.parent_must_be_a_folder)
            return false
        }
        if (parent.id == entity.id) {
            parentLayout.setErrorLabel(R.string.parent_can_not_be_a_parent_of_itself)
            return false
        }
        val parentInsideItself = if (SqliteUtils.cteSupported()) {
            isParentInsideItselfUsingCte(entity.id, parent.id, tableName)
        } else {
            isParentInsideItself(entity.id, parent.id)
        }
        return if (parentInsideItself) {
            parentLayout.setErrorLabel(R.string.parent_can_not_be_inside_itself)
            false
        } else {
            true
        }
    }

    protected abstract fun isParentInsideItself(parentId: Int, newParentId: Int): Boolean

    protected fun isParentInsideItself(
        newParentId: Int,
        entityIdAttribute: QueryAttribute<ENTITY, Int>,
        whereCondition: Condition<*, *>
    ): Boolean {
        val childIds = data
            .select(entityClass, entityIdAttribute)
            .where(whereCondition)
            .get()
            .iterator()
            .asSequence()
            .map { entity -> entity.id }
            .toList()

        if (childIds.isEmpty()) {
            return false
        }
        return if (childIds.contains(newParentId)) {
            true
        } else {
            childIds.any { childId -> isParentInsideItself(childId, newParentId) }
        }
    }

    private fun isParentInsideItselfUsingCte(
        parentId: Int,
        newParentId: Int,
        tableName: String
    ): Boolean {
        var query = "with recursive subtree" +
                " as (select id" +
                "       from :tableName" +
                "      where id = :parentId" +
                "      union all" +
                "     select child.id" +
                "       from :tableName as child" +
                "       join subtree on child.parent_id = subtree.id)" +
                " select id" +
                " from subtree" +
                " where id <> :parentId"
        query = query.replace(":parentId".toRegex(), parentId.toString())
        query = query.replace(":tableName".toRegex(), tableName)

        return data
            .raw(query)
            .iterator()
            .asSequence()
            .map { tuple -> tuple.get("id") as Int }
            .any { childId -> childId == newParentId }
    }

    protected fun extractInputBoolean(name: String): Boolean {
        return intent.getBooleanExtra(name, false)
    }

    companion object {
        const val INPUT_PARENT_ID = "parentId"
        const val INPUT_IS_FOLDER = "isFolder"
    }
}
