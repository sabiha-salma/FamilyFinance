package io.github.zwieback.familyfinance.business.article.query

import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class AllArticleQueryBuilder private constructor(data: ReactiveEntityStore<Persistable>) :
    ArticleQueryBuilder<AllArticleQueryBuilder>(data) {

    companion object {
        fun create(data: ReactiveEntityStore<Persistable>): AllArticleQueryBuilder {
            return AllArticleQueryBuilder(data)
        }
    }
}
