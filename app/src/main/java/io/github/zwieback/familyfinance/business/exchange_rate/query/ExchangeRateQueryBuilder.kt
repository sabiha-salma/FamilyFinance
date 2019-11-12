package io.github.zwieback.familyfinance.business.exchange_rate.query

import io.github.zwieback.familyfinance.core.model.ExchangeRateView
import io.github.zwieback.familyfinance.core.query.EntityQueryBuilder
import io.requery.Persistable
import io.requery.query.Limit
import io.requery.query.OrderBy
import io.requery.query.Where
import io.requery.query.WhereAndOr
import io.requery.reactivex.ReactiveEntityStore
import io.requery.reactivex.ReactiveResult
import org.threeten.bp.LocalDate
import java.math.BigDecimal

class ExchangeRateQueryBuilder private constructor(data: ReactiveEntityStore<Persistable>) :
    EntityQueryBuilder<ExchangeRateView>(data) {

    private var currencyId: Int? = null
    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null
    private var startValue: BigDecimal? = null
    private var endValue: BigDecimal? = null

    override val entityClass: Class<ExchangeRateView>
        get() = ExchangeRateView::class.java

    fun withCurrencyId(currencyId: Int?): ExchangeRateQueryBuilder {
        return apply { this.currencyId = currencyId }
    }

    fun withStartDate(startDate: LocalDate?): ExchangeRateQueryBuilder {
        return apply { this.startDate = startDate }
    }

    fun withEndDate(endDate: LocalDate?): ExchangeRateQueryBuilder {
        return apply { this.endDate = endDate }
    }

    fun withStartValue(startValue: BigDecimal?): ExchangeRateQueryBuilder {
        return apply { this.startValue = startValue }
    }

    fun withEndValue(endValue: BigDecimal?): ExchangeRateQueryBuilder {
        return apply { this.endValue = endValue }
    }

    override fun buildWhere(
        select: Where<ReactiveResult<ExchangeRateView>>
    ): WhereAndOr<ReactiveResult<ExchangeRateView>> {
        var result = select.where(ExchangeRateView.ID.gt(0))
        currencyId?.let { currencyId ->
            result = result.and(ExchangeRateView.CURRENCY_ID.eq(currencyId))
        }
        startDate?.let { startDate ->
            result = result.and(ExchangeRateView.DATE.greaterThanOrEqual(startDate))
        }
        endDate?.let { endDate ->
            result = result.and(ExchangeRateView.DATE.lessThanOrEqual(endDate))
        }
        startValue?.let { startValue ->
            result = result.and(ExchangeRateView.VALUE.greaterThanOrEqual(startValue))
        }
        endValue?.let { endValue ->
            result = result.and(ExchangeRateView.VALUE.lessThanOrEqual(endValue))
        }
        return result
    }

    override fun buildOrderBy(
        where: OrderBy<Limit<ReactiveResult<ExchangeRateView>>>
    ): Limit<ReactiveResult<ExchangeRateView>> {
        return where.orderBy(ExchangeRateView.DATE.desc())
    }

    companion object {
        fun create(data: ReactiveEntityStore<Persistable>): ExchangeRateQueryBuilder {
            return ExchangeRateQueryBuilder(data)
        }
    }
}
