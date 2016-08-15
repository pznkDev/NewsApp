package slava.kpi.com.newsusa.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import slava.kpi.com.newsusa.fragments.AllNewsFragment;

public class TabsPagerFragmentAdapter extends FragmentPagerAdapter{

    private String[] tabs;

    public TabsPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
        tabs = new String[] {"All", "Other", "Favourite"};
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return AllNewsFragment.getInstance();

            case 1:
                return AllNewsFragment.getInstance();

            case 2:
                return AllNewsFragment.getInstance();
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
