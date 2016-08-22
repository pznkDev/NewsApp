package slava.kpi.com.newsusa.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import slava.kpi.com.newsusa.Constants;
import slava.kpi.com.newsusa.R;
import slava.kpi.com.newsusa.activities.ArticleFullActivity;
import slava.kpi.com.newsusa.adapter.ShortArticleListAdapter;
import slava.kpi.com.newsusa.entities.ArticleShort;
import slava.kpi.com.newsusa.listeners.EndlessRecyclerOnScrollListener;

public class AllNewsFragment extends Fragment {

    public static final String TAG = "AllNewsFragment";
    private final String LOG_TAG = "myTag";

    private final int LAYOUT = R.layout.fragment_all_news;

    private View view;

    private Document doc;

    private boolean flagSuccess = false;

    private RecyclerView rvShortArticle;
    private ShortArticleListAdapter shortArticleListAdapter;
    private EndlessRecyclerOnScrollListener recyclerOnScrollListener;
    private SwipeRefreshLayout swipeRefresh;

    private AVLoadingIndicatorView loadingAnimation;

    private FloatingActionButton fab;

    public static AllNewsFragment getInstance() {
        Bundle args = new Bundle();
        AllNewsFragment allNewsFragment = new AllNewsFragment();
        allNewsFragment.setArguments(args);

        return allNewsFragment;
    }

    private List<ArticleShort> allNews = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        rvShortArticle = (RecyclerView) view.findViewById(R.id.rec_view_short_article_list);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvShortArticle.setLayoutManager(layoutManager);
        // load news on next page when approaching to the end of list
        recyclerOnScrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                getNewsOnPage(current_page);
            }
        };
        rvShortArticle.setOnScrollListener(recyclerOnScrollListener);
        shortArticleListAdapter = new ShortArticleListAdapter(getContext(), getAllNews());
        rvShortArticle.setAdapter(shortArticleListAdapter);

        // set Listener for rec view adapter. Tap on short article -> open new Activity fro detailed info
        shortArticleListAdapter.setArticleListener(new ShortArticleListAdapter.OnArticleClickListener() {
            @Override
            public void onClick(String articleFullURL, String title) {
                String finalArticleFullURL = Constants.URL_ARTICLE_SHORT + articleFullURL;
                Intent articleFullIntent = new Intent(getActivity(), ArticleFullActivity.class);
                articleFullIntent.putExtra(Constants.EXTRA_TITLE, title);
                articleFullIntent.putExtra(Constants.EXTRA_ARTICLE_FULL_URL, finalArticleFullURL);
                startActivity(articleFullIntent);
            }
        });

        //refresh recyclerView
        //clear all news in List, clear adapterList
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_all_news);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shortArticleListAdapter.notifyItemRangeRemoved(0, allNews.size());
                allNews.clear();
                recyclerOnScrollListener.refresh();
                getNewsOnPage(1);
                swipeRefresh.setRefreshing(false);
            }
        });

        fab = (FloatingActionButton) view.findViewById(R.id.fab_all_news_up);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutManager.scrollToPositionWithOffset(0, 0);
            }
        });

        loadingAnimation = (AVLoadingIndicatorView) view.findViewById(R.id.avi_all_news);

        // if launch first time -> load first 30 news
        if (allNews.size() == 0) getNewsOnPage(1);

        return view;
    }

    private void getNewsOnPage(int page) {
        String fullURL = Constants.URL_NEWS;
        if (page > 1) fullURL = fullURL + "?page=" + page;
        else {
            loadingAnimation.setVisibility(View.VISIBLE);
            loadingAnimation.show();
        }
        new LoadNews().execute(fullURL);
    }

    private List<ArticleShort> getAllNews() {
        return allNews;
    }

    private class LoadNews extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                doc = Jsoup.connect(strings[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (doc != null) {
                // split into articles
                Elements elements = doc.select("article.story_list.span3.col");
                // check if null, something went wrong
                if (elements.size() > 0) {
                    for (Element part : elements) {

                        // parse each article
                        Element image = part.select("img").first();
                        String imgSmallURL = "";
                        String imgBigURL = "";
                        if (image != null) {
                            String imgCode = image.attr("data-srcset");
                            int commaIndex = imgCode.indexOf(",");
                            imgSmallURL = imgCode.substring(0, commaIndex);
                            imgBigURL = imgCode.substring(commaIndex+2);
                        }

                        // create new short article and add it to list
                        allNews.add(new ArticleShort(part.select("h3").first().text(),
                                imgSmallURL,
                                imgBigURL,
                                part.select("a").first().attr("href"),
                                part.select("p.date").first().text()));
                    }
                }

                flagSuccess = true;
            } else flagSuccess = false;

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //if success then show add news in adapter
            if (flagSuccess)
                rvShortArticle.getAdapter().notifyItemRangeInserted(allNews.size() - 30, allNews.size());
            else
                Toast.makeText(getContext(), "Oops, something went wrong" + allNews.size(), Toast.LENGTH_SHORT).show();
            loadingAnimation.hide();

        }
    }
}
