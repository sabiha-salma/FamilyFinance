package io.github.zwieback.familyfinance.business.chart.marker

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import io.github.zwieback.familyfinance.R

@SuppressLint("ViewConstructor")
class HorizontalBarChartMarkerView(
    context: Context,
    private val xAxisValueFormatter: ValueFormatter,
    private val yAxisValueFormatter: ValueFormatter
) : MarkerView(context, R.layout.chart_pie_marker_view) {

    private val labelContent: TextView = findViewById(R.id.label_content)
    private val valueContent: TextView = findViewById(R.id.value_content)

    override fun refreshContent(e: Entry, highlight: Highlight?) {
        labelContent.text = xAxisValueFormatter.getFormattedValue(e.x)
        valueContent.text = yAxisValueFormatter.getFormattedValue(e.y)
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(0f, (-height).toFloat())
    }
}
