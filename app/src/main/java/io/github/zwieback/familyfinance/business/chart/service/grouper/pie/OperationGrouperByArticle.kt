package io.github.zwieback.familyfinance.business.chart.service.grouper.pie

import io.github.zwieback.familyfinance.core.model.OperationView

class OperationGrouperByArticle : PieOperationGrouper() {

    /**
     * Collect article ids.
     *
     * @param operations source operations
     * @return set of article ids
     */
    override fun collectGroupIds(operations: List<OperationView>): Set<Int> {
        return operations
            .asSequence()
            .map { it.articleId }
            .toSet()
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun filterByGroup(
        articleId: Int,
        operations: List<OperationView>
    ): List<OperationView> {
        return operations.filter { operation -> articleId == operation.articleId }
    }
}
