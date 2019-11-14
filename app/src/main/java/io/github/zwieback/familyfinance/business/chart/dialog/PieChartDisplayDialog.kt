package io.github.zwieback.familyfinance.business.chart.dialog

import androidx.annotation.StringRes
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.chart.display.PieChartDisplay
import io.github.zwieback.familyfinance.business.chart.display.PieChartDisplay.Companion.PIE_CHART_DISPLAY
import io.github.zwieback.familyfinance.business.chart.display.type.PieChartGroupByType
import io.github.zwieback.familyfinance.business.chart.display.type.PieChartGroupingType
import io.github.zwieback.familyfinance.business.chart.exception.UnsupportedPieChartGroupByTypeException
import io.github.zwieback.familyfinance.business.chart.exception.UnsupportedPieChartGroupingTypeException
import io.github.zwieback.familyfinance.databinding.DialogDisplayChartPieBinding

class PieChartDisplayDialog : ChartDisplayDialog<PieChartDisplay, DialogDisplayChartPieBinding>() {

    override val inputDisplayName: String
        get() = PIE_CHART_DISPLAY

    override val dialogTitle: Int
        get() = R.string.pie_chart_of_expenses_display_title

    override val dialogLayoutId: Int
        get() = R.layout.dialog_display_chart_pie

    override fun createCopyOfDisplay(display: PieChartDisplay): PieChartDisplay {
        return PieChartDisplay(display)
    }

    override fun bind(display: PieChartDisplay) {
        binding.display = display
    }

    override fun updateDisplayProperties() {
        display.groupingType = determineGroupingType()
        display.groupByType = determineGroupByType()
    }

    private fun determineGroupingType(): PieChartGroupingType {
        return when {
            binding.groupingSimple.isChecked -> PieChartGroupingType.SIMPLE
            binding.groupingLimit.isChecked -> PieChartGroupingType.LIMIT
            else -> throw UnsupportedPieChartGroupingTypeException()
        }
    }

    private fun determineGroupByType(): PieChartGroupByType {
        return when {
            binding.groupByArticle.isChecked -> PieChartGroupByType.ARTICLE
            binding.groupByArticleParent.isChecked -> PieChartGroupByType.ARTICLE_PARENT
            else -> throw UnsupportedPieChartGroupByTypeException()
        }
    }

    companion object {
        fun newInstance(
            display: PieChartDisplay,
            @StringRes dialogTitleId: Int
        ): PieChartDisplayDialog {
            return PieChartDisplayDialog().apply {
                arguments = createArguments(PIE_CHART_DISPLAY, display, dialogTitleId)
            }
        }
    }
}
