package io.github.zwieback.familyfinance.business.operation.filter

import io.github.zwieback.familyfinance.constant.IdConstants.EMPTY_ID
import io.github.zwieback.familyfinance.extension.BigDecimalParceler
import io.github.zwieback.familyfinance.extension.LocalDateParceler
import io.github.zwieback.familyfinance.extension.endOfMonth
import io.github.zwieback.familyfinance.extension.startOfMonth
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler
import org.threeten.bp.LocalDate
import java.math.BigDecimal

@Parcelize
@TypeParceler<LocalDate, LocalDateParceler>
@TypeParceler<BigDecimal?, BigDecimalParceler>
data class TransferOperationFilter(
    override var startDate: LocalDate = LocalDate.now().startOfMonth(),
    override var endDate: LocalDate = LocalDate.now().endOfMonth(),
    override var startValue: BigDecimal? = null,
    override var endValue: BigDecimal? = null,
    override var articleId: Int = EMPTY_ID,
    override var accountId: Int = EMPTY_ID,
    override var ownerId: Int = EMPTY_ID,
    override var currencyId: Int = EMPTY_ID
) : OperationFilter() {

    companion object {
        const val TRANSFER_OPERATION_FILTER = "transferOperationFilter"
    }
}
