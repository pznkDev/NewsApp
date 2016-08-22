package slava.kpi.com.newsusa.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.IOException;

import slava.kpi.com.newsusa.Constants;
import slava.kpi.com.newsusa.R;
import slava.kpi.com.newsusa.other.HtmlHttpImageGetter;

public class ArticleFullActivity extends AppCompatActivity {

    private String title, articleFullURL;
    private Toolbar toolbar;
    private Document doc;
    private boolean flagSuccess = false;

    private TextView tvTitle;
    private HtmlTextView tvText;
    private AVLoadingIndicatorView loadingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppDefault);
        setContentView(R.layout.activity_article_full);

        initToolbar();

        Intent argsIntent = getIntent();
        if (argsIntent != null) {
            title = argsIntent.getStringExtra(Constants.EXTRA_TITLE);
            articleFullURL = argsIntent.getStringExtra(Constants.EXTRA_ARTICLE_FULL_URL);
        }

        tvTitle = (TextView) findViewById(R.id.tv_article_full_title);
        tvTitle.setText(title);
        tvText = (HtmlTextView) findViewById(R.id.tv_article_full_text);

        loadingAnimation = (AVLoadingIndicatorView) findViewById(R.id.avi_full_article);

        new FullArticleParser().execute(articleFullURL);

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_article_full);
        toolbar.setTitle(R.string.toolbar_article_full_title);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.toolbar_article_full_menu);
    }

    private class FullArticleParser extends AsyncTask<String, String, String> {

        StringBuilder fullText = new StringBuilder();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingAnimation.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                doc = Jsoup.connect(strings[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (doc != null) {

                Element element = doc.select("div.story-copy.clearfix").first();

                //split into strings
                Elements parts = element.select("p");
                // parse ech of strings
                for (Element part : parts) {
                    fullText.append(part);
                }

                flagSuccess = true;
            } else flagSuccess = false;

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loadingAnimation.hide();
            if (flagSuccess) {
                Log.d("myTag", fullText.toString());
                tvText.setHtml(fullText.toString(), new HtmlHttpImageGetter(tvText));
                tvTitle.setVisibility(View.VISIBLE);
            } else Toast.makeText(getApplicationContext(), "Oops, something went wrong", Toast.LENGTH_SHORT).show();

        }
    }

}
