package io.github.zwieback.familyfinance.business.account.query

import io.github.zwieback.familyfinance.core.model.Account
import io.github.zwieback.familyfinance.core.model.AccountView
import io.github.zwieback.familyfinance.core.query.EntityFolderQueryBuilder
import io.requery.Persistable
import io.requery.meta.QueryExpression
import io.requery.query.Limit
import io.requery.query.OrderBy
import io.requery.query.Where
import io.requery.query.WhereAndOr
import io.requery.reactivex.ReactiveEntityStore
import io.requery.reactivex.ReactiveResult

class AccountQueryBuilder private constructor(data: ReactiveEntityStore<Persistable>) :
    EntityFolderQueryBuilder<AccountQueryBuilder, AccountView>(data) {

    private var ownerId: Int? = null
    private var onlyActive: Boolean = false

    override val entityClass: Class<AccountView>
        get() = AccountView::class.java

    override val parentIdColumn: QueryExpression<Int>
        get() = Account.PARENT_ID

    fun withOwnerId(ownerId: Int?): AccountQueryBuilder {
        return apply { this.ownerId = ownerId }
    }

    fun withOnlyActive(onlyActive: Boolean): AccountQueryBuilder {
        return apply { this.onlyActive = onlyActive }
    }

    override fun buildWhere(
        select: Where<ReactiveResult<AccountView>>
    ): WhereAndOr<ReactiveResult<AccountView>> {
        var result = super.buildWhere(select)
        ownerId?.let {
            result = select
                .where(AccountView.PARENT_ID.eq(parentId).and(AccountView.OWNER_ID.isNull))
                .or(AccountView.PARENT_ID.eq(parentId).and(AccountView.OWNER_ID.eq(ownerId)))
        }
        if (onlyActive) {
            result = result.and(AccountView.ACTIVE.eq(true))
        }
        return result
    }

    override fun buildOrderBy(
        where: OrderBy<Limit<ReactiveResult<AccountView>>>
    ): Limit<ReactiveResult<AccountView>> {
        return where.orderBy(AccountView.ORDER_CODE)
    }

    companion object {
        fun create(data: ReactiveEntityStore<Persistable>): AccountQueryBuilder {
            return AccountQueryBuilder(data)
        }
    }
}
