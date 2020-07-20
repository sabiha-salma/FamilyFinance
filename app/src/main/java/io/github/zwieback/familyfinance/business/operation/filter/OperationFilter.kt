package io.github.zwieback.familyfinance.business.operation.filter

import android.content.Context
import android.os.Parcel
import io.github.zwieback.familyfinance.constant.IdConstants.EMPTY_ID
import io.github.zwieback.familyfinance.core.filter.EntityFilter
import io.github.zwieback.familyfinance.core.preference.config.FilterPrefs
import io.github.zwieback.familyfinance.extension.*
import io.github.zwieback.familyfinance.util.NumberUtils.readBigDecimalFromParcel
import io.github.zwieback.familyfinance.util.NumberUtils.writeBigDecimalToParcel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.threeten.bp.LocalDate
import java.math.BigDecimal

abstract class OperationFilter : EntityFilter {

    lateinit var startDate: LocalDate
    lateinit var endDate: LocalDate
    var startValue: BigDecimal? = null
    var endValue: BigDecimal? = null
    private var articleId: Int = 0
    private var accountId: Int = 0
    private var ownerId: Int = 0
    private var currencyId: Int = 0

    constructor(context: Context) : super(context)

    constructor(filter: OperationFilter) : super(filter) {
        startDate = filter.startDate
        endDate = filter.endDate
        startValue = filter.startValue
        endValue = filter.endValue
        setArticleId(filter.getArticleId())
        setAccountId(filter.getAccountId())
        setOwnerId(filter.getOwnerId())
        setCurrencyId(filter.getCurrencyId())
    }

    protected constructor(`in`: Parcel) : super(`in`)

    override fun init(context: Context) {
        super.init(context)
        startDate = LocalDate.now().startOfMonth()
        runBlocking(Dispatchers.IO) {
            val filterPrefs = FilterPrefs.with(context)
            if (filterPrefs.includeLastDaysOfPreviousMonthOnStartDate) {
                val numberOfIncludedLastDays = filterPrefs.numberOfIncludedLastDays
                val currentDate = LocalDate.now()
                if (currentDate.dayOfMonth <= numberOfIncludedLastDays) {
                    startDate = startDate.minusDays(numberOfIncludedLastDays.toLong())
                }
            }
        }
        endDate = LocalDate.now().endOfMonth()
        articleId = EMPTY_ID
        accountId = EMPTY_ID
        ownerId = EMPTY_ID
        currencyId = EMPTY_ID
    }

    override fun readFromParcel(`in`: Parcel) {
        startDate = `in`.readLocalDate() ?: error("Can't read startDate from parcel")
        endDate = `in`.readLocalDate() ?: error("Can't read endDate from parcel")
        startValue = readBigDecimalFromParcel(`in`)
        endValue = readBigDecimalFromParcel(`in`)
        articleId = `in`.readInt()
        accountId = `in`.readInt()
        ownerId = `in`.readInt()
        currencyId = `in`.readInt()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeLocalDate(startDate)
        out.writeLocalDate(endDate)
        writeBigDecimalToParcel(out, startValue)
        writeBigDecimalToParcel(out, endValue)
        out.writeInt(articleId)
        out.writeInt(accountId)
        out.writeInt(ownerId)
        out.writeInt(currencyId)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OperationFilter

        if (startDate != other.startDate) return false
        if (endDate != other.endDate) return false
        if (startValue != other.startValue) return false
        if (endValue != other.endValue) return false
        if (articleId != other.articleId) return false
        if (accountId != other.accountId) return false
        if (ownerId != other.ownerId) return false
        if (currencyId != other.currencyId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = startDate.hashCode()
        result = 31 * result + endDate.hashCode()
        result = 31 * result + (startValue?.hashCode() ?: 0)
        result = 31 * result + (endValue?.hashCode() ?: 0)
        result = 31 * result + articleId
        result = 31 * result + accountId
        result = 31 * result + ownerId
        result = 31 * result + currencyId
        return result
    }

    fun getArticleId(): Int? {
        return articleId.toNullableId()
    }

    fun setArticleId(articleId: Int?) {
        this.articleId = articleId.toEmptyId()
    }

    fun getAccountId(): Int? {
        return accountId.toNullableId()
    }

    fun setAccountId(accountId: Int?) {
        this.accountId = accountId.toEmptyId()
    }

    fun getOwnerId(): Int? {
        return ownerId.toNullableId()
    }

    fun setOwnerId(ownerId: Int?) {
        this.ownerId = ownerId.toEmptyId()
    }

    fun getCurrencyId(): Int? {
        return currencyId.toNullableId()
    }

    fun setCurrencyId(currencyId: Int?) {
        this.currencyId = currencyId.toEmptyId()
    }
}
