package io.github.zwieback.familyfinance.business.chart.service.grouper

import io.github.zwieback.familyfinance.core.model.OperationView
import org.threeten.bp.LocalDate

interface OperationGrouper {

    fun group(
        operations: List<OperationView>,
        startDate: LocalDate,
        endDate: LocalDate
    ): Map<Float, List<OperationView>>
}
