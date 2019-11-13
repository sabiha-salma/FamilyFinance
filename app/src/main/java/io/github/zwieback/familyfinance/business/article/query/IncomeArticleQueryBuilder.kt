package io.github.zwieback.familyfinance.business.article.query

import io.github.zwieback.familyfinance.core.model.type.ArticleType
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class IncomeArticleQueryBuilder private constructor(data: ReactiveEntityStore<Persistable>) :
    ArticleQueryBuilder<IncomeArticleQueryBuilder>(data) {

    companion object {
        fun create(data: ReactiveEntityStore<Persistable>): IncomeArticleQueryBuilder {
            return IncomeArticleQueryBuilder(data)
                .withType(ArticleType.INCOME_ARTICLE)
        }
    }
}
