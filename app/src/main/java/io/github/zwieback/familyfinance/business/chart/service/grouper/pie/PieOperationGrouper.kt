package io.github.zwieback.familyfinance.business.chart.service.grouper.pie

import io.github.zwieback.familyfinance.business.chart.service.grouper.OperationGrouper
import io.github.zwieback.familyfinance.core.model.OperationView
import org.threeten.bp.LocalDate

abstract class PieOperationGrouper : OperationGrouper {

    /**
     * Result.Key - group id.
     *
     * Result.Value - operations of that group.
     *
     * @param operations source operations
     * @return grouped operations
     */
    override fun group(
        operations: List<OperationView>,
        startDate: LocalDate,
        endDate: LocalDate
    ): Map<Float, List<OperationView>> {
        return collectGroupIds(operations)
            .associate { groupId ->
                groupId.toFloat() to filterByGroup(groupId, operations)
            }
    }

    protected abstract fun collectGroupIds(operations: List<OperationView>): Set<Int>

    protected abstract fun filterByGroup(
        groupId: Int,
        operations: List<OperationView>
    ): List<OperationView>
}
