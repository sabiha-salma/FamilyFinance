package io.github.zwieback.familyfinance.business.operation.filter

import io.github.zwieback.familyfinance.core.filter.EntityFilter
import io.github.zwieback.familyfinance.extension.toEmptyId
import io.github.zwieback.familyfinance.extension.toNullableId
import org.threeten.bp.LocalDate
import java.math.BigDecimal

abstract class OperationFilter : EntityFilter() {

    abstract var startDate: LocalDate
    abstract var endDate: LocalDate
    abstract var startValue: BigDecimal?
    abstract var endValue: BigDecimal?
    abstract var articleId: Int
    abstract var accountId: Int
    abstract var ownerId: Int
    abstract var currencyId: Int

    fun takeArticleId(): Int? {
        return articleId.toNullableId()
    }

    fun putArticleId(articleId: Int?) {
        this.articleId = articleId.toEmptyId()
    }

    fun takeAccountId(): Int? {
        return accountId.toNullableId()
    }

    fun putAccountId(accountId: Int?) {
        this.accountId = accountId.toEmptyId()
    }

    fun takeOwnerId(): Int? {
        return ownerId.toNullableId()
    }

    fun putOwnerId(ownerId: Int?) {
        this.ownerId = ownerId.toEmptyId()
    }

    fun takeCurrencyId(): Int? {
        return currencyId.toNullableId()
    }

    fun putCurrencyId(currencyId: Int?) {
        this.currencyId = currencyId.toEmptyId()
    }
}
