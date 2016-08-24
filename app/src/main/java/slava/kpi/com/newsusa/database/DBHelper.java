package slava.kpi.com.newsusa.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // fields for database
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "newsDB";
    public static final String TABLE_ARTICLES = "articles";

    //fields for table "articles"
    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_IMG_SMALL_URL = "imgSmallURL";
    public static final String KEY_IMG_BIG_URL = "imgBigURL";
    public static final String KEY_ARTICLE_FULL_URL = "articleFullURL";
    public static final String KEY_DATE = "date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //creates articles db
        sqLiteDatabase.execSQL("create table " + TABLE_ARTICLES +
                "(" + KEY_ID + " integer primary key," +
                KEY_TITLE + " text," +
                KEY_IMG_SMALL_URL + " text," +
                KEY_IMG_BIG_URL + " text," +
                KEY_ARTICLE_FULL_URL + " text," +
                KEY_DATE + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_ARTICLES);
        onCreate(sqLiteDatabase);
    }
}
