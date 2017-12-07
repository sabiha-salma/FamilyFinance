package io.github.zwieback.familyfinance.business.chart.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.annimon.stream.Stream;
import com.mikepenz.iconics.utils.IconicsMenuInflaterUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.app.FamilyFinanceApplication;
import io.github.zwieback.familyfinance.business.chart.listener.ChartDisplayListener;
import io.github.zwieback.familyfinance.business.operation.listener.FlowOfFundsOperationFilterListener;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public abstract class ChartFragment extends Fragment
        implements FlowOfFundsOperationFilterListener, ChartDisplayListener {

    protected ReactiveEntityStore<Persistable> data;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        data = ((FamilyFinanceApplication) ((Activity) extractContext()).getApplication()).getData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        List<Integer> menuIds = new ArrayList<>(collectMenuIds());
        if (addFilterMenuItem()) {
            menuIds.add(R.menu.menu_entity_filter);
        }
        Stream.of(menuIds).forEach(menuId ->
                IconicsMenuInflaterUtil.inflate(inflater, extractContext(), menuId, menu));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                showFilterDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected List<Integer> collectMenuIds() {
        return Collections.singletonList(getMenuId());
    }

    @MenuRes
    protected abstract int getMenuId();

    protected abstract boolean addFilterMenuItem();

    protected abstract void showFilterDialog();

    @NonNull
    public Context extractContext() {
        Context context = super.getContext();
        if (context != null) {
            return context;
        }
        throw new IllegalStateException("Context is null");
    }
}
