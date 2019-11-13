package io.github.zwieback.familyfinance.business.article.filter

import android.os.Parcel
import android.os.Parcelable

class AllArticleFilter : ArticleFilter {

    constructor() : super()

    constructor(filter: AllArticleFilter) : super(filter)

    private constructor(`in`: Parcel) : super(`in`)

    companion object {
        const val ALL_ARTICLE_FILTER = "allArticleFilter"

        @JvmField
        var CREATOR: Parcelable.Creator<AllArticleFilter> =
            object : Parcelable.Creator<AllArticleFilter> {

                override fun createFromParcel(parcel: Parcel): AllArticleFilter {
                    return AllArticleFilter(parcel)
                }

                override fun newArray(size: Int): Array<AllArticleFilter?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
