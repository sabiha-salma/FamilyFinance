package io.github.zwieback.familyfinance.business.article.fragment

import io.github.zwieback.familyfinance.business.article.adapter.ArticleAdapter
import io.github.zwieback.familyfinance.business.article.filter.ArticleFilter
import io.github.zwieback.familyfinance.business.article.listener.OnArticleClickListener
import io.github.zwieback.familyfinance.core.fragment.EntityFolderFragment
import io.github.zwieback.familyfinance.core.model.ArticleView
import io.github.zwieback.familyfinance.databinding.ItemArticleBinding

abstract class ArticleFragment<ADAPTER : ArticleAdapter> :
    EntityFolderFragment<
            ArticleView,
            ArticleFilter,
            ItemArticleBinding,
            OnArticleClickListener,
            ADAPTER
            >()
