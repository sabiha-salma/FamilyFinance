package io.github.zwieback.familyfinance.business.chart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.business.chart.fragment.BarChartFragment;
import io.github.zwieback.familyfinance.business.chart.fragment.ChartFragment;
import io.github.zwieback.familyfinance.business.chart.fragment.PieChartFragment;

public class ChartFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private static final int PAGE_COUNT = 2;
    private static final int BAR_CHART = 0;
    private static final int PIE_CHART = 1;

    private SparseArray<ChartFragment> registeredFragments;
    private String tabTitles[];

    public ChartFragmentPagerAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm);
        registeredFragments = new SparseArray<>();
        initTabTitles(context);
    }

    private void initTabTitles(@NonNull Context context) {
        tabTitles = new String[PAGE_COUNT];
        tabTitles[BAR_CHART] = context.getString(R.string.bar_chart_title);
        tabTitles[PIE_CHART] = context.getString(R.string.pie_chart_title);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case BAR_CHART:
                return new BarChartFragment();
            case PIE_CHART:
                return new PieChartFragment();
        }
        throw new UnsupportedOperationException("Tab #" + position + " is not supported");
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ChartFragment fragment = (ChartFragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public ChartFragment findFragment(int position) {
        return registeredFragments.get(position);
    }
}
