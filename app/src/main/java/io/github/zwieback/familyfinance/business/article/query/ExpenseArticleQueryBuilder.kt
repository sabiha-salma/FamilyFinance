package io.github.zwieback.familyfinance.business.article.query

import io.github.zwieback.familyfinance.core.model.type.ArticleType
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore

class ExpenseArticleQueryBuilder private constructor(data: ReactiveEntityStore<Persistable>) :
    ArticleQueryBuilder<ExpenseArticleQueryBuilder>(data) {

    companion object {
        fun create(data: ReactiveEntityStore<Persistable>): ExpenseArticleQueryBuilder {
            return ExpenseArticleQueryBuilder(data)
                .withType(ArticleType.EXPENSE_ARTICLE)
        }
    }
}
