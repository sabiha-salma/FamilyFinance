package io.github.zwieback.familyfinance.business.chart.marker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import io.github.zwieback.familyfinance.R;

@SuppressLint("ViewConstructor")
public class BarChartMarkerView extends MarkerView {

    @NonNull
    private final TextView markerContent;
    @NonNull
    private final IAxisValueFormatter xAxisValueFormatter;
    @NonNull
    private final IAxisValueFormatter yAxisValueFormatter;

    public BarChartMarkerView(@NonNull Context context,
                              @NonNull IAxisValueFormatter xAxisValueFormatter,
                              @NonNull IAxisValueFormatter yAxisValueFormatter) {
        super(context, R.layout.chart_bar_marker_view);
        this.xAxisValueFormatter = xAxisValueFormatter;
        this.yAxisValueFormatter = yAxisValueFormatter;
        this.markerContent = findViewById(R.id.marker_content);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        markerContent.setText("x: " + xAxisValueFormatter.getFormattedValue(e.getX(), null) +
                "; y: " + yAxisValueFormatter.getFormattedValue(e.getY(), null));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2.0f), -getHeight());
    }
}
