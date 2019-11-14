package io.github.zwieback.familyfinance.business.operation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter
import io.github.zwieback.familyfinance.business.operation.listener.OnOperationClickListener
import io.github.zwieback.familyfinance.business.operation.service.calculator.BalanceCalculator
import io.github.zwieback.familyfinance.core.adapter.BindingHolder
import io.github.zwieback.familyfinance.core.adapter.EntityAdapter
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.databinding.ItemOperationBinding
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.requery.Persistable
import io.requery.query.Result
import io.requery.reactivex.ReactiveEntityStore

abstract class OperationAdapter<FILTER : OperationFilter> internal constructor(
    context: Context,
    clickListener: OnOperationClickListener,
    data: ReactiveEntityStore<Persistable>,
    filter: FILTER
) : EntityAdapter<OperationView, FILTER, ItemOperationBinding, OnOperationClickListener>(
    OperationView.`$TYPE`,
    context,
    clickListener,
    data,
    filter
) {
    private val observable = OperationAdapterDataObserver()

    private var balanceView: TextView? = null
    private var balanceCalculation: Disposable? = null

    override fun inflate(inflater: LayoutInflater): ItemOperationBinding {
        return ItemOperationBinding.inflate(inflater)
    }

    override fun extractEntity(binding: ItemOperationBinding): OperationView {
        return binding.operation as OperationView
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun onBindViewHolder(
        operation: OperationView?,
        holder: BindingHolder<ItemOperationBinding>,
        position: Int
    ) {
        operation?.let {
            holder.binding.operation = operation
            holder.binding.value.setTextColor(provider.provideTextColor(operation))
            provider.setupIcon(holder.binding.icon.icon, operation)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        registerAdapterDataObserver(observable)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        cancelBalanceCalculation()
        unregisterAdapterDataObserver(observable)
        super.onDetachedFromRecyclerView(recyclerView)
    }

    fun setBalanceView(balanceView: TextView?) {
        this.balanceView = balanceView
    }

    override fun close() {
        super.close()
        cancelBalanceCalculation()
    }

    private fun calculateBalanceInBackground() {
        if (balanceView == null) {
            return
        }
        cancelBalanceCalculation()
        showBalanceCalculating()
        balanceCalculation =
            Single.fromCallable { performQuery() }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map { calculateBalance(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { balance -> showBalance(balance) }
    }

    private fun cancelBalanceCalculation() {
        balanceCalculation?.dispose()
    }

    private fun calculateBalance(queryResult: Result<OperationView>): String {
        return BalanceCalculator.calculateBalance(queryResult)
    }

    private fun showBalance(balance: String) {
        balanceView?.text = balance
    }

    private fun showBalanceCalculating() {
        balanceView?.setText(R.string.hint_calculating)
    }

    private inner class OperationAdapterDataObserver : RecyclerView.AdapterDataObserver() {

        override fun onChanged() {
            triggerUpdateProcessor()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            triggerUpdateProcessor()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            triggerUpdateProcessor()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            triggerUpdateProcessor()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            triggerUpdateProcessor()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            triggerUpdateProcessor()
        }

        private fun triggerUpdateProcessor() {
            calculateBalanceInBackground()
        }
    }
}
