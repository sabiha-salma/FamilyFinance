package io.github.zwieback.familyfinance.business.chart.fragment

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.chart.display.PieChartDisplay
import io.github.zwieback.familyfinance.business.chart.display.type.PieChartGroupByType
import io.github.zwieback.familyfinance.business.chart.display.type.PieChartGroupingType
import io.github.zwieback.familyfinance.business.chart.marker.PieChartMarkerView
import io.github.zwieback.familyfinance.business.chart.service.converter.OperationConverter
import io.github.zwieback.familyfinance.business.chart.service.converter.pie.OperationPieLimitConverter
import io.github.zwieback.familyfinance.business.chart.service.converter.pie.OperationPieSimpleConverter
import io.github.zwieback.familyfinance.business.chart.service.formatter.LocalizedValueFormatter
import io.github.zwieback.familyfinance.business.chart.service.grouper.OperationGrouper
import io.github.zwieback.familyfinance.business.chart.service.grouper.pie.OperationGrouperByArticle
import io.github.zwieback.familyfinance.business.chart.service.grouper.pie.OperationGrouperByArticleParent
import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.extension.collectMaterialDesignColors
import io.github.zwieback.familyfinance.util.ConfigurationUtils

abstract class PieChartFragment<F : OperationFilter> :
    ChartFragment<PieChart, PieEntry, F, PieChartDisplay>() {

    private var pieValueTextSize: Float = 0.0f

    override val fragmentChartLayout: Int
        get() = R.layout.fragment_chart_pie

    override val chartId: Int
        get() = R.id.pie_chart

    override val displayName: String
        get() = PieChartDisplay.PIE_CHART_DISPLAY

    @get:StringRes
    protected abstract val dataSetLabel: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pieValueTextSize = resources.getDimension(R.dimen.pie_value_text_size)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshData()
    }

    override fun createDefaultDisplay(): PieChartDisplay {
        return PieChartDisplay()
    }

    override fun setupChart() {
        chart.setUsePercentValues(display.isUsePercentValues)
        chart.description.isEnabled = false
        chart.setEntryLabelColor(Color.BLACK)
        chart.setEntryLabelTextSize(pieValueTextSize)

        setupLegend()
        setupMarker()
    }

    private fun setupLegend() {
        val legend = chart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.orientation = determineLegendOrientation()
        legend.isWordWrapEnabled = true
    }

    private fun determineLegendOrientation(): Legend.LegendOrientation {
        return if (ConfigurationUtils.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Legend.LegendOrientation.HORIZONTAL
        } else {
            Legend.LegendOrientation.VERTICAL
        }
    }

    private fun setupMarker() {
        val mv = PieChartMarkerView(
            requireContext(),
            LocalizedValueFormatter()
        )
        mv.chartView = chart
        chart.marker = mv
    }

    override fun showData(groupedOperations: Map<Float, List<OperationView>>) {
        if (groupedOperations.isEmpty()) {
            clearData(R.string.chart_no_data)
            return
        }
        val expenseSet = buildPieDataSet(groupedOperations, dataSetLabel)
        val pieData = PieData(expenseSet).apply {
            setValueFormatter(determineFormatter())
            setDrawValues(display.isViewValues)
        }
        chart.setUsePercentValues(display.isUsePercentValues)
        chart.data = pieData
        chart.animateY(Y_AXIS_ANIMATION_DURATION, Easing.EaseInOutQuad)
    }

    private fun buildPieDataSet(
        operations: Map<Float, List<OperationView>>,
        @StringRes dataSetLabel: Int
    ): PieDataSet {
        val dataColors = collectDataColors()
        val pieEntries = convertOperations(operations)
        return PieDataSet(pieEntries, getString(dataSetLabel)).apply {
            setDrawIcons(false)
            colors = dataColors
            sliceSpace = SLICE_SPACE
            valueTextSize = pieValueTextSize
        }
    }

    private fun collectDataColors(): List<Int> {
        return requireContext().collectMaterialDesignColors().shuffled()
    }

    override fun onApplyFilter(filter: F) {
        this.filter = filter
        refreshData()
    }

    override fun onApplyDisplay(display: PieChartDisplay) {
        if (this.display.needRefreshData(display)) {
            this.display = display
            operationConverter = determineOperationConverter()
            operationGrouper = determineOperationGrouper()
            refreshData()
        } else {
            this.display = display
            chart.data?.let { chartData ->
                chartData.setDrawValues(display.isViewValues)
                chartData.setValueFormatter(determineFormatter())
            }
            chart.setUsePercentValues(display.isUsePercentValues)
            chart.invalidate()
        }
    }

    override fun determineOperationConverter(): OperationConverter<PieEntry> {
        return when (display.groupingType) {
            PieChartGroupingType.SIMPLE -> OperationPieSimpleConverter(
                requireContext(),
                display.groupByType
            )
            PieChartGroupingType.LIMIT -> OperationPieLimitConverter(
                requireContext(),
                display.groupByType
            )
        }
    }

    override fun determineOperationGrouper(): OperationGrouper {
        return when (display.groupByType) {
            PieChartGroupByType.ARTICLE -> OperationGrouperByArticle()
            PieChartGroupByType.ARTICLE_PARENT -> OperationGrouperByArticleParent()
        }
    }

    private fun determineFormatter(): ValueFormatter {
        return if (display.isUsePercentValues) {
            PercentFormatter()
        } else {
            LargeValueFormatter()
        }
    }

    companion object {
        private const val SLICE_SPACE = 2f
        private const val Y_AXIS_ANIMATION_DURATION = 500
    }
}
