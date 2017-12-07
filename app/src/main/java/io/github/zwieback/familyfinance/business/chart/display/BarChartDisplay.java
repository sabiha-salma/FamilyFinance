package io.github.zwieback.familyfinance.business.chart.display;

import android.os.Parcel;
import android.support.annotation.NonNull;

import io.github.zwieback.familyfinance.business.chart.display.type.BarChartGroupType;

public class BarChartDisplay extends ChartDisplay {

    public static final String BAR_CHART_DISPLAY = "barChartDisplay";

    public static final Creator<BarChartDisplay> CREATOR = new Creator<BarChartDisplay>() {

        @Override
        public BarChartDisplay createFromParcel(Parcel in) {
            return new BarChartDisplay(in);
        }

        @Override
        public BarChartDisplay[] newArray(int size) {
            return new BarChartDisplay[size];
        }
    };

    /**
     * Initialized in the {@link #init} method.
     */
    @SuppressWarnings("NullableProblems")
    @NonNull
    private BarChartGroupType groupType;

    public BarChartDisplay() {
        super();
    }

    public BarChartDisplay(BarChartDisplay display) {
        super(display);
        groupType = display.groupType;
    }

    private BarChartDisplay(Parcel in) {
        super(in);
    }

    @Override
    protected void init() {
        groupType = BarChartGroupType.DAYS;
    }

    @Override
    protected void readFromParcel(Parcel in) {
        groupType = BarChartGroupType.valueOf(in.readString());
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(groupType.name());
    }

    @NonNull
    public BarChartGroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(@NonNull BarChartGroupType groupType) {
        this.groupType = groupType;
    }
}
