package io.github.zwieback.familyfinance.business.chart.fragment;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.List;
import java.util.Map;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.business.chart.converter.OperationConverter;
import io.github.zwieback.familyfinance.business.chart.dialog.BarChartDisplayDialog;
import io.github.zwieback.familyfinance.business.chart.display.BarChartDisplay;
import io.github.zwieback.familyfinance.business.chart.display.ChartDisplay;
import io.github.zwieback.familyfinance.business.chart.exception.UnsupportedBarChartGroupTypeException;
import io.github.zwieback.familyfinance.business.chart.formatter.DayValueFormatter;
import io.github.zwieback.familyfinance.business.chart.formatter.LocalizedValueFormatter;
import io.github.zwieback.familyfinance.business.chart.formatter.MonthValueFormatter;
import io.github.zwieback.familyfinance.business.chart.formatter.QuarterValueFormatter;
import io.github.zwieback.familyfinance.business.chart.formatter.WeekValueFormatter;
import io.github.zwieback.familyfinance.business.chart.formatter.YearValueFormatter;
import io.github.zwieback.familyfinance.business.chart.marker.BarMarkerView;
import io.github.zwieback.familyfinance.business.operation.dialog.FlowOfFundsOperationFilterDialog;
import io.github.zwieback.familyfinance.business.operation.filter.FlowOfFundsOperationFilter;
import io.github.zwieback.familyfinance.business.operation.query.ExpenseOperationQueryBuilder;
import io.github.zwieback.familyfinance.business.operation.service.grouper.OperationGrouper;
import io.github.zwieback.familyfinance.business.operation.service.grouper.OperationGrouperByDay;
import io.github.zwieback.familyfinance.business.operation.service.grouper.OperationGrouperByMonth;
import io.github.zwieback.familyfinance.business.operation.service.grouper.OperationGrouperByQuarter;
import io.github.zwieback.familyfinance.business.operation.service.grouper.OperationGrouperByWeek;
import io.github.zwieback.familyfinance.business.operation.service.grouper.OperationGrouperByYear;
import io.github.zwieback.familyfinance.core.model.OperationView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.requery.query.Result;

public class BarChartFragment extends ChartFragment implements OnChartValueSelectedListener {

    private static final float NORMAL_GRANULARITY = 1f;
    private static final int Y_AXIS_ANIMATION_DURATION = 500;

    private FlowOfFundsOperationFilter filter;
    private BarChartDisplay display;
    private RectF onValueSelectedRectF;
    private BarChart chart;
    private int maxBarCount;
    private float barValueTextSize;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filter = new FlowOfFundsOperationFilter();
        display = new BarChartDisplay();
        maxBarCount = getResources().getInteger(R.integer.max_bar_count);
        barValueTextSize = getResources().getDimension(R.dimen.bar_value_text_size);
        onValueSelectedRectF = new RectF();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        chart = view.findViewById(R.id.bar_chart);
        setupChart();
        refreshData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_display:
                showDisplayDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getMenuId() {
        return R.menu.menu_chart_bar;
    }

    @Override
    protected boolean addFilterMenuItem() {
        return true;
    }

    private void setupChart() {
        chart.setOnChartValueSelectedListener(this);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);

        IAxisValueFormatter xAxisFormatter = determineXAxisFormatter();
        IAxisValueFormatter yAxisFormatter = new LargeValueFormatter();
        float xAxisYOffset = getResources().getDimension(R.dimen.x_axis_y_offset);
        int yAxisMinimum = getResources().getInteger(R.integer.y_axis_minimum);
        int xAxisRotationAngle = getResources().getInteger(R.integer.x_axis_label_rotation_angle);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(NORMAL_GRANULARITY);
        xAxis.setLabelCount(maxBarCount);
        xAxis.setLabelRotationAngle(xAxisRotationAngle);
        xAxis.setYOffset(xAxisYOffset);
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(yAxisMinimum);
        leftAxis.setValueFormatter(yAxisFormatter);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setAxisMinimum(yAxisMinimum);
        rightAxis.setValueFormatter(yAxisFormatter);

        setupMarker(xAxisFormatter);
    }

    private void setupXAxisValueFormatter(IAxisValueFormatter xAxisFormatter) {
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(xAxisFormatter);
    }

    private void setupMarker(IAxisValueFormatter xAxisFormatter) {
        BarMarkerView mv = new BarMarkerView(extractContext(), xAxisFormatter,
                new LocalizedValueFormatter());
        mv.setChartView(chart);
        chart.setMarker(mv);
    }

    private void refreshData() {
        clearData(R.string.bar_loading);

        Observable.fromCallable(this::getExpenses)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(this::convertExpenses)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showData);
    }

    private Result<OperationView> getExpenses() {
        return ExpenseOperationQueryBuilder.create(data)
                .setStartDate(filter.getStartDate())
                .setEndDate(filter.getEndDate())
                .setStartValue(filter.getStartValue())
                .setEndValue(filter.getEndValue())
                .setOwnerId(filter.getOwnerId())
                .setCurrencyId(filter.getCurrencyId())
                .setArticleId(filter.getArticleId())
                .setAccountId(filter.getAccountId())
                .build();
    }

    private List<BarEntry> convertExpenses(Result<OperationView> expenses) {
        OperationGrouper grouper = determineOperationGrouper();
        OperationConverter converter = new OperationConverter(extractContext());
        Map<Float, List<OperationView>> groupedExpenses = grouper.group(expenses.toList(),
                filter.getStartDate(), filter.getEndDate());
        return converter.convertToBarEntries(groupedExpenses, false);
    }

    private void showData(List<BarEntry> expenseEntries) {
        if (expenseEntries.isEmpty()) {
            clearData(R.string.bar_no_data);
            return;
        }
        BarDataSet expenseSet = new BarDataSet(expenseEntries,
                getString(R.string.bar_set_expenses));
        expenseSet.setDrawIcons(false);
        expenseSet.setColors(ContextCompat.getColor(extractContext(), R.color.colorExpense));

        BarData data = new BarData(expenseSet);
        data.setValueTextSize(barValueTextSize);
        data.setValueFormatter(new LargeValueFormatter());

        fixChartWidth(expenseEntries.size());
        chart.setData(data);
        chart.setVisibleXRangeMaximum(maxBarCount);
        chart.animateY(Y_AXIS_ANIMATION_DURATION);
    }

    private void clearData(@StringRes int noDataTextRes) {
        chart.setNoDataText(getString(noDataTextRes));
        chart.clear();
    }

    private void fixChartWidth(int numberOfEntries) {
        if (numberOfEntries < maxBarCount) {
            chart.fitScreen();
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null) {
            return;
        }
        RectF bounds = onValueSelectedRectF;
        chart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = chart.getPosition(e, YAxis.AxisDependency.LEFT);
        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() {
        // do nothing
    }

    @Override
    public void onApplyFilter(FlowOfFundsOperationFilter filter) {
        this.filter = filter;
        refreshData();
    }

    @Override
    public void onApplyDisplay(ChartDisplay display) {
        this.display = (BarChartDisplay) display;
        IAxisValueFormatter xAxisFormatter = determineXAxisFormatter();
        setupXAxisValueFormatter(xAxisFormatter);
        setupMarker(xAxisFormatter);
        refreshData();
    }

    @Override
    protected void showFilterDialog() {
        DialogFragment dialog = FlowOfFundsOperationFilterDialog.newInstance(filter,
                R.string.bar_chart_filter_title);
        dialog.show(getChildFragmentManager(), "FlowOfFundsOperationFilterDialog");
    }

    private void showDisplayDialog() {
        DialogFragment dialog = BarChartDisplayDialog.newInstance(display);
        dialog.show(getChildFragmentManager(), "BarChartDisplayDialog");
    }

    private OperationGrouper determineOperationGrouper() {
        switch (display.getGroupType()) {
            case DAYS:
                return new OperationGrouperByDay();
            case WEEKS:
                return new OperationGrouperByWeek();
            case MONTHS:
                return new OperationGrouperByMonth();
            case QUARTERS:
                return new OperationGrouperByQuarter();
            case YEARS:
                return new OperationGrouperByYear();
        }
        throw new UnsupportedBarChartGroupTypeException();
    }

    private IAxisValueFormatter determineXAxisFormatter() {
        switch (display.getGroupType()) {
            case DAYS:
                return new DayValueFormatter();
            case WEEKS:
                return new WeekValueFormatter();
            case MONTHS:
                return new MonthValueFormatter();
            case QUARTERS:
                return new QuarterValueFormatter();
            case YEARS:
                return new YearValueFormatter();
        }
        throw new UnsupportedBarChartGroupTypeException();
    }
}
