package io.github.zwieback.familyfinance.business.chart.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.business.chart.display.ChartDisplay;
import io.github.zwieback.familyfinance.business.chart.fragment.BarChartFragment;
import io.github.zwieback.familyfinance.business.chart.fragment.ChartFragment;
import io.github.zwieback.familyfinance.business.chart.listener.ChartDisplayListener;
import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter;
import io.github.zwieback.familyfinance.business.operation.listener.OperationFilterListener;
import io.github.zwieback.familyfinance.core.activity.DataActivityWrapper;

public class ChartActivity extends DataActivityWrapper
        implements OperationFilterListener<OperationFilter>, ChartDisplayListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceFragment(!isFirstFrame());
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_chart);
    }

    @Override
    protected int getTitleStringId() {
        return R.string.chart_activity_title;
    }

    // -----------------------------------------------------------------------------------------
    // Fragment methods
    // -----------------------------------------------------------------------------------------

    private boolean isFirstFrame() {
        return getSupportFragmentManager().getBackStackEntryCount() == 0;
    }

    @SuppressWarnings("unchecked")
    private void replaceFragment(boolean addToBackStack) {
        String tag = getFragmentTag();
        ChartFragment fragment = (ChartFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = createFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(getFragmentContainerId(), fragment, tag);
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
    }

    private String getFragmentTag() {
        return getLocalClassName();
    }

    @SuppressWarnings("unchecked")
    private <F extends ChartFragment> F createFragment() {
        return (F) new BarChartFragment();
    }

    @SuppressWarnings("unchecked")
    private <F extends ChartFragment> F findFragment() {
        return (F) getSupportFragmentManager().findFragmentById(getFragmentContainerId());
    }

    @IdRes
    private int getFragmentContainerId() {
        return R.id.chart_fragment;
    }

    // -----------------------------------------------------------------------------------------
    // Filter methods
    // -----------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public void onApplyFilter(OperationFilter filter) {
        findFragment().onApplyFilter(filter);
    }

    // -----------------------------------------------------------------------------------------
    // Display methods
    // -----------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public void onApplyDisplay(ChartDisplay display) {
        findFragment().onApplyDisplay(display);
    }
}
