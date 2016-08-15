package slava.kpi.com.newsusa.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import slava.kpi.com.newsusa.R;

public class FavouriteNewsFragment extends Fragment{

    public static final String TAG = "FavouriteNewsFragment";

    private final int LAYOUT = R.layout.fragment_favourite_news;

    private View view;

    public static FavouriteNewsFragment getInstance(){
        Bundle args = new Bundle();
        FavouriteNewsFragment favouriteNewsFragment = new FavouriteNewsFragment();
        favouriteNewsFragment.setArguments(args);

        return favouriteNewsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        return view;
    }
}
