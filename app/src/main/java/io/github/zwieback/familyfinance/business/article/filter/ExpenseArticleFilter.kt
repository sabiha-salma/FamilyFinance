package io.github.zwieback.familyfinance.business.article.filter

import android.os.Parcel
import android.os.Parcelable

class ExpenseArticleFilter : ArticleFilter {

    constructor() : super()

    constructor(filter: ExpenseArticleFilter) : super(filter)

    private constructor(`in`: Parcel) : super(`in`)

    companion object {
        const val EXPENSE_ARTICLE_FILTER = "expenseArticleFilter"

        @JvmField
        var CREATOR: Parcelable.Creator<ExpenseArticleFilter> =
            object : Parcelable.Creator<ExpenseArticleFilter> {

                override fun createFromParcel(parcel: Parcel): ExpenseArticleFilter {
                    return ExpenseArticleFilter(parcel)
                }

                override fun newArray(size: Int): Array<ExpenseArticleFilter?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
