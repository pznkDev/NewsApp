package slava.kpi.com.newsusa.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import slava.kpi.com.newsusa.R;
import slava.kpi.com.newsusa.adapter.ShortArticleListAdapter;
import slava.kpi.com.newsusa.entities.ArticleShort;
import slava.kpi.com.newsusa.listeners.EndlessRecyclerOnScrollListener;

public class AllNewsFragment extends Fragment {

    public static final String TAG = "AllNewsFragment";

    private final String LOG_TAG = "myTag";

    private final String URL = "http://www.sandiegouniontribune.com/news/national-news/california/";

    private final int LAYOUT = R.layout.fragment_all_news;

    private View view;

    private ProgressDialog progressDialog;

    private Document doc;

    private boolean flagSuccess = false;

    private RecyclerView rvShortArticle;
    ShortArticleListAdapter shortArticleListAdapter;
    private EndlessRecyclerOnScrollListener recyclerOnScrollListener;


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

        recyclerOnScrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                // TODO load next news
            }
        };
        rvShortArticle.setOnScrollListener(recyclerOnScrollListener);
        shortArticleListAdapter = new ShortArticleListAdapter(getContext(), getAllNews());
        rvShortArticle.setAdapter(shortArticleListAdapter);

        if (allNews.size() == 0) new LoadNews().execute(URL);

        return view;
    }

    private List<ArticleShort> getAllNews() {
        return allNews;
    }

    private class LoadNews extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Loading News");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

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
                        String imgURL = new String();
                        if (image != null) imgURL = image.attr("src");
                        else imgURL = "";
                        // create new short article and add it to list
                        allNews.add(new ArticleShort(part.select("h3").first().text(), imgURL,
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
            if (flagSuccess) {
                Toast.makeText(getContext(), "Success" + allNews.size(), Toast.LENGTH_SHORT).show();
                rvShortArticle.getAdapter().notifyItemRangeInserted(allNews.size(), allNews.size()+30);

            }
            else Toast.makeText(getContext(), "Error" + allNews.size(), Toast.LENGTH_SHORT).show();

            progressDialog.cancel();
        }
    }
}
