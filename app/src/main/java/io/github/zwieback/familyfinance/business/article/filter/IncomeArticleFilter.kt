package io.github.zwieback.familyfinance.business.article.filter

import android.os.Parcel
import android.os.Parcelable

class IncomeArticleFilter : ArticleFilter {

    constructor() : super()

    constructor(filter: IncomeArticleFilter) : super(filter)

    private constructor(`in`: Parcel) : super(`in`)

    companion object {
        const val INCOME_ARTICLE_FILTER = "incomeArticleFilter"

        @JvmField
        var CREATOR: Parcelable.Creator<IncomeArticleFilter> =
            object : Parcelable.Creator<IncomeArticleFilter> {

                override fun createFromParcel(parcel: Parcel): IncomeArticleFilter {
                    return IncomeArticleFilter(parcel)
                }

                override fun newArray(size: Int): Array<IncomeArticleFilter?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
