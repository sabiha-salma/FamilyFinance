package io.github.zwieback.familyfinance.business.chart.listener

import io.github.zwieback.familyfinance.business.chart.display.ChartDisplay

interface ChartDisplayListener<D : ChartDisplay<D>> {

    fun onApplyDisplay(display: D)
}
