package io.github.zwieback.familyfinance.core.filter

import io.github.zwieback.familyfinance.extension.toEmptyId
import io.github.zwieback.familyfinance.extension.toNullableId

abstract class EntityFolderFilter : EntityFilter() {

    abstract var parentId: Int

    fun takeParentId(): Int? {
        return parentId.toNullableId()
    }

    fun putParentId(parentId: Int?) {
        this.parentId = parentId.toEmptyId()
    }
}
