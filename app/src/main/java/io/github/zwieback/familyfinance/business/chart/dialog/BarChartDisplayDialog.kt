package io.github.zwieback.familyfinance.business.chart.dialog

import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.chart.display.BarChartDisplay
import io.github.zwieback.familyfinance.business.chart.display.BarChartDisplay.Companion.BAR_CHART_DISPLAY
import io.github.zwieback.familyfinance.business.chart.display.type.BarChartGroupType
import io.github.zwieback.familyfinance.business.chart.exception.UnsupportedBarChartGroupTypeException
import io.github.zwieback.familyfinance.databinding.DialogDisplayChartBarBinding

class BarChartDisplayDialog : ChartDisplayDialog<BarChartDisplay, DialogDisplayChartBarBinding>() {

    override val inputDisplayName: String
        get() = BAR_CHART_DISPLAY

    override val dialogTitle: Int
        get() = R.string.bar_chart_display_title

    override val dialogLayoutId: Int
        get() = R.layout.dialog_display_chart_bar

    override fun createCopyOfDisplay(display: BarChartDisplay): BarChartDisplay {
        return BarChartDisplay(display)
    }

    override fun bind(display: BarChartDisplay) {
        binding.display = display
    }

    override fun updateDisplayProperties() {
        display.groupType = determineGroupType()
    }

    private fun determineGroupType(): BarChartGroupType {
        return when {
            binding.groupByDays.isChecked -> BarChartGroupType.DAYS
            binding.groupByWeeks.isChecked -> BarChartGroupType.WEEKS
            binding.groupByMonths.isChecked -> BarChartGroupType.MONTHS
            binding.groupByQuarters.isChecked -> BarChartGroupType.QUARTERS
            binding.groupByYears.isChecked -> BarChartGroupType.YEARS
            else -> throw UnsupportedBarChartGroupTypeException()
        }
    }

    companion object {
        fun newInstance(display: BarChartDisplay) = BarChartDisplayDialog().apply {
            arguments = createArguments(BAR_CHART_DISPLAY, display)
        }
    }
}
