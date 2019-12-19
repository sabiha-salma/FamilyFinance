package io.github.zwieback.familyfinance.business.chart.fragment

import android.graphics.RectF
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.chart.display.BarChartDisplay
import io.github.zwieback.familyfinance.business.chart.display.HorizontalBarChartDisplay
import io.github.zwieback.familyfinance.business.chart.display.type.HorizontalBarChartGroupByType
import io.github.zwieback.familyfinance.business.chart.marker.HorizontalBarChartMarkerView
import io.github.zwieback.familyfinance.business.chart.service.builder.IdIndexMapStatefulBuilder
import io.github.zwieback.familyfinance.business.chart.service.converter.OperationConverter
import io.github.zwieback.familyfinance.business.chart.service.converter.bar.OperationHorizontalBarConverter
import io.github.zwieback.familyfinance.business.chart.service.converter.bar.OperationHorizontalBarPercentConverter
import io.github.zwieback.familyfinance.business.chart.service.formatter.LocalizedValueFormatter
import io.github.zwieback.familyfinance.business.chart.service.grouper.OperationGrouper
import io.github.zwieback.familyfinance.business.chart.service.grouper.pie.OperationGrouperByArticle
import io.github.zwieback.familyfinance.business.chart.service.grouper.pie.OperationGrouperByArticleParent
import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter
import io.github.zwieback.familyfinance.core.model.Article
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.util.StringUtils

abstract class HorizontalBarChartFragment<F : OperationFilter> :
    ChartFragment<HorizontalBarChart, BarEntry, F, HorizontalBarChartDisplay>(),
    OnChartValueSelectedListener {

    private var maxBarCountOnScreen: Int = 0
    private var barValueTextSize: Float = 0.toFloat()
    private lateinit var onValueSelectedRectF: RectF
    private lateinit var idIndexMapStatefulBuilder: IdIndexMapStatefulBuilder

    override val fragmentChartLayout: Int
        get() = R.layout.fragment_chart_bar_horizontal

    override val chartId: Int
        get() = R.id.horizontal_bar_chart

    override val displayName: String
        get() = BarChartDisplay.BAR_CHART_DISPLAY

    @get:StringRes
    protected abstract val dataSetLabel: Int

    @get:ColorRes
    protected abstract val dataSetColor: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        maxBarCountOnScreen = resources.getInteger(R.integer.max_horizontal_bar_count_on_screen)
        barValueTextSize = resources.getDimension(R.dimen.bar_value_text_size)
        onValueSelectedRectF = RectF()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshData()
    }

    override fun createDefaultDisplay(): HorizontalBarChartDisplay {
        return HorizontalBarChartDisplay()
    }

    override fun setupChart() {
        chart.setOnChartValueSelectedListener(this)
        chart.setDrawValueAboveBar(true)
        chart.description.isEnabled = false

        val yAxisMinimum = resources.getInteger(R.integer.y_axis_minimum)

        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            granularity = NORMAL_GRANULARITY
            labelCount = maxBarCountOnScreen
        }
        chart.axisLeft.apply {
            setDrawGridLines(true)
            axisMinimum = yAxisMinimum.toFloat()
        }
        chart.axisRight.apply {
            setDrawGridLines(false)
            axisMinimum = yAxisMinimum.toFloat()
        }
        disableLegend()
    }

    private fun disableLegend() {
        val legend = chart.legend
        legend.isEnabled = false
    }

    private fun setupXAxisValueFormatter(xAxisFormatter: ValueFormatter) {
        val xAxis = chart.xAxis
        xAxis.valueFormatter = xAxisFormatter
    }

    private fun setupMarker(
        xAxisFormatter: ValueFormatter,
        yAxisFormatter: ValueFormatter
    ) {
        val mv = HorizontalBarChartMarkerView(
            requireContext(),
            xAxisFormatter, yAxisFormatter
        )
        mv.chartView = chart
        chart.marker = mv
    }

    override fun showData(groupedOperations: Map<Float, List<OperationView>>) {
        if (groupedOperations.isEmpty()) {
            clearData(R.string.chart_no_data)
            return
        }

        val barEntries = convertOperations(groupedOperations)
        val idIndexMap = idIndexMapStatefulBuilder.build()

        val articleNames = convertToArticleNames(idIndexMap)
        val xAxisFormatter = determineXAxisFormatter(articleNames)
        setupXAxisValueFormatter(xAxisFormatter)
        setupMarker(xAxisFormatter, determineYAxisFormatter())

        val expenseSet = buildBarDataSet(
            barEntries, dataSetLabel, dataSetColor,
            display.isViewValues
        )

        val data = BarData(expenseSet)
        data.setValueTextSize(barValueTextSize)
        data.setValueFormatter(determineDataSetValueFormatter())

        chart.data = data
        chart.setVisibleXRangeMaximum(maxBarCountOnScreen.toFloat())
        fixChartWidth(groupedOperations.size)
        chart.moveViewTo(0f, 0f, YAxis.AxisDependency.LEFT)
        chart.animateY(Y_AXIS_ANIMATION_DURATION)
    }

    private fun convertToArticleNames(idIndexMap: Map<Float, Float>): Array<String> {
        val articleIds = idIndexMap.keys
            .asSequence()
            .map { articleId -> articleId.toInt() }
            .toSet()
        val articles = data
            .select(Article::class.java, Article.ID, Article.NAME)
            .where(Article.ID.`in`(articleIds))
            .get()
            .toList()
        val articleNames = Array(articles.size) { StringUtils.EMPTY }
        articles
            .forEach { article ->
                val articleId = article.id.toFloat()
                val barIndex = idIndexMap[articleId]?.toInt()
                    ?: error("BarIndex is null on $articleId")
                articleNames[barIndex] = article.name
            }
        return articleNames
    }

    private fun buildBarDataSet(
        barEntries: List<BarEntry>,
        @StringRes dataSetLabel: Int,
        @ColorRes dataSetColor: Int,
        drawValuesEnabled: Boolean
    ): BarDataSet {
        return BarDataSet(barEntries, getString(dataSetLabel)).apply {
            setDrawIcons(false)
            setColors(ContextCompat.getColor(requireContext(), dataSetColor))
            setDrawValues(drawValuesEnabled)
        }
    }

    private fun fixChartWidth(numberOfEntries: Int) {
        if (numberOfEntries < maxBarCountOnScreen) {
            chart.fitScreen()
        } else {
            scaleToAcceptableSize(numberOfEntries)
        }
    }

    private fun scaleToAcceptableSize(numberOfEntries: Int) {
        chart.viewPortHandler.setMaximumScaleX(numberOfEntries / 2.0f)
    }

    private fun updateDrawValues() {
        val chartData = chart.data
        chartData?.setDrawValues(display.isViewValues)
        chart.invalidate()
    }

    override fun onValueSelected(e: Entry?, h: Highlight) {
        (e as? BarEntry)?.let { barEntry ->
            val bounds = onValueSelectedRectF
            chart.getBarBounds(barEntry, bounds)
            val position = chart.getPosition(
                barEntry,
                chart.data.getDataSetByIndex(h.dataSetIndex).axisDependency
            )
            MPPointF.recycleInstance(position)
        }
    }

    override fun onNothingSelected() {
        // do nothing
    }

    override fun onApplyDisplay(display: HorizontalBarChartDisplay) {
        if (this.display.needRefreshData(display)) {
            this.display = display
            operationGrouper = determineOperationGrouper()
            operationConverter = determineOperationConverter()
            refreshData()
        } else {
            this.display = display
            updateDrawValues()
        }
    }

    override fun determineOperationConverter(): OperationConverter<BarEntry> {
        idIndexMapStatefulBuilder = IdIndexMapStatefulBuilder.create()
        return if (display.isUsePercentValues) {
            OperationHorizontalBarPercentConverter(requireContext(), idIndexMapStatefulBuilder)
        } else {
            OperationHorizontalBarConverter(requireContext(), idIndexMapStatefulBuilder)
        }
    }

    override fun determineOperationGrouper(): OperationGrouper {
        return when (display.groupByType) {
            HorizontalBarChartGroupByType.ARTICLE -> OperationGrouperByArticle()
            HorizontalBarChartGroupByType.ARTICLE_PARENT -> OperationGrouperByArticleParent()
        }
    }

    private fun determineXAxisFormatter(articleNames: Array<String>): ValueFormatter {
        return IndexAxisValueFormatter(articleNames)
    }

    private fun determineYAxisFormatter(): ValueFormatter {
        return if (display.isUsePercentValues) {
            PercentFormatter()
        } else {
            LocalizedValueFormatter()
        }
    }

    private fun determineDataSetValueFormatter(): ValueFormatter {
        return if (display.isUsePercentValues) {
            PercentFormatter()
        } else {
            LargeValueFormatter()
        }
    }

    companion object {
        private const val NORMAL_GRANULARITY = 1f
        private const val Y_AXIS_ANIMATION_DURATION = 500
    }
}
