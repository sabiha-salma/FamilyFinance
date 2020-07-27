package io.github.zwieback.familyfinance.business.article.fragment

import io.github.zwieback.familyfinance.business.article.adapter.AllArticleAdapter
import io.github.zwieback.familyfinance.business.article.filter.AllArticleFilter.Companion.ALL_ARTICLE_FILTER
import io.github.zwieback.familyfinance.business.article.filter.ArticleFilter

class AllArticleFragment : ArticleFragment<AllArticleAdapter>() {

    override val filterName: String
        get() = ALL_ARTICLE_FILTER

    override fun createEntityAdapter(): AllArticleAdapter {
        val filter = extractFilter()
        return AllArticleAdapter(requireContext(), clickListener, data, filter)
    }

    companion object {
        fun newInstance(filter: ArticleFilter) = AllArticleFragment().apply {
            arguments = createArguments(ALL_ARTICLE_FILTER, filter)
        }
    }
}
