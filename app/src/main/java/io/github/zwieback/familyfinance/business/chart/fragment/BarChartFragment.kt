package io.github.zwieback.familyfinance.business.chart.fragment

import android.graphics.RectF
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.chart.dialog.BarChartDisplayDialog
import io.github.zwieback.familyfinance.business.chart.display.BarChartDisplay
import io.github.zwieback.familyfinance.business.chart.display.type.BarChartGroupType
import io.github.zwieback.familyfinance.business.chart.marker.BarChartMarkerView
import io.github.zwieback.familyfinance.business.chart.service.converter.OperationConverter
import io.github.zwieback.familyfinance.business.chart.service.converter.bar.OperationBarConverter
import io.github.zwieback.familyfinance.business.chart.service.formatter.*
import io.github.zwieback.familyfinance.business.chart.service.grouper.OperationGrouper
import io.github.zwieback.familyfinance.business.chart.service.grouper.bar.*
import io.github.zwieback.familyfinance.business.chart.service.sieve.OperationSieve
import io.github.zwieback.familyfinance.business.operation.dialog.FlowOfFundsOperationFilterDialog
import io.github.zwieback.familyfinance.business.operation.filter.FlowOfFundsOperationFilter
import io.github.zwieback.familyfinance.business.operation.query.FlowOfFundsOperationQueryBuilder
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.core.model.type.OperationType
import io.requery.query.Result

class BarChartFragment :
    ChartFragment<BarChart, BarEntry, FlowOfFundsOperationFilter, BarChartDisplay>(),
    OnChartValueSelectedListener {

    private var maxBarCountOnScreen: Int = 0
    private var barValueTextSize: Float = 0.toFloat()
    private lateinit var onValueSelectedRectF: RectF
    private lateinit var operationSieve: OperationSieve

    override val fragmentChartLayout: Int
        get() = R.layout.fragment_chart_bar

    override val chartId: Int
        get() = R.id.bar_chart

    override val filterName: String
        get() = FlowOfFundsOperationFilter.FLOW_OF_FUNDS_OPERATION_FILTER

    override val displayName: String
        get() = BarChartDisplay.BAR_CHART_DISPLAY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        maxBarCountOnScreen = resources.getInteger(R.integer.max_bar_count_on_screen)
        barValueTextSize = resources.getDimension(R.dimen.bar_value_text_size)
        onValueSelectedRectF = RectF()
        operationSieve = OperationSieve()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshData()
    }

    override fun createDefaultFilter(): FlowOfFundsOperationFilter {
        return FlowOfFundsOperationFilter()
    }

    override fun createDefaultDisplay(): BarChartDisplay {
        return BarChartDisplay()
    }

    override fun setupChart() {
        chart.setOnChartValueSelectedListener(this)
        chart.setDrawValueAboveBar(true)
        chart.description.isEnabled = false

        val xAxisFormatter = determineXAxisFormatter()
        val yAxisFormatter = LargeValueFormatter()
        val xAxisYOffset = resources.getDimension(R.dimen.x_axis_y_offset)
        val yAxisMinimum = resources.getInteger(R.integer.y_axis_minimum)
        val xAxisRotationAngle = resources.getInteger(R.integer.x_axis_label_rotation_angle)

        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            setCenterAxisLabels(true)
            granularity = NORMAL_GRANULARITY
            labelCount = maxBarCountOnScreen
            labelRotationAngle = xAxisRotationAngle.toFloat()
            yOffset = xAxisYOffset
            valueFormatter = xAxisFormatter
        }
        chart.axisLeft.apply {
            axisMinimum = yAxisMinimum.toFloat()
            valueFormatter = yAxisFormatter
        }
        chart.axisRight.apply {
            axisMinimum = yAxisMinimum.toFloat()
            valueFormatter = yAxisFormatter
        }
        setupMarker(xAxisFormatter)
    }

    private fun setupXAxisValueFormatter(xAxisFormatter: ValueFormatter) {
        chart.xAxis.valueFormatter = xAxisFormatter
    }

    private fun setupMarker(xAxisFormatter: ValueFormatter) {
        val mv = BarChartMarkerView(
            requireContext(), xAxisFormatter,
            LocalizedValueFormatter()
        )
        mv.chartView = chart
        chart.marker = mv
    }

    override fun buildOperations(): Result<OperationView> {
        return FlowOfFundsOperationQueryBuilder.create(data)
            .withTypes(determineOperationTypes())
            .withStartDate(filter.startDate)
            .withEndDate(filter.endDate)
            .withStartValue(filter.startValue)
            .withEndValue(filter.endValue)
            .withOwnerId(filter.getOwnerId())
            .withCurrencyId(filter.getCurrencyId())
            .withArticleId(filter.getArticleId())
            .withAccountId(filter.getAccountId())
            .build()
    }

    private fun filterOperations(
        operations: Map<Float, List<OperationView>>,
        types: List<OperationType>
    ): Map<Float, List<OperationView>> {
        return operationSieve.filterByTypes(operations, types)
    }

    override fun showData(groupedOperations: Map<Float, List<OperationView>>) {
        if (groupedOperations.isEmpty()) {
            clearData(R.string.chart_no_data)
            return
        }
        val incomeSet = buildBarDataSet(
            groupedOperations, OperationType.incomeTypes,
            R.string.data_set_incomes, R.color.colorIncome, display.isViewIncomeValues,
            display.isViewIncomes
        )
        val expenseSet = buildBarDataSet(
            groupedOperations, OperationType.expenseTypes,
            R.string.data_set_expenses, R.color.colorExpense, display.isViewExpenseValues,
            display.isViewExpenses
        )
        val barData = BarData(incomeSet, expenseSet).apply {
            setValueTextSize(barValueTextSize)
            setValueFormatter(LargeValueFormatter())
        }
        val minX = groupedOperations.keys.min() ?: error("groupedOperations is empty")
        val maxX = groupedOperations.keys.max() ?: error("groupedOperations is empty")

        chart.data = barData
        chart.barData.barWidth = BAR_WIDTH
        chart.xAxis.axisMinimum = minX
        chart.xAxis.axisMaximum = maxX + X_AXIS_MAXIMUM_FIX
        chart.groupBars(minX, GROUP_SPACE, BAR_SPACE)
        chart.setVisibleXRangeMaximum(maxBarCountOnScreen.toFloat())
        fixChartWidth(groupedOperations.size)
        chart.animateY(Y_AXIS_ANIMATION_DURATION)
    }

    private fun buildBarDataSet(
        groupedOperations: Map<Float, List<OperationView>>,
        types: List<OperationType>,
        @StringRes dataSetLabel: Int,
        @ColorRes dataSetColor: Int,
        drawValuesEnabled: Boolean,
        visible: Boolean
    ): BarDataSet {
        val operations = filterOperations(groupedOperations, types)
        val barEntries = convertOperations(operations)
        return BarDataSet(barEntries, getString(dataSetLabel)).apply {
            setDrawIcons(false)
            setColors(ContextCompat.getColor(requireContext(), dataSetColor))
            setDrawValues(drawValuesEnabled)
            isVisible = visible
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
        updateDrawValues(INCOME_BAR_SET, display.isViewIncomeValues)
        updateDrawValues(EXPENSE_BAR_SET, display.isViewExpenseValues)
        chart.invalidate()
    }

    private fun updateDrawValues(index: Int, enabled: Boolean) {
        chart.barData.getDataSetByIndex(index).setDrawValues(enabled)
    }

    override fun onValueSelected(e: Entry?, h: Highlight) {
        if (e == null) {
            return
        }
        val bounds = onValueSelectedRectF
        chart.getBarBounds(e as BarEntry, bounds)
        val position = chart.getPosition(e, YAxis.AxisDependency.LEFT)
        MPPointF.recycleInstance(position)
    }

    override fun onNothingSelected() {
        // do nothing
    }

    override fun onApplyFilter(filter: FlowOfFundsOperationFilter) {
        this.filter = filter
        refreshData()
    }

    override fun onApplyDisplay(display: BarChartDisplay) {
        if (this.display.needRefreshData(display)) {
            this.display = display
            operationGrouper = determineOperationGrouper()
            val xAxisFormatter = determineXAxisFormatter()
            setupXAxisValueFormatter(xAxisFormatter)
            setupMarker(xAxisFormatter)
            refreshData()
        } else {
            this.display = display
            updateDrawValues()
        }
    }

    override fun showFilterDialog() {
        FlowOfFundsOperationFilterDialog
            .newInstance(filter, R.string.bar_chart_filter_title)
            .show(childFragmentManager, "FlowOfFundsOperationFilterDialog")
    }

    override fun showDisplayDialog() {
        BarChartDisplayDialog
            .newInstance(display)
            .show(childFragmentManager, "BarChartDisplayDialog")
    }

    override fun determineOperationConverter(): OperationConverter<BarEntry> {
        return OperationBarConverter(requireContext())
    }

    override fun determineOperationGrouper(): OperationGrouper {
        return when (display.groupType) {
            BarChartGroupType.DAYS -> OperationGrouperByDay()
            BarChartGroupType.WEEKS -> OperationGrouperByWeek()
            BarChartGroupType.MONTHS -> OperationGrouperByMonth()
            BarChartGroupType.QUARTERS -> OperationGrouperByQuarter()
            BarChartGroupType.YEARS -> OperationGrouperByYear()
        }
    }

    private fun determineXAxisFormatter(): ValueFormatter {
        return when (display.groupType) {
            BarChartGroupType.DAYS -> DayValueFormatter()
            BarChartGroupType.WEEKS -> WeekValueFormatter()
            BarChartGroupType.MONTHS -> MonthValueFormatter()
            BarChartGroupType.QUARTERS -> QuarterValueFormatter()
            BarChartGroupType.YEARS -> YearValueFormatter()
        }
    }

    private fun determineOperationTypes(): List<OperationType> {
        val types = mutableListOf<OperationType>()
        if (display.isViewIncomes) {
            types.add(OperationType.INCOME_OPERATION)
            if (display.isIncludeTransfers) {
                types.add(OperationType.TRANSFER_INCOME_OPERATION)
            }
        }
        if (display.isViewExpenses) {
            types.add(OperationType.EXPENSE_OPERATION)
            if (display.isIncludeTransfers) {
                types.add(OperationType.TRANSFER_EXPENSE_OPERATION)
            }
        }
        return types
    }

    companion object {
        private const val NORMAL_GRANULARITY = 1f
        private const val X_AXIS_MAXIMUM_FIX = 1f

        // (barWidth + barSpace) * 2 + groupSpace = 1.00 -> interval per "group"
        private const val GROUP_SPACE = 0.08f
        private const val BAR_SPACE = 0.06f // x2 DataSet
        private const val BAR_WIDTH = 0.4f // x2 DataSet

        private const val INCOME_BAR_SET = 0
        private const val EXPENSE_BAR_SET = 1
        private const val Y_AXIS_ANIMATION_DURATION = 500
    }
}
