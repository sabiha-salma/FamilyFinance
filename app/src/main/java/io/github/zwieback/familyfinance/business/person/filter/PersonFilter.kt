package io.github.zwieback.familyfinance.business.person.filter

import io.github.zwieback.familyfinance.constant.IdConstants.EMPTY_ID
import io.github.zwieback.familyfinance.core.filter.EntityFolderFilter
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonFilter(
    override var parentId: Int = EMPTY_ID
) : EntityFolderFilter() {

    companion object {
        const val PERSON_FILTER = "personFilter"
    }
}
