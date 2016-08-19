package slava.kpi.com.newsusa.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import slava.kpi.com.newsusa.Constants;
import slava.kpi.com.newsusa.R;

public class ArticleFullActivity extends AppCompatActivity {

    private String title, articleFullURL;
    private Toolbar toolbar;
    private Document doc;
    private boolean flagSuccess = false;

    private TextView tvTitle, tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_full);

        initToolbar();

        Intent argsIntent = getIntent();
        if (argsIntent != null) {
            title = argsIntent.getStringExtra(Constants.EXTRA_TITLE);
            articleFullURL = argsIntent.getStringExtra(Constants.EXTRA_ARTICLE_FULL_URL);
        }

        tvTitle = (TextView) findViewById(R.id.tv_article_full_title);
        tvTitle.setText(title);
        tvText = (TextView) findViewById(R.id.tv_article_full_text);

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
                    fullText.append(part.text() + "\n");
                }

                flagSuccess = true;
            } else flagSuccess = false;

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (flagSuccess) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                tvText.setText(fullText);
                tvTitle.setVisibility(View.VISIBLE);
            } else Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

        }
    }

}
