package io.github.zwieback.familyfinance.business.exchange_rate.filter

import android.os.Parcel
import android.os.Parcelable
import io.github.zwieback.familyfinance.constant.IdConstants.EMPTY_ID
import io.github.zwieback.familyfinance.core.filter.EntityFilter
import io.github.zwieback.familyfinance.extension.toEmptyId
import io.github.zwieback.familyfinance.extension.toNullableId
import io.github.zwieback.familyfinance.util.DateUtils.readLocalDateFromParcel
import io.github.zwieback.familyfinance.util.DateUtils.writeLocalDateToParcel
import io.github.zwieback.familyfinance.util.NumberUtils.readBigDecimalFromParcel
import io.github.zwieback.familyfinance.util.NumberUtils.writeBigDecimalToParcel
import org.threeten.bp.LocalDate
import java.math.BigDecimal

class ExchangeRateFilter : EntityFilter {

    private var currencyId: Int = 0
    var startDate: LocalDate? = null
    var endDate: LocalDate? = null
    var startValue: BigDecimal? = null
    var endValue: BigDecimal? = null

    constructor() : super()

    constructor(filter: ExchangeRateFilter) : super(filter) {
        setCurrencyId(filter.getCurrencyId())
        startDate = filter.startDate
        endDate = filter.endDate
        startValue = filter.startValue
        endValue = filter.endValue
    }

    private constructor(`in`: Parcel) : super(`in`)

    override fun init() {
        super.init()
        currencyId = EMPTY_ID
    }

    override fun readFromParcel(`in`: Parcel) {
        super.readFromParcel(`in`)
        currencyId = `in`.readInt()
        startDate = readLocalDateFromParcel(`in`)
        endDate = readLocalDateFromParcel(`in`)
        startValue = readBigDecimalFromParcel(`in`)
        endValue = readBigDecimalFromParcel(`in`)
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(currencyId)
        writeLocalDateToParcel(out, startDate)
        writeLocalDateToParcel(out, endDate)
        writeBigDecimalToParcel(out, startValue)
        writeBigDecimalToParcel(out, endValue)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExchangeRateFilter

        if (currencyId != other.currencyId) return false
        if (startDate != other.startDate) return false
        if (endDate != other.endDate) return false
        if (startValue != other.startValue) return false
        if (endValue != other.endValue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = currencyId
        result = 31 * result + (startDate?.hashCode() ?: 0)
        result = 31 * result + (endDate?.hashCode() ?: 0)
        result = 31 * result + (startValue?.hashCode() ?: 0)
        result = 31 * result + (endValue?.hashCode() ?: 0)
        return result
    }

    fun getCurrencyId(): Int? {
        return currencyId.toNullableId()
    }

    fun setCurrencyId(currencyId: Int?) {
        this.currencyId = currencyId.toEmptyId()
    }

    companion object {
        const val EXCHANGE_RATE_FILTER = "exchangeRateFilter"

        @JvmField
        var CREATOR: Parcelable.Creator<ExchangeRateFilter> =
            object : Parcelable.Creator<ExchangeRateFilter> {

                override fun createFromParcel(parcel: Parcel): ExchangeRateFilter {
                    return ExchangeRateFilter(parcel)
                }

                override fun newArray(size: Int): Array<ExchangeRateFilter?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
