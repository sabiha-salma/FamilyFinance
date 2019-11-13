package io.github.zwieback.familyfinance.business.article.filter

import android.os.Parcel
import io.github.zwieback.familyfinance.core.filter.EntityFolderFilter

abstract class ArticleFilter : EntityFolderFilter {

    var searchName: String? = null

    internal constructor() : super()

    internal constructor(filter: ArticleFilter) : super(filter) {
        searchName = filter.searchName
    }

    internal constructor(`in`: Parcel) : super(`in`)

    override fun readFromParcel(`in`: Parcel) {
        super.readFromParcel(`in`)
        `in`.writeString(searchName)
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        searchName = out.readString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ArticleFilter

        if (searchName != other.searchName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (searchName?.hashCode() ?: 0)
        return result
    }
}
