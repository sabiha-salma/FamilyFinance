package io.github.zwieback.familyfinance.core.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.app.FamilyFinanceApplication
import io.github.zwieback.familyfinance.core.adapter.EntityAdapter
import io.github.zwieback.familyfinance.core.filter.EntityFilter
import io.github.zwieback.familyfinance.core.listener.EntityClickListener
import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

abstract class EntityFragment<
        ENTITY : IBaseEntity,
        FILTER : EntityFilter,
        BINDING : ViewDataBinding,
        LISTENER : EntityClickListener<ENTITY>,
        ADAPTER : EntityAdapter<ENTITY, FILTER, BINDING, LISTENER>> :
    Fragment() {

    protected lateinit var clickListener: LISTENER
    protected lateinit var data: ReactiveEntityStore<Persistable>
    protected lateinit var adapter: ADAPTER
    private lateinit var executor: ExecutorService

    protected open val fragmentLayoutId: Int
        @LayoutRes
        get() = R.layout.fragment_entity

    protected open val recyclerViewId: Int
        @IdRes
        get() = R.id.recycler_view

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EntityClickListener<*>) {
            this.clickListener = context as LISTENER
        } else {
            throw ClassCastException("$context must implement EntityClickListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = ((requireContext() as Activity).application as FamilyFinanceApplication).data
        executor = Executors.newSingleThreadExecutor()
        adapter = createEntityAdapter()
        adapter.setExecutor(executor)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(fragmentLayoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(recyclerViewId)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.queryAsync()
    }

    override fun onDestroy() {
        executor.shutdown()
        adapter.close()
        super.onDestroy()
    }

    protected abstract fun createEntityAdapter(): ADAPTER

    fun refresh() {
        adapter.queryAsync()
    }

    fun applyFilter(filter: FILTER) {
        adapter.applyFilter(filter)
        adapter.queryAsync()
    }

    protected fun extractFilter(filterName: String): FILTER {
        val savedFilter = requireArguments().getParcelable<FILTER>(filterName)
        return savedFilter ?: error("Filter wasn't saved early")
    }

    companion object {
        fun <FILTER : EntityFilter> createArguments(filterName: String, filter: FILTER): Bundle {
            return Bundle().apply {
                putParcelable(filterName, filter)
            }
        }
    }
}
