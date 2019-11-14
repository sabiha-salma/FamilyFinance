package io.github.zwieback.familyfinance.business.operation.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.operation.adapter.OperationAdapter
import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter
import io.github.zwieback.familyfinance.business.operation.listener.OnOperationClickListener
import io.github.zwieback.familyfinance.core.fragment.EntityFragment
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.core.preference.config.InterfacePrefs
import io.github.zwieback.familyfinance.databinding.ItemOperationBinding

abstract class OperationFragment<FILTER : OperationFilter> :
    EntityFragment<OperationView, FILTER, ItemOperationBinding, OnOperationClickListener, OperationAdapter<FILTER>>() {

    private lateinit var interfacePrefs: InterfacePrefs

    override val fragmentLayoutId: Int
        get() = R.layout.fragment_operation

    override val recyclerViewId: Int
        get() = R.id.recycler_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        interfacePrefs = InterfacePrefs.with(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (interfacePrefs.isShowBalanceOnOperationScreens) {
            val balanceView = view.findViewById<TextView>(R.id.balance)
            adapter.setBalanceView(balanceView)
        } else {
            val balanceViewGroup = view.findViewById<ViewGroup>(R.id.balance_group)
            balanceViewGroup.visibility = View.GONE
        }
    }
}
