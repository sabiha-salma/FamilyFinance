package io.github.zwieback.familyfinance.business.chart.marker

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import io.github.zwieback.familyfinance.R

@SuppressLint("ViewConstructor")
class PieChartMarkerView(
    context: Context,
    private val valueFormatter: ValueFormatter
) : MarkerView(context, R.layout.chart_pie_marker_view) {

    private val labelContent: TextView = findViewById(R.id.label_content)
    private val valueContent: TextView = findViewById(R.id.value_content)

    override fun refreshContent(e: Entry, highlight: Highlight?) {
        val pieEntry = e as PieEntry
        labelContent.text = pieEntry.label
        valueContent.text = valueFormatter.getFormattedValue(pieEntry.value)
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2.0f), (-(height * 2)).toFloat())
    }
}
