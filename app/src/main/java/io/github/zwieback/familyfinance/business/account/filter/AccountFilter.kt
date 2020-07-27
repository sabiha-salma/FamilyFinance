package io.github.zwieback.familyfinance.business.account.filter

import io.github.zwieback.familyfinance.constant.IdConstants.EMPTY_ID
import io.github.zwieback.familyfinance.core.filter.EntityFolderFilter
import io.github.zwieback.familyfinance.extension.toEmptyId
import io.github.zwieback.familyfinance.extension.toNullableId
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountFilter(
    override var parentId: Int = EMPTY_ID,
    var ownerId: Int = EMPTY_ID,
    var isOnlyActive: Boolean = false
) : EntityFolderFilter() {

    fun takeOwnerId(): Int? {
        return ownerId.toNullableId()
    }

    fun putOwnerId(ownerId: Int?) {
        this.ownerId = ownerId.toEmptyId()
    }

    companion object {
        const val ACCOUNT_FILTER = "accountFilter"
    }
}
