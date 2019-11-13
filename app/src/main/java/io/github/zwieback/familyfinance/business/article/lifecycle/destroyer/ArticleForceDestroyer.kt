package io.github.zwieback.familyfinance.business.article.lifecycle.destroyer

import android.content.Context
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityForceDestroyer
import io.github.zwieback.familyfinance.core.model.Article
import io.requery.Persistable
import io.requery.meta.QueryAttribute
import io.requery.reactivex.ReactiveEntityStore

internal class ArticleForceDestroyer(context: Context, data: ReactiveEntityStore<Persistable>) :
    EntityForceDestroyer<Article>(context, data) {

    override val entityClass: Class<Article>
        get() = Article::class.java

    override val idAttribute: QueryAttribute<Article, Int>
        get() = Article.ID
}
