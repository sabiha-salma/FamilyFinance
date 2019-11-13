package io.github.zwieback.familyfinance.business.article.activity

import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.article.filter.AllArticleFilter
import io.github.zwieback.familyfinance.business.article.filter.AllArticleFilter.Companion.ALL_ARTICLE_FILTER
import io.github.zwieback.familyfinance.business.article.fragment.AllArticleFragment

class AllArticleActivity : ArticleActivity<AllArticleFragment, AllArticleFilter>() {

    override val titleStringId: Int
        get() = R.string.all_article_activity_title

    override val filterName: String
        get() = ALL_ARTICLE_FILTER

    override val defaultParentId: Int?
        get() = null

    override fun createDefaultFilter(): AllArticleFilter {
        return AllArticleFilter().apply {
            setParentId(initialParentId)
        }
    }

    override fun createFragment(): AllArticleFragment {
        return AllArticleFragment.newInstance(filter)
    }
}
