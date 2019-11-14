package io.github.zwieback.familyfinance.business.chart.service.converter

import com.github.mikephil.charting.data.Entry
import io.github.zwieback.familyfinance.business.chart.service.grouper.OperationGrouper
import io.github.zwieback.familyfinance.core.model.OperationView

interface OperationConverter<E : Entry> {

    /**
     * Convert operations into the list of [Entry].
     *
     * NOTE: sorting is extremely important!
     *
     * @param operations operations that grouped by one of the [OperationGrouper]
     * @return list of entries to display in chart
     * @see [setVisibleXRangeMaximum not working as expected](https://github.com/PhilJay/MPAndroidChart/issues/983.issuecomment-152299035)
     * @see [The order of entries](https://github.com/PhilJay/MPAndroidChart/wiki/Setting-Data.the-order-of-entries)
     */
    fun convertToEntries(operations: Map<Float, List<OperationView>>): List<E>
}
