package io.github.zwieback.familyfinance.business.article.filter

import io.github.zwieback.familyfinance.constant.IdConstants.EMPTY_ID
import kotlinx.parcelize.Parcelize

@Parcelize
data class IncomeArticleFilter(
    override var parentId: Int = EMPTY_ID,
    override var searchName: String? = null
) : ArticleFilter() {

    companion object {
        const val INCOME_ARTICLE_FILTER = "incomeArticleFilter"
    }
}
