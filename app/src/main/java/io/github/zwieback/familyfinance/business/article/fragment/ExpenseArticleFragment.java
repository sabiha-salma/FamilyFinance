package io.github.zwieback.familyfinance.business.article.fragment;

import android.os.Bundle;

import io.github.zwieback.familyfinance.business.article.adapter.ExpenseArticleAdapter;
import io.github.zwieback.familyfinance.business.article.filter.ArticleFilter;

import static io.github.zwieback.familyfinance.business.article.filter.ExpenseArticleFilter.EXPENSE_ARTICLE_FILTER;

public class ExpenseArticleFragment extends ArticleFragment<ExpenseArticleAdapter> {

    public static ExpenseArticleFragment newInstance(ArticleFilter filter) {
        ExpenseArticleFragment fragment = new ExpenseArticleFragment();
        Bundle args = Companion.createArguments(EXPENSE_ARTICLE_FILTER, filter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ExpenseArticleAdapter createEntityAdapter() {
        ArticleFilter filter = extractFilter(EXPENSE_ARTICLE_FILTER);
        return new ExpenseArticleAdapter(requireContext(), getClickListener(), getData(), filter);
    }
}
