package io.github.zwieback.familyfinance.business.account.filter

import android.os.Parcel
import android.os.Parcelable
import io.github.zwieback.familyfinance.core.filter.EntityFolderFilter
import io.github.zwieback.familyfinance.extension.EMPTY_ID
import io.github.zwieback.familyfinance.extension.toEmptyId
import io.github.zwieback.familyfinance.extension.toNullableId
import io.github.zwieback.familyfinance.util.BooleanUtils.readBooleanFromParcel
import io.github.zwieback.familyfinance.util.BooleanUtils.writeBooleanToParcel

class AccountFilter : EntityFolderFilter {

    private var ownerId: Int = 0
    var isOnlyActive: Boolean = false

    constructor() : super()

    constructor(filter: AccountFilter) : super(filter) {
        setOwnerId(filter.getOwnerId())
        isOnlyActive = filter.isOnlyActive
    }

    private constructor(`in`: Parcel) : super(`in`)

    override fun init() {
        super.init()
        ownerId = EMPTY_ID
        isOnlyActive = false
    }

    override fun readFromParcel(`in`: Parcel) {
        super.readFromParcel(`in`)
        ownerId = `in`.readInt()
        isOnlyActive = readBooleanFromParcel(`in`)
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(ownerId)
        writeBooleanToParcel(out, isOnlyActive)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as AccountFilter

        if (ownerId != other.ownerId) return false
        if (isOnlyActive != other.isOnlyActive) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + ownerId
        result = 31 * result + isOnlyActive.hashCode()
        return result
    }

    fun getOwnerId(): Int? {
        return ownerId.toNullableId()
    }

    fun setOwnerId(ownerId: Int?) {
        this.ownerId = ownerId.toEmptyId()
    }

    companion object {
        const val ACCOUNT_FILTER = "accountFilter"

        @JvmField
        var CREATOR: Parcelable.Creator<AccountFilter> =
            object : Parcelable.Creator<AccountFilter> {

                override fun createFromParcel(parcel: Parcel): AccountFilter {
                    return AccountFilter(parcel)
                }

                override fun newArray(size: Int): Array<AccountFilter?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
