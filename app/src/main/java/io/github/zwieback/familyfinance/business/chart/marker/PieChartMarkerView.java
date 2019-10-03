package io.github.zwieback.familyfinance.business.chart.marker;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import io.github.zwieback.familyfinance.R;

@SuppressLint("ViewConstructor")
public class PieChartMarkerView extends MarkerView {

    @NonNull
    private final TextView labelContent;
    @NonNull
    private final TextView valueContent;
    @NonNull
    private final IAxisValueFormatter valueFormatter;

    public PieChartMarkerView(@NonNull Context context,
                              @NonNull IAxisValueFormatter valueFormatter) {
        super(context, R.layout.chart_pie_marker_view);
        this.valueFormatter = valueFormatter;
        this.labelContent = findViewById(R.id.label_content);
        this.valueContent = findViewById(R.id.value_content);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        PieEntry pieEntry = (PieEntry) e;
        labelContent.setText(pieEntry.getLabel());
        valueContent.setText(valueFormatter.getFormattedValue(pieEntry.getValue(), null));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2.0f), -(getHeight() * 2));
    }
}
