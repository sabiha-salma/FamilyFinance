package io.github.zwieback.familyfinance.business.article.query

import io.github.zwieback.familyfinance.core.model.ArticleView
import io.github.zwieback.familyfinance.core.model.type.ArticleType
import io.github.zwieback.familyfinance.core.query.EntityFolderQueryBuilder
import io.github.zwieback.familyfinance.util.TransliterationUtils.transliterate
import io.requery.Persistable
import io.requery.meta.QueryExpression
import io.requery.query.Limit
import io.requery.query.OrderBy
import io.requery.query.Where
import io.requery.query.WhereAndOr
import io.requery.reactivex.ReactiveEntityStore
import io.requery.reactivex.ReactiveResult
import java.util.*

abstract class ArticleQueryBuilder<T : ArticleQueryBuilder<T>> internal constructor(
    data: ReactiveEntityStore<Persistable>
) : EntityFolderQueryBuilder<T, ArticleView>(data) {

    private var type: ArticleType? = null
    private var searchName: String? = null

    override val entityClass: Class<ArticleView>
        get() = ArticleView::class.java

    override val parentIdColumn: QueryExpression<Int>
        get() = ArticleView.PARENT_ID

    @Suppress("UNCHECKED_CAST")
    fun withType(type: ArticleType?): T {
        return apply { this.type = type } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun withSearchName(searchName: String?): T {
        return apply { this.searchName = transliterate(searchName) } as T
    }

    override fun buildWhere(
        select: Where<ReactiveResult<ArticleView>>
    ): WhereAndOr<ReactiveResult<ArticleView>> {
        val searchNameInLowerCase = this.searchName.orEmpty().toLowerCase(Locale.getDefault())
        val where = if (searchNameInLowerCase.isNotEmpty()) {
            select.where(ArticleView.NAME_ASCII.lower().like("%${searchNameInLowerCase}%"))
        } else {
            super.buildWhere(select)
        }
        return type
            ?.let { type -> where.and(ArticleView.TYPE.eq(type)) }
            ?: where
    }

    override fun buildOrderBy(
        where: OrderBy<Limit<ReactiveResult<ArticleView>>>
    ): Limit<ReactiveResult<ArticleView>> {
        return where.orderBy(ArticleView.FOLDER.desc(), ArticleView.NAME)
    }
}
