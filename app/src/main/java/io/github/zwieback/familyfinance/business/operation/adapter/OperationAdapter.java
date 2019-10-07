package io.github.zwieback.familyfinance.business.operation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter;
import io.github.zwieback.familyfinance.business.operation.listener.OnOperationClickListener;
import io.github.zwieback.familyfinance.business.operation.service.calculator.BalanceCalculator;
import io.github.zwieback.familyfinance.core.adapter.BindingHolder;
import io.github.zwieback.familyfinance.core.adapter.EntityAdapter;
import io.github.zwieback.familyfinance.core.model.OperationView;
import io.github.zwieback.familyfinance.databinding.ItemOperationBinding;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.reactivex.ReactiveEntityStore;

public abstract class OperationAdapter<FILTER extends OperationFilter>
        extends EntityAdapter<OperationView, FILTER, ItemOperationBinding,
        OnOperationClickListener> {

    @NonNull
    private final OperationAdapterDataObserver observable = new OperationAdapterDataObserver();

    @Nullable
    private TextView balanceView;
    @Nullable
    private Disposable balanceCalculation;

    OperationAdapter(Context context,
                     OnOperationClickListener clickListener,
                     ReactiveEntityStore<Persistable> data,
                     FILTER filter) {
        super(OperationView.$TYPE, context, clickListener, data, filter);
    }

    @Override
    protected ItemOperationBinding inflate(LayoutInflater inflater) {
        return ItemOperationBinding.inflate(inflater);
    }

    @Override
    protected OperationView extractEntity(ItemOperationBinding binding) {
        return (OperationView) binding.getOperation();
    }

    @Override
    public void onBindViewHolder(OperationView operation,
                                 BindingHolder<ItemOperationBinding> holder,
                                 int position) {
        holder.binding.setOperation(operation);
        holder.binding.value.setTextColor(provider.provideTextColor(operation));
        provider.setupIcon(holder.binding.icon.getIcon(), operation);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        registerAdapterDataObserver(observable);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        cancelBalanceCalculation();
        unregisterAdapterDataObserver(observable);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public final void setBalanceView(@Nullable TextView balanceView) {
        this.balanceView = balanceView;
    }

    @Override
    public void close() {
        super.close();
        cancelBalanceCalculation();
    }

    private void calculateBalanceInBackground() {
        if (balanceView == null) {
            return;
        }
        cancelBalanceCalculation();
        showBalance(R.string.hint_calculating);
        balanceCalculation = Single.fromCallable(this::performQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(this::calculateBalance)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showBalance);
    }

    private void cancelBalanceCalculation() {
        if (balanceCalculation != null) {
            balanceCalculation.dispose();
        }
    }

    private String calculateBalance(Result<OperationView> queryResult) {
        return BalanceCalculator.calculateBalance(queryResult);
    }

    private void showBalance(String balance) {
        if (balanceView != null) {
            balanceView.setText(balance);
        }
    }

    private void showBalance(@StringRes int resId) {
        if (balanceView != null) {
            balanceView.setText(resId);
        }
    }

    private class OperationAdapterDataObserver extends RecyclerView.AdapterDataObserver {

        @Override
        public void onChanged() {
            triggerUpdateProcessor();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            triggerUpdateProcessor();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            triggerUpdateProcessor();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            triggerUpdateProcessor();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            triggerUpdateProcessor();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            triggerUpdateProcessor();
        }

        private void triggerUpdateProcessor() {
            calculateBalanceInBackground();
        }
    }
}
