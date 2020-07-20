package io.github.zwieback.familyfinance.core.filter

import android.os.Parcel
import io.github.zwieback.familyfinance.constant.IdConstants.EMPTY_ID
import io.github.zwieback.familyfinance.extension.toEmptyId
import io.github.zwieback.familyfinance.extension.toNullableId

abstract class EntityFolderFilter : EntityFilter {

    private var parentId: Int = 0

    protected constructor() : super()

    protected constructor(filter: EntityFolderFilter) : super(filter) {
        setParentId(filter.getParentId())
    }

    protected constructor(`in`: Parcel) : super(`in`)

    override fun init() {
        super.init()
        parentId = EMPTY_ID
    }

    override fun readFromParcel(`in`: Parcel) {
        parentId = `in`.readInt()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeInt(parentId)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EntityFolderFilter

        if (parentId != other.parentId) return false

        return true
    }

    override fun hashCode(): Int {
        return parentId
    }

    fun getParentId(): Int? {
        return parentId.toNullableId()
    }

    fun setParentId(parentId: Int?) {
        this.parentId = parentId.toEmptyId()
    }
}
