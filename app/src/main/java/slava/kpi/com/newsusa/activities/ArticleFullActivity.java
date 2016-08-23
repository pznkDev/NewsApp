package slava.kpi.com.newsusa.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.IOException;

import slava.kpi.com.newsusa.Constants;
import slava.kpi.com.newsusa.R;
import slava.kpi.com.newsusa.database.DBHelper;
import slava.kpi.com.newsusa.entities.ArticleShort;
import slava.kpi.com.newsusa.other.HtmlHttpImageGetter;

public class ArticleFullActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Document doc;
    private boolean flagSuccess = false;

    private TextView tvTitle,tvDate;
    private ImageView imgBig;
    private HtmlTextView tvText;
    private AVLoadingIndicatorView loadingAnimation;

    private DBHelper dbHelper;

    boolean isFavoriteArticle;
    long currentArticleDBId;

    private ArticleShort currentArticle;

    private RelativeLayout layoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppDefault);
        setContentView(R.layout.activity_article_full);

        initToolbar();

        Intent argsIntent = getIntent();
        if (argsIntent != null) {
            currentArticle = argsIntent.getParcelableExtra(Constants.EXTRA_ARTICLE_SHORT);
        }

        layoutMain = (RelativeLayout) findViewById(R.id.layout_activity_article_full);
        tvTitle = (TextView) findViewById(R.id.tv_article_full_title);
        tvTitle.setText(currentArticle.getTitle());
        tvDate = (TextView) findViewById(R.id.tv_article_full_date);
        tvDate.setText(currentArticle.getDate());
        tvText = (HtmlTextView) findViewById(R.id.tv_article_full_text);
        imgBig = (ImageView) findViewById(R.id.img_view_article_full_img_big);

        loadingAnimation = (AVLoadingIndicatorView) findViewById(R.id.avi_full_article);

        new FullArticleParser().execute(currentArticle.getArticleFullURL());

        dbHelper = new DBHelper(this);

        checkIfFavorite();
    }

    private void checkIfFavorite() {
        // check if this article is in favorite list (stored in database)
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_ARTICLES, null);

        if (cursor.moveToFirst()) {

            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int urlIndex = cursor.getColumnIndex(DBHelper.KEY_ARTICLE_FULL_URL);

            do {
                if (cursor.getString(urlIndex).equals(currentArticle.getArticleFullURL())) {
                    // menu item set image
                    addToFavorite(cursor.getInt(idIndex));
                    break;
                }

            } while (cursor.moveToNext());

        }
        cursor.close();
        dbHelper.close();

        if (!isFavoriteArticle) {
            deleteFromFavorite();
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_article_full);
        toolbar.setTitle(R.string.toolbar_article_full_title);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toolbar_article_full_item_favorite:

                        SQLiteDatabase database = dbHelper.getWritableDatabase();

                        if (isFavoriteArticle) {
                            //it is favorite now, so delete this article from favorites

                            //delete article from database
                            if (currentArticleDBId != -1) {
                                int DelCount = database.delete(DBHelper.TABLE_ARTICLES, DBHelper.KEY_ID + "=" + currentArticleDBId, null);

                                if (DelCount > 0) {
                                    deleteFromFavorite();
                                    Snackbar.make(layoutMain, "Deleted from favorites", Snackbar.LENGTH_SHORT).show();
                                }
                            }

                        } else {
                            //it is not favorite now, so add it to favorites

                            // save article(title, url) in database
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(DBHelper.KEY_TITLE, currentArticle.getTitle());
                            contentValues.put(DBHelper.KEY_IMG_SMALL_URL, currentArticle.getImgSmallURL());
                            contentValues.put(DBHelper.KEY_IMG_BIG_URL, currentArticle.getImgBigURL());
                            contentValues.put(DBHelper.KEY_ARTICLE_FULL_URL, currentArticle.getArticleFullURL());
                            contentValues.put(DBHelper.KEY_DATE, currentArticle.getDate());

                            currentArticleDBId = database.insert(DBHelper.TABLE_ARTICLES, null, contentValues);
                            dbHelper.close();

                            addToFavorite(currentArticleDBId);

                            Snackbar.make(layoutMain, "Added to favorites", Snackbar.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.toolbar_article_full_item_share:
                        // share function
                        break;
                }
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.toolbar_article_full_menu);
    }

    private class FullArticleParser extends AsyncTask<String, String, String> {

        String fullText;
        String imgURL = "";

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

                // get main article's image url
                Element imgElement = doc.select("div.article-image").first();

                if (imgElement != null) {
                    Element image = imgElement.select("img").first();
                    imgURL = image.attr("src");
                }

                // get main article's text
                Element textElement = doc.select("div.story-copy.clearfix").first();

                String text = textElement.toString();

                // delete all \n for regular expressions processing
                text = text.replaceAll("\n", "");
                // remove all unnecessary parts
                fullText = text.replaceAll("(<figure).*(</figure>)", "");

                flagSuccess = true;
            } else flagSuccess = false;

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loadingAnimation.hide();
            if (flagSuccess) {

                if (!imgURL.equals(""))
                    Picasso.with(getApplicationContext())
                            .load(imgURL)
                            .into(imgBig);

                tvText.setHtml(fullText.toString(), new HtmlHttpImageGetter(tvText));
                tvTitle.setVisibility(View.VISIBLE);
                tvDate.setVisibility(View.VISIBLE);
            } else
                Toast.makeText(getApplicationContext(), "Oops, something went wrong", Toast.LENGTH_SHORT).show();

        }
    }

    private void addToFavorite(long currentId) {
        toolbar.getMenu().getItem(1).setIcon(R.drawable.ic_star_added);
        isFavoriteArticle = true;
        currentArticleDBId = currentId;
    }

    private void deleteFromFavorite() {
        toolbar.getMenu().getItem(1).setIcon(R.drawable.ic_star);
        isFavoriteArticle = false;
        currentArticleDBId = -1;
    }

}
