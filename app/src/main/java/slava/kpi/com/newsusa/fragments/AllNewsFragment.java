package slava.kpi.com.newsusa.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import slava.kpi.com.newsusa.entities.ArticleShort;

public class AllNewsFragment extends Fragment {

    public static final String TAG = "AllNewsFragment";

    private final String LOG_TAG = "myTag";

    private final String URL = "http://www.sandiegouniontribune.com/news/national-news/california/";

    private final int LAYOUT = R.layout.fragment_all_news;

    private View view;

    private ProgressDialog progressDialog;

    private Document doc;

    private boolean flagSuccess = false;

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

        if (allNews.size() == 0) new LoadNews().execute(URL);

        return view;
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

            if (flagSuccess)
                Toast.makeText(getContext(), "Success" + allNews.size(), Toast.LENGTH_SHORT).show();
            else Toast.makeText(getContext(), "Error" + allNews.size(), Toast.LENGTH_SHORT).show();

            progressDialog.cancel();
        }
    }
}
