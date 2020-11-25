package io.github.zwieback.familyfinance.business.operation.query

import io.github.zwieback.familyfinance.business.operation.type.OperationSortType
import io.github.zwieback.familyfinance.core.model.AccountView
import io.github.zwieback.familyfinance.core.model.ArticleView
import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.core.model.type.OperationType
import io.github.zwieback.familyfinance.core.query.EntityQueryBuilder
import io.github.zwieback.familyfinance.extension.endOfMonth
import io.github.zwieback.familyfinance.extension.startOfMonth
import io.github.zwieback.familyfinance.util.SqliteUtils
import io.requery.Persistable
import io.requery.meta.QueryAttribute
import io.requery.meta.Type
import io.requery.query.Limit
import io.requery.query.OrderBy
import io.requery.query.Where
import io.requery.query.WhereAndOr
import io.requery.reactivex.ReactiveEntityStore
import io.requery.reactivex.ReactiveResult
import org.threeten.bp.LocalDate
import java.math.BigDecimal

abstract class OperationQueryBuilder<T : OperationQueryBuilder<T>>(
    data: ReactiveEntityStore<Persistable>
) : EntityQueryBuilder<OperationView>(data) {

    private var startDate: LocalDate = LocalDate.now().startOfMonth()
    private var endDate: LocalDate = LocalDate.now().endOfMonth()
    private var startValue: BigDecimal? = null
    private var endValue: BigDecimal? = null
    private var types: List<OperationType>? = null
    private var articleId: Int? = null
    private var accountId: Int? = null
    private var ownerId: Int? = null
    private var toWhomId: Int? = null
    private var currencyId: Int? = null
    private var sortType: OperationSortType = OperationSortType.DEFAULT

    override val entityClass: Class<OperationView>
        get() = OperationView::class.java

    @Suppress("UNCHECKED_CAST")
    fun withStartDate(startDate: LocalDate): T {
        return apply { this.startDate = startDate } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun withEndDate(endDate: LocalDate): T {
        return apply { this.endDate = endDate } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun withStartValue(startValue: BigDecimal?): T {
        return apply { this.startValue = startValue } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun withEndValue(endValue: BigDecimal?): T {
        return apply { this.endValue = endValue } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun withTypes(types: List<OperationType>?): T {
        return apply { this.types = types } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun withArticleId(expenseArticleId: Int?): T {
        return apply { this.articleId = expenseArticleId } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun withAccountId(accountId: Int?): T {
        return apply { this.accountId = accountId } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun withOwnerId(ownerId: Int?): T {
        return apply { this.ownerId = ownerId } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun withToWhomId(toWhomId: Int?): T {
        return apply { this.toWhomId = toWhomId } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun withCurrencyId(currencyId: Int?): T {
        return apply { this.currencyId = currencyId } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun withSortType(sortType: OperationSortType): T {
        return apply { this.sortType = sortType } as T
    }

    override fun buildWhere(
        select: Where<ReactiveResult<OperationView>>
    ): WhereAndOr<ReactiveResult<OperationView>> {
        var result = select.where(
            OperationView.DATE.greaterThanOrEqual(startDate)
                .and(OperationView.DATE.lessThanOrEqual(endDate))
        )
        startValue?.let { startValue ->
            result = result.and(OperationView.VALUE.greaterThanOrEqual(startValue))
        }
        endValue?.let { endValue ->
            result = result.and(OperationView.VALUE.lessThanOrEqual(endValue))
        }
        types?.let { types ->
            result = result.and(OperationView.TYPE.`in`(types))
        }
        articleId?.let { articleId ->
            val articleIds = collectArticleIds(articleId)
            result = result.and(OperationView.ARTICLE_ID.`in`(articleIds))
        }
        accountId?.let { accountId ->
            val accountIds = collectAccountIds(accountId)
            result = result.and(OperationView.ACCOUNT_ID.`in`(accountIds))
        }
        ownerId?.let { ownerId ->
            result = result.and(OperationView.OWNER_ID.eq(ownerId))
        }
        toWhomId?.let { toWhomId ->
            result = result.and(OperationView.TO_WHOM_ID.eq(toWhomId))
        }
        currencyId?.let { currencyId ->
            result = result.and(OperationView.CURRENCY_ID.eq(currencyId))
        }
        return result
    }

    override fun buildOrderBy(
        where: OrderBy<Limit<ReactiveResult<OperationView>>>
    ): Limit<ReactiveResult<OperationView>> {
        return when (sortType) {
            OperationSortType.DEFAULT ->
                where.orderBy(OperationView.DATE.desc(), OperationView.ID.desc())
            OperationSortType.BY_CREATION_DATE ->
                where.orderBy(
                    OperationView.CREATE_DATE.desc(),
                    OperationView.DATE.desc(),
                    OperationView.ID.desc()
                )
            OperationSortType.BY_LAST_CHANGE_DATE ->
                where.orderBy(
                    OperationView.LAST_CHANGE_DATE.desc(),
                    OperationView.DATE.desc(),
                    OperationView.ID.desc()
                )
        }
    }

    /**
     * Collect all the accounts (this and its children) recursively.
     *
     * @param parentId an id of parent account
     * @return all accounts
     */
    private fun collectAccountIds(parentId: Int): Set<Int> {
        return collectEntityIds(
            parentId,
            AccountView.`$TYPE`,
            AccountView.ID,
            AccountView.PARENT_ID,
            { account -> account.id },
            { account -> account.parentId }
        )
    }

    /**
     * Collect all the articles (this and its children) recursively.
     *
     * @param parentId an id of parent article
     * @return all articles
     */
    private fun collectArticleIds(parentId: Int): Set<Int> {
        return collectEntityIds(
            parentId,
            ArticleView.`$TYPE`,
            ArticleView.ID,
            ArticleView.PARENT_ID,
            { article -> article.id },
            { article -> article.parentId }
        )
    }

    /**
     * Workaround to get all the entities (this and its children) recursively
     * without using CTE.
     *
     * NOTE: The `parentId` can be `null`
     */
    private fun <E : IBaseEntity> collectEntityIds(
        parentId: Int,
        entityType: Type<E>,
        entityIdAttribute: QueryAttribute<E, Int>,
        parentIdAttribute: QueryAttribute<E, Int>,
        getIdFunction: (E) -> Int,
        getParentIdFunction: (E) -> Int?
    ): Set<Int> {
        if (SqliteUtils.cteSupported()) {
            return collectEntityIdsThroughCte(parentId, entityType.name)
        }

        val idWithParentIdMap =
            data.select(entityType.classType, entityIdAttribute, parentIdAttribute)
                .get()
                .iterator()
                .asSequence()
                .associate { entity ->
                    getIdFunction(entity) to getParentIdFunction(entity)
                }

        return mutableSetOf(parentId).apply {
            addAll(collectChildIds(idWithParentIdMap, parentId))
        }
    }

    private fun collectChildIds(idWithParentIdMap: Map<Int, Int?>, parentId: Int): List<Int> {
        val childIds = idWithParentIdMap
            .asSequence()
            .filter { entry -> parentId == entry.value }
            .map { entry -> entry.key }
            .toList()

        if (childIds.isEmpty()) {
            return childIds
        }

        val result = ArrayList(childIds)
        childIds.forEach { childId ->
            result.addAll(collectChildIds(idWithParentIdMap, childId))
        }
        return result
    }

    private fun collectEntityIdsThroughCte(parentId: Int, tableName: String): Set<Int> {
        val query = "with recursive subtree" +
                " as (select id" +
                "       from $tableName" +
                "      where id = $parentId" +
                "      union all" +
                "     select child.id" +
                "       from $tableName as child" +
                "       join subtree on child.parent_id = subtree.id)" +
                " select id" +
                " from subtree"

        val result = data.raw(query)
        return result.iterator()
            .asSequence()
            .map { tuple -> tuple.get("id") as Int }
            .toSet()
    }
}
