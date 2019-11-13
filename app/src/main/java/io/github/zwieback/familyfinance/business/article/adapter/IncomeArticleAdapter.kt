package io.github.zwieback.familyfinance.business.article.adapter

import android.content.Context
import io.github.zwieback.familyfinance.business.article.filter.ArticleFilter
import io.github.zwieback.familyfinance.business.article.listener.OnArticleClickListener
import io.github.zwieback.familyfinance.business.article.query.IncomeArticleQueryBuilder
import io.github.zwieback.familyfinance.core.model.ArticleView
import io.requery.Persistable
import io.requery.query.Result
import io.requery.reactivex.ReactiveEntityStore

class IncomeArticleAdapter(
    context: Context,
    clickListener: OnArticleClickListener,
    data: ReactiveEntityStore<Persistable>,
    filter: ArticleFilter
) : ArticleAdapter(context, clickListener, data, filter) {

    override fun performQuery(): Result<ArticleView> {
        return IncomeArticleQueryBuilder.create(data)
            .withParentId(parentId)
            .withSearchName(filter.searchName)
            .build()
    }
}
