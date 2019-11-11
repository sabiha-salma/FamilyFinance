package io.github.zwieback.familyfinance.business.article.lifecycle.creator

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.model.Article
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class ArticleRootCreator(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : ArticleCreator(context, data) {

    override fun buildEntities(): Iterable<Article> {
        return sortedSetOf(
            this,
            createIncomeFolder(null, R.string.article_incomes),
            createExpenseFolder(null, R.string.article_expenses),
            createServiceFolder(null, R.string.article_service)
        )
    }
}
