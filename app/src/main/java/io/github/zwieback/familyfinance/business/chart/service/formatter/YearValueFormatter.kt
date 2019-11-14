package io.github.zwieback.familyfinance.business.chart.service.formatter

import com.github.mikephil.charting.formatter.ValueFormatter

class YearValueFormatter : ValueFormatter() {

    override fun getFormattedValue(year: Float): String {
        return year.toInt().toString()
    }
}
