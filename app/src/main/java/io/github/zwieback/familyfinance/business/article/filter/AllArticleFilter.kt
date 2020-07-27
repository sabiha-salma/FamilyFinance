package io.github.zwieback.familyfinance.business.article.filter

import io.github.zwieback.familyfinance.constant.IdConstants.EMPTY_ID
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AllArticleFilter(
    override var parentId: Int = EMPTY_ID,
    override var searchName: String? = null
) : ArticleFilter() {

    companion object {
        const val ALL_ARTICLE_FILTER = "allArticleFilter"
    }
}
