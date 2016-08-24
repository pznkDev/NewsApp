package slava.kpi.com.newsusa.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import slava.kpi.com.newsusa.R;
import slava.kpi.com.newsusa.fragments.AllNewsFragment;
import slava.kpi.com.newsusa.fragments.FavouriteNewsFragment;
import slava.kpi.com.newsusa.fragments.PopularNewsFragment;

public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {

    private String[] tabs;

    public TabsPagerFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);

        tabs = new String[]{context.getResources().getString(R.string.tab_popular),
                context.getResources().getString(R.string.tab_all),
                context.getResources().getString(R.string.tab_favourite)};
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return PopularNewsFragment.getInstance();
            case 1:
                return AllNewsFragment.getInstance();
            case 2:
                return FavouriteNewsFragment.getInstance();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
