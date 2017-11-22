package io.github.zwieback.familyfinance.business.operation.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.business.operation.adapter.OperationAdapter;
import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter;
import io.github.zwieback.familyfinance.business.operation.listener.OnOperationClickListener;
import io.github.zwieback.familyfinance.core.fragment.EntityFragment;
import io.github.zwieback.familyfinance.core.model.OperationView;
import io.github.zwieback.familyfinance.databinding.ItemOperationBinding;

public abstract class OperationFragment<FILTER extends OperationFilter>
        extends EntityFragment<OperationView, FILTER, ItemOperationBinding,
        OnOperationClickListener, OperationAdapter<FILTER>> {

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView balanceView = view.findViewById(getBalanceViewId());
        adapter.setBalanceView(balanceView);
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_operation;
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.recycler_view;
    }

    @IdRes
    protected int getBalanceViewId() {
        return R.id.balance;
    }
}
