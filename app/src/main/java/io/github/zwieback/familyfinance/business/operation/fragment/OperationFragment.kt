package io.github.zwieback.familyfinance.business.operation.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.operation.adapter.OperationAdapter
import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter
import io.github.zwieback.familyfinance.business.operation.listener.OnOperationClickListener
import io.github.zwieback.familyfinance.business.operation.type.OperationSortType
import io.github.zwieback.familyfinance.core.fragment.EntityFragment
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.core.preference.config.InterfacePrefs
import io.github.zwieback.familyfinance.databinding.ItemOperationBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

abstract class OperationFragment<FILTER : OperationFilter> :
    EntityFragment<OperationView, FILTER, ItemOperationBinding, OnOperationClickListener, OperationAdapter<FILTER>>() {

    private lateinit var interfacePrefs: InterfacePrefs

    override val fragmentLayoutId: Int
        get() = R.layout.fragment_operation

    override val recyclerViewId: Int
        get() = R.id.recycler_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        interfacePrefs = runBlocking(Dispatchers.IO) {
            InterfacePrefs.with(requireContext())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBlocking {
            val isShowBalanceOnOperationScreens = withContext(Dispatchers.IO) {
                interfacePrefs.isShowBalanceOnOperationScreens
            }
            if (isShowBalanceOnOperationScreens) {
                val balanceViewGroup = view.findViewById<ViewGroup>(R.id.balance_group)
                val balanceView = balanceViewGroup.findViewById<TextView>(R.id.balance)
                adapter.setBalanceView(balanceView)
                balanceViewGroup.visibility = View.VISIBLE
            }
        }
    }

    fun changeSort(sortType: OperationSortType) {
        adapter.changeSort(sortType)
        adapter.queryAsync()
    }
}
