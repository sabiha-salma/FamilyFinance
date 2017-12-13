package io.github.zwieback.familyfinance.business.chart.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import io.github.zwieback.familyfinance.app.FamilyFinanceApplication;
import io.github.zwieback.familyfinance.business.chart.display.ChartDisplay;
import io.github.zwieback.familyfinance.business.chart.listener.ChartDisplayListener;
import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter;
import io.github.zwieback.familyfinance.business.operation.listener.OperationFilterListener;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public abstract class ChartFragment<F extends OperationFilter, D extends ChartDisplay>
        extends Fragment implements OperationFilterListener<F>, ChartDisplayListener<D> {

    protected ReactiveEntityStore<Persistable> data;
    protected F filter;
    protected D display;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = ((FamilyFinanceApplication) ((Activity) extractContext()).getApplication()).getData();
        filter = loadFilter(savedInstanceState);
        display = loadDisplay(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getFilterName(), filter);
        outState.putParcelable(getDisplayName(), display);
    }

    private F loadFilter(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return createDefaultFilter();
        }
        return savedInstanceState.getParcelable(getFilterName());
    }

    private D loadDisplay(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return createDefaultDisplay();
        }
        return savedInstanceState.getParcelable(getDisplayName());
    }

    protected abstract String getFilterName();

    protected abstract F createDefaultFilter();

    protected abstract String getDisplayName();

    protected abstract D createDefaultDisplay();

    public abstract void showFilterDialog();

    public abstract void showDisplayDialog();

    @NonNull
    protected Context extractContext() {
        Context context = super.getContext();
        if (context != null) {
            return context;
        }
        throw new IllegalStateException("Context is null");
    }
}
