package io.github.zwieback.familyfinance.business.chart.service.converter.pie

import android.content.Context
import com.github.mikephil.charting.data.PieEntry
import io.github.zwieback.familyfinance.business.chart.display.type.PieChartGroupByType
import io.github.zwieback.familyfinance.business.chart.service.calculator.OperationCalculator
import io.github.zwieback.familyfinance.business.chart.service.converter.OperationConverter
import io.github.zwieback.familyfinance.business.chart.service.grouper.pie.OperationGrouperByArticle
import io.github.zwieback.familyfinance.business.chart.service.grouper.pie.OperationGrouperByArticleParent
import io.github.zwieback.familyfinance.core.model.OperationView
import java.math.BigDecimal

open class OperationPieSimpleConverter(
    context: Context,
    private val groupByType: PieChartGroupByType
) : OperationConverter<PieEntry> {

    private val calculator: OperationCalculator = OperationCalculator(context)

    override fun convertToEntries(operations: Map<Float, List<OperationView>>): List<PieEntry> {
        val sumMap = convertToSumMap(operations)
        return sumMap
            .asSequence()
            .map { operationEntry -> convertToPieEntry(operationEntry) }
            .sortedBy { pieEntry -> pieEntry.value }
            .toList()
    }

    /**
     * Result.Key - article (parent) name.
     *
     * Result.Value - calculated sum of source operations.
     *
     * NOTE: Each group contains at least one operation. This is guaranteed
     * by [OperationGrouperByArticle] and [OperationGrouperByArticleParent]
     *
     * @param operations source operations
     * @return converted map
     */
    protected fun convertToSumMap(
        operations: Map<Float, List<OperationView>>
    ): Map<String, BigDecimal> {
        return operations
            .asSequence()
            .associate { entry ->
                determineGroupName(entry.value) to calculator.calculateSum(entry.value)
            }
    }

    protected fun convertToPieEntry(entry: Map.Entry<String, BigDecimal>): PieEntry {
        val sumOfOperations = entry.value.toFloat()
        val groupName = entry.key
        return PieEntry(sumOfOperations, groupName)
    }

    /**
     * @param operations source operations
     * @return group name of operations
     */
    private fun determineGroupName(operations: List<OperationView>): String {
        return when (groupByType) {
            PieChartGroupByType.ARTICLE -> operations[0].articleName
            PieChartGroupByType.ARTICLE_PARENT -> operations[0].articleParentName
                ?: error("Operation ${operations[0].id} hasn't articleParentName")
        }
    }
}
