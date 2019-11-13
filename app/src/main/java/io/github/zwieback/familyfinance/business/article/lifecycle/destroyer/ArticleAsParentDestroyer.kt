package io.github.zwieback.familyfinance.business.article.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityAsParentDestroyer
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer
import io.github.zwieback.familyfinance.core.model.Article
import io.requery.Persistable
import io.requery.meta.QueryExpression
import io.requery.reactivex.ReactiveEntityStore

class ArticleAsParentDestroyer(context: Context, data: ReactiveEntityStore<Persistable>) :
    EntityAsParentDestroyer<Article>(context, data) {

    override val entityClass: Class<Article>
        get() = Article::class.java

    override val parentIdExpression: QueryExpression<Int>
        get() = Article.PARENT_ID

    override val alertResourceId: Int
        get() = R.string.articles_with_parent_exists

    override fun next(): EntityDestroyer<Article>? {
        return ArticleFromExpenseOperationsDestroyer(context, data)
    }
}
