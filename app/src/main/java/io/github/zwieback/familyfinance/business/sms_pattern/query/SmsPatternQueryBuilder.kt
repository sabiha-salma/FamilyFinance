package io.github.zwieback.familyfinance.business.sms_pattern.query

import io.github.zwieback.familyfinance.core.model.SmsPattern
import io.github.zwieback.familyfinance.core.model.SmsPatternView
import io.github.zwieback.familyfinance.core.query.EntityQueryBuilder
import io.requery.Persistable
import io.requery.query.Limit
import io.requery.query.OrderBy
import io.requery.query.Where
import io.requery.query.WhereAndOr
import io.requery.reactivex.ReactiveEntityStore
import io.requery.reactivex.ReactiveResult

class SmsPatternQueryBuilder private constructor(data: ReactiveEntityStore<Persistable>) :
    EntityQueryBuilder<SmsPatternView>(data) {

    private var sender: String? = null
    private var useOrderByCommon: Boolean = false

    override val entityClass: Class<SmsPatternView>
        get() = SmsPatternView::class.java

    fun withSender(sender: String?): SmsPatternQueryBuilder {
        return apply { this.sender = sender }
    }

    fun withUseOrderByCommon(): SmsPatternQueryBuilder {
        return apply { this.useOrderByCommon = true }
    }

    override fun buildWhere(
        select: Where<ReactiveResult<SmsPatternView>>
    ): WhereAndOr<ReactiveResult<SmsPatternView>> {
        var result = select.where(SmsPatternView.ID.gt(0))
        sender?.let { sender ->
            result = result.and(SmsPattern.SENDER.equal(sender))
        }
        return result
    }

    override fun buildOrderBy(
        where: OrderBy<Limit<ReactiveResult<SmsPatternView>>>
    ): Limit<ReactiveResult<SmsPatternView>> {
        return if (useOrderByCommon) {
            where.orderBy(SmsPatternView.SENDER, SmsPatternView.NAME, SmsPatternView.COMMON)
        } else {
            where.orderBy(SmsPatternView.SENDER, SmsPatternView.NAME)
        }
    }

    companion object {
        fun create(data: ReactiveEntityStore<Persistable>): SmsPatternQueryBuilder {
            return SmsPatternQueryBuilder(data)
        }
    }
}
