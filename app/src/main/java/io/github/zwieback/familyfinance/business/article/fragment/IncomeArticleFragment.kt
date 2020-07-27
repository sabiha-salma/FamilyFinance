package io.github.zwieback.familyfinance.business.article.fragment

import io.github.zwieback.familyfinance.business.article.adapter.IncomeArticleAdapter
import io.github.zwieback.familyfinance.business.article.filter.ArticleFilter
import io.github.zwieback.familyfinance.business.article.filter.IncomeArticleFilter.Companion.INCOME_ARTICLE_FILTER

class IncomeArticleFragment : ArticleFragment<IncomeArticleAdapter>() {

    override val filterName: String
        get() = INCOME_ARTICLE_FILTER

    override fun createEntityAdapter(): IncomeArticleAdapter {
        val filter = extractFilter()
        return IncomeArticleAdapter(requireContext(), clickListener, data, filter)
    }

    companion object {
        fun newInstance(filter: ArticleFilter) = IncomeArticleFragment().apply {
            arguments = createArguments(INCOME_ARTICLE_FILTER, filter)
        }
    }
}
