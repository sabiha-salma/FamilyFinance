package io.github.zwieback.familyfinance.business.chart.dialog

import androidx.annotation.StringRes
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.chart.display.HorizontalBarChartDisplay
import io.github.zwieback.familyfinance.business.chart.display.HorizontalBarChartDisplay.Companion.HORIZONTAL_BAR_CHART_DISPLAY
import io.github.zwieback.familyfinance.business.chart.display.type.HorizontalBarChartGroupByType
import io.github.zwieback.familyfinance.business.chart.exception.UnsupportedHorizontalBarChartGroupByTypeException
import io.github.zwieback.familyfinance.databinding.DialogDisplayChartBarHorizontalBinding

class HorizontalBarChartDisplayDialog :
    ChartDisplayDialog<HorizontalBarChartDisplay, DialogDisplayChartBarHorizontalBinding>() {

    override val inputDisplayName: String
        get() = HORIZONTAL_BAR_CHART_DISPLAY

    override val dialogTitle: Int
        get() = R.string.horizontal_bar_chart_of_expenses_display_title

    override val dialogLayoutId: Int
        get() = R.layout.dialog_display_chart_bar_horizontal

    override fun createCopyOfDisplay(display: HorizontalBarChartDisplay): HorizontalBarChartDisplay {
        return HorizontalBarChartDisplay(display)
    }

    override fun bind(display: HorizontalBarChartDisplay) {
        binding.display = display
    }

    override fun updateDisplayProperties() {
        display.groupByType = determineGroupByType()
    }

    private fun determineGroupByType(): HorizontalBarChartGroupByType {
        return when {
            binding.groupByDays.isChecked -> HorizontalBarChartGroupByType.ARTICLE
            binding.groupByWeeks.isChecked -> HorizontalBarChartGroupByType.ARTICLE_PARENT
            else -> throw UnsupportedHorizontalBarChartGroupByTypeException()
        }
    }

    companion object {
        fun newInstance(
            display: HorizontalBarChartDisplay,
            @StringRes dialogTitleId: Int
        ): HorizontalBarChartDisplayDialog {
            return HorizontalBarChartDisplayDialog().apply {
                arguments = createArguments(HORIZONTAL_BAR_CHART_DISPLAY, display, dialogTitleId)
            }
        }
    }
}
