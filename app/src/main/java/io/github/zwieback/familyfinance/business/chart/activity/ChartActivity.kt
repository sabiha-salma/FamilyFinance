package io.github.zwieback.familyfinance.business.chart.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.mikepenz.iconics.utils.IconicsMenuInflaterUtil
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.chart.adapter.ChartFragmentPagerAdapter
import io.github.zwieback.familyfinance.business.chart.display.ChartDisplay
import io.github.zwieback.familyfinance.business.chart.fragment.ChartFragment
import io.github.zwieback.familyfinance.business.chart.listener.ChartDisplayListener
import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter
import io.github.zwieback.familyfinance.business.operation.listener.OperationFilterListener
import io.github.zwieback.familyfinance.core.activity.DataActivityWrapper

class ChartActivity<FILTER, DISPLAY> :
    DataActivityWrapper(),
    OperationFilterListener<FILTER>,
    ChartDisplayListener<DISPLAY>
        where FILTER : OperationFilter,
              DISPLAY : ChartDisplay<DISPLAY> {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: ChartFragmentPagerAdapter

    override val titleStringId: Int
        get() = R.string.chart_activity_title

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTabs()
    }

    override fun setupContentView() {
        setContentView(R.layout.activity_chart)
    }

    private fun setupTabs() {
        pagerAdapter = ChartFragmentPagerAdapter(supportFragmentManager, this)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = pagerAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                findFragmentByPosition(position)?.let { chartFragment ->
                    if (!chartFragment.isDataLoaded) {
                        chartFragment.refreshData()
                    }
                }
            }
        })
        tabLayout = findViewById(R.id.sliding_tabs)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        val menuIds = listOf(R.menu.menu_entity_filter, R.menu.menu_chart_display)
        menuIds.forEach { menuId ->
            IconicsMenuInflaterUtil.inflate(inflater, this, menuId, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                findFragment()?.showFilterDialog()
                true
            }
            R.id.action_display -> {
                findFragment()?.showDisplayDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(TAB_POSITION, tabLayout.selectedTabPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewPager.currentItem = savedInstanceState.getInt(TAB_POSITION)
    }

    // -----------------------------------------------------------------------------------------
    // Fragment methods
    // -----------------------------------------------------------------------------------------

    private fun findFragment(): ChartFragment<*, *, FILTER, DISPLAY>? {
        return findFragmentByPosition(tabLayout.selectedTabPosition)
    }

    @Suppress("UNCHECKED_CAST")
    private fun findFragmentByPosition(position: Int): ChartFragment<*, *, FILTER, DISPLAY>? {
        return pagerAdapter.findFragment(position) as? ChartFragment<*, *, FILTER, DISPLAY>
    }

    // -----------------------------------------------------------------------------------------
    // Filter methods
    // -----------------------------------------------------------------------------------------

    override fun onApplyFilter(filter: FILTER) {
        findFragment()?.onApplyFilter(filter)
    }

    // -----------------------------------------------------------------------------------------
    // Display methods
    // -----------------------------------------------------------------------------------------

    override fun onApplyDisplay(display: DISPLAY) {
        findFragment()?.onApplyDisplay(display)
    }

    companion object {
        private const val TAB_POSITION = "tabPosition"
    }
}
