package io.github.zwieback.familyfinance.business.chart.service.grouper.pie

import io.github.zwieback.familyfinance.core.model.OperationView

class OperationGrouperByArticleParent : PieOperationGrouper() {

    /**
     * Collect article parent ids without `null` (root) operations.
     *
     * @param operations source operations
     * @return set of article parent ids
     */
    override fun collectGroupIds(operations: List<OperationView>): Set<Int> {
        return operations
            .asSequence()
            .mapNotNull { operation -> operation.articleParentId }
            .toSet()
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun filterByGroup(
        articleParentId: Int,
        operations: List<OperationView>
    ): List<OperationView> {
        return operations.filter { operation -> articleParentId == operation.articleParentId }
    }
}
