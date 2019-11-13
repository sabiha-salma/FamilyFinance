package io.github.zwieback.familyfinance.business.article.fragment

import io.github.zwieback.familyfinance.business.article.adapter.ExpenseArticleAdapter
import io.github.zwieback.familyfinance.business.article.filter.ArticleFilter
import io.github.zwieback.familyfinance.business.article.filter.ExpenseArticleFilter.Companion.EXPENSE_ARTICLE_FILTER

class ExpenseArticleFragment : ArticleFragment<ExpenseArticleAdapter>() {

    override fun createEntityAdapter(): ExpenseArticleAdapter {
        val filter = extractFilter(EXPENSE_ARTICLE_FILTER)
        return ExpenseArticleAdapter(requireContext(), clickListener, data, filter)
    }

    companion object {
        fun newInstance(filter: ArticleFilter) = ExpenseArticleFragment().apply {
            arguments = createArguments(EXPENSE_ARTICLE_FILTER, filter)
        }
    }
}
