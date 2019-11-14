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
class BarChartMarkerView(
    context: Context,
    private val xAxisValueFormatter: ValueFormatter,
    private val yAxisValueFormatter: ValueFormatter
) : MarkerView(context, R.layout.chart_bar_marker_view) {

    private val markerContent: TextView = findViewById(R.id.marker_content)

    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry, highlight: Highlight?) {
        markerContent.text = "x: ${xAxisValueFormatter.getFormattedValue(e.x)}" +
                "; y: ${yAxisValueFormatter.getFormattedValue(e.y)}"
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2.0f), (-height).toFloat())
    }
}
