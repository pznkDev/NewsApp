package slava.kpi.com.newsusa.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import slava.kpi.com.newsusa.Constants;
import slava.kpi.com.newsusa.R;
import slava.kpi.com.newsusa.activities.ArticleFullActivity;
import slava.kpi.com.newsusa.adapter.ShortArticleListAdapter;
import slava.kpi.com.newsusa.database.DBHelper;
import slava.kpi.com.newsusa.entities.ArticleShort;

public class FavouriteNewsFragment extends Fragment {

    private final int LAYOUT = R.layout.fragment_favourite_news;

    private View view;

    private RecyclerView rvShortArticle;
    private ShortArticleListAdapter shortArticleListAdapter;
    private SwipeRefreshLayout swipeRefresh;

    public static FavouriteNewsFragment getInstance() {
        Bundle args = new Bundle();
        FavouriteNewsFragment favouriteNewsFragment = new FavouriteNewsFragment();
        favouriteNewsFragment.setArguments(args);

        return favouriteNewsFragment;
    }

    DBHelper dbHelper;

    private List<ArticleShort> favoriteNews = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        dbHelper = new DBHelper(getContext());

        rvShortArticle = (RecyclerView) view.findViewById(R.id.rec_view_short_article_favorite_list);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvShortArticle.setLayoutManager(layoutManager);

        shortArticleListAdapter = new ShortArticleListAdapter(getContext(), favoriteNews);
        rvShortArticle.setAdapter(shortArticleListAdapter);

        shortArticleListAdapter.setArticleListener(new ShortArticleListAdapter.OnArticleClickListener() {
            @Override
            public void onClick(ArticleShort articleShort) {
                ArticleShort articleShortOpen = articleShort;
                Intent articleFullIntent = new Intent(getActivity(), ArticleFullActivity.class);
                articleFullIntent.putExtra(Constants.EXTRA_ARTICLE_SHORT, articleShortOpen);
                startActivity(articleFullIntent);
            }
        });

        //refresh recyclerView
        //clear all news in List, clear adapterList
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_favorite_news);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shortArticleListAdapter.notifyItemRangeRemoved(0, favoriteNews.size());
                favoriteNews.clear();

                loadFavoriteNews();

                swipeRefresh.setRefreshing(false);
            }
        });
        if (favoriteNews.size() == 0) loadFavoriteNews();

        return view;
    }

    private void loadFavoriteNews() {
        // load all favorite news from dataBase
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_ARTICLES, null);

        if (cursor.moveToFirst()) {
            int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int imgSmallIndex = cursor.getColumnIndex(DBHelper.KEY_IMG_SMALL_URL);
            int imgBigIndex = cursor.getColumnIndex(DBHelper.KEY_IMG_BIG_URL);
            int articleFullIndex = cursor.getColumnIndex(DBHelper.KEY_ARTICLE_FULL_URL);
            int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);

            do {
                favoriteNews.add(new ArticleShort(cursor.getString(titleIndex),
                        cursor.getString(imgSmallIndex),
                        cursor.getString(imgBigIndex),
                        cursor.getString(articleFullIndex),
                        cursor.getString(dateIndex)));
            } while (cursor.moveToNext());

        }
        cursor.close();
        dbHelper.close();

        rvShortArticle.getAdapter().notifyItemRangeInserted(0, favoriteNews.size());
    }
}
