package io.github.zwieback.familyfinance.business.chart.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.data.Entry
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.app.FamilyFinanceApplication
import io.github.zwieback.familyfinance.business.chart.display.ChartDisplay
import io.github.zwieback.familyfinance.business.chart.listener.ChartDisplayListener
import io.github.zwieback.familyfinance.business.chart.service.converter.OperationConverter
import io.github.zwieback.familyfinance.business.chart.service.grouper.OperationGrouper
import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter
import io.github.zwieback.familyfinance.business.operation.listener.OperationFilterListener
import io.github.zwieback.familyfinance.core.model.OperationView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.requery.Persistable
import io.requery.query.Result
import io.requery.reactivex.ReactiveEntityStore

abstract class ChartFragment<CHART, ENTRY, FILTER, DISPLAY> :
    Fragment(),
    OperationFilterListener<FILTER>,
    ChartDisplayListener<DISPLAY>
        where CHART : Chart<*>,
              ENTRY : Entry,
              FILTER : OperationFilter,
              DISPLAY : ChartDisplay<DISPLAY> {

    protected lateinit var data: ReactiveEntityStore<Persistable>
    protected lateinit var filter: FILTER
    protected lateinit var display: DISPLAY
    protected lateinit var chart: CHART

    protected lateinit var operationConverter: OperationConverter<ENTRY>
    protected lateinit var operationGrouper: OperationGrouper

    private val compositeDisposable = CompositeDisposable()

    var isDataLoaded: Boolean = false
        private set

    protected abstract val filterName: String

    protected abstract val displayName: String

    @get:LayoutRes
    protected abstract val fragmentChartLayout: Int

    @get:IdRes
    protected abstract val chartId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = ((requireContext() as Activity).application as FamilyFinanceApplication).data
        filter = loadFilter(savedInstanceState)
        display = loadDisplay(savedInstanceState)

        operationConverter = determineOperationConverter()
        operationGrouper = determineOperationGrouper()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(fragmentChartLayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        chart = view.findViewById(chartId)
        setupChart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(filterName, filter)
        outState.putParcelable(displayName, display)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun loadFilter(savedInstanceState: Bundle?): FILTER {
        return savedInstanceState?.getParcelable(filterName) ?: createDefaultFilter()
    }

    private fun loadDisplay(savedInstanceState: Bundle?): DISPLAY {
        return savedInstanceState?.getParcelable(displayName) ?: createDefaultDisplay()
    }

    protected abstract fun createDefaultFilter(): FILTER

    protected abstract fun createDefaultDisplay(): DISPLAY

    protected abstract fun setupChart()

    fun refreshData() {
        isDataLoaded = true
        clearData(R.string.chart_loading)

        compositeDisposable.add(
            Single.fromCallable { buildOperations() }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map { operations -> groupOperations(operations) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { groupedOperations -> showData(groupedOperations) }
        )
    }

    protected fun clearData(@StringRes noDataTextRes: Int) {
        chart.setNoDataText(getString(noDataTextRes))
        chart.clear()
    }

    protected abstract fun buildOperations(): Result<OperationView>

    private fun groupOperations(operations: Result<OperationView>): Map<Float, List<OperationView>> {
        return operationGrouper.group(operations.toList(), filter.startDate, filter.endDate)
    }

    protected fun convertOperations(groupedOperations: Map<Float, List<OperationView>>): List<ENTRY> {
        return operationConverter.convertToEntries(groupedOperations)
    }

    protected abstract fun showData(groupedOperations: Map<Float, List<OperationView>>)

    abstract fun showFilterDialog()

    abstract fun showDisplayDialog()

    protected abstract fun determineOperationConverter(): OperationConverter<ENTRY>

    protected abstract fun determineOperationGrouper(): OperationGrouper
}
