package io.github.zwieback.familyfinance.core.filter

import android.os.Parcel
import io.github.zwieback.familyfinance.util.NumberUtils.ID_AS_NULL
import io.github.zwieback.familyfinance.util.NumberUtils.intToIntegerId
import io.github.zwieback.familyfinance.util.NumberUtils.integerToIntId

abstract class EntityFolderFilter : EntityFilter {

    private var parentId: Int = 0

    protected constructor() : super()

    protected constructor(filter: EntityFolderFilter) : super(filter) {
        setParentId(filter.getParentId())
    }

    protected constructor(`in`: Parcel) : super(`in`)

    override fun init() {
        super.init()
        parentId = ID_AS_NULL
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
        return intToIntegerId(parentId)
    }

    fun setParentId(parentId: Int?) {
        this.parentId = integerToIntId(parentId)
    }
}
