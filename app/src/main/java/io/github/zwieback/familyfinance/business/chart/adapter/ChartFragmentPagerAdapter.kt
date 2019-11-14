package io.github.zwieback.familyfinance.business.chart.adapter

import android.content.Context
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.chart.fragment.*

class ChartFragmentPagerAdapter(fm: FragmentManager, context: Context) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val registeredFragments: SparseArray<ChartFragment<*, *, *, *>> = SparseArray()
    private val tabTitles = arrayOfNulls<String>(PAGE_COUNT)

    init {
        initTabTitles(context)
    }

    private fun initTabTitles(context: Context) {
        tabTitles[BAR_CHART] =
            context.getString(R.string.bar_chart_title)
        tabTitles[HORIZONTAL_BAR_CHART_OF_EXPENSES] =
            context.getString(R.string.horizontal_bar_chart_of_expenses_title)
        tabTitles[HORIZONTAL_BAR_CHART_OF_INCOMES] =
            context.getString(R.string.horizontal_bar_chart_of_incomes_title)
        tabTitles[PIE_CHART_OF_EXPENSES] =
            context.getString(R.string.pie_chart_of_expenses_title)
        tabTitles[PIE_CHART_OF_INCOMES] =
            context.getString(R.string.pie_chart_of_incomes_title)
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            BAR_CHART -> BarChartFragment()
            HORIZONTAL_BAR_CHART_OF_EXPENSES -> HorizontalBarChartOfExpensesFragment()
            HORIZONTAL_BAR_CHART_OF_INCOMES -> HorizontalBarChartOfIncomesFragment()
            PIE_CHART_OF_EXPENSES -> PieChartOfExpensesFragment()
            PIE_CHART_OF_INCOMES -> PieChartOfIncomesFragment()
            else -> throw UnsupportedOperationException("Chart tab #$position isn't supported")
        }
    }

    override fun getCount(): Int {
        return PAGE_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as ChartFragment<*, *, *, *>
        registeredFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun findFragment(position: Int): ChartFragment<*, *, *, *>? {
        return registeredFragments.get(position)
    }

    companion object {
        private const val PAGE_COUNT = 5
        private const val BAR_CHART = 0
        private const val HORIZONTAL_BAR_CHART_OF_EXPENSES = 1
        private const val HORIZONTAL_BAR_CHART_OF_INCOMES = 2
        private const val PIE_CHART_OF_EXPENSES = 3
        private const val PIE_CHART_OF_INCOMES = 4
    }
}
