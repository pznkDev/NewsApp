package slava.kpi.com.newsusa.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import slava.kpi.com.newsusa.Constants;
import slava.kpi.com.newsusa.R;

public class ArticleFullActivity extends AppCompatActivity {

    private String title, articleFullURL;
    private Toolbar toolbar;

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

}
