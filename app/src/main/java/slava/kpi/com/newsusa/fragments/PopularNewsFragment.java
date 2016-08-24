package slava.kpi.com.newsusa.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class PopularNewsFragment extends Fragment {

    private final int LAYOUT = R.layout.fragment_popular_news;

    private View view;

    private Document doc;

    private boolean flagSuccess = false;

    private RecyclerView rvShortArticle;
    private ShortArticleListAdapter shortArticleListAdapter;
    private SwipeRefreshLayout swipeRefresh;

    private AVLoadingIndicatorView loadingAnimation;

    public static PopularNewsFragment getInstance() {
        Bundle args = new Bundle();
        PopularNewsFragment popularNewsFragment = new PopularNewsFragment();
        popularNewsFragment.setArguments(args);

        return popularNewsFragment;
    }

    private List<ArticleShort> popularNews = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        rvShortArticle = (RecyclerView) view.findViewById(R.id.rec_view_short_article_popular_list);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvShortArticle.setLayoutManager(layoutManager);

        shortArticleListAdapter = new ShortArticleListAdapter(getContext(), popularNews);
        rvShortArticle.setAdapter(shortArticleListAdapter);

        // set Listener for rec view adapter. Tap on short article -> open new Activity for detailed info
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
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_popular_news);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shortArticleListAdapter.notifyItemRangeRemoved(0, popularNews.size());
                popularNews.clear();
                loadPopularNews();
                swipeRefresh.setRefreshing(false);
            }
        });

        loadingAnimation = (AVLoadingIndicatorView) view.findViewById(R.id.avi_popular_news);

        // if launch first time -> load articles with images
        if (popularNews.size() == 0) loadPopularNews();

        return view;
    }

    private void loadPopularNews() {
        loadingAnimation.setVisibility(View.VISIBLE);
        loadingAnimation.show();
        new LoadPopularNews().execute(Constants.URL_NEWS);
    }

    private class LoadPopularNews extends AsyncTask<String, String, String> {

        // load all news on first page only with images

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

                        // if article contains image then add this article to the popularList
                        if (image != null) {
                            String imgCode = image.attr("data-srcset");
                            int commaIndex = imgCode.indexOf(",");
                            imgSmallURL = imgCode.substring(0, commaIndex);
                            imgBigURL = imgCode.substring(commaIndex + 2);

                            // create new short article and add it to list
                            popularNews.add(new ArticleShort(part.select("h3").first().text(),
                                    imgSmallURL,
                                    imgBigURL,
                                    Constants.URL_ARTICLE_SHORT + part.select("a").first().attr("href"),
                                    part.select("p.date").first().text()));
                        }

                    }
                }

                flagSuccess = true;
            } else flagSuccess = false;

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            loadingAnimation.hide();
            //if success then show add news in adapter
            if (flagSuccess)
                rvShortArticle.getAdapter().notifyItemRangeInserted(0, popularNews.size());
            else
                Toast.makeText(getContext(), "Oops, something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

}
