package slava.kpi.com.newsusa.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticleShort implements Parcelable {

    private String title, imgSmallURL, imgBigURL, articleFullURL, date;

    public ArticleShort(String title, String imgSmallURL, String imgBigURL, String fullArticleURL, String date) {
        this.title = title;
        this.imgSmallURL = imgSmallURL;
        this.imgBigURL = imgBigURL;
        this.articleFullURL = fullArticleURL;
        this.date = date;
    }

    protected ArticleShort(Parcel in) {
        title = in.readString();
        imgSmallURL = in.readString();
        imgBigURL = in.readString();
        articleFullURL = in.readString();
        date = in.readString();
    }

    public static final Creator<ArticleShort> CREATOR = new Creator<ArticleShort>() {
        @Override
        public ArticleShort createFromParcel(Parcel in) {
            return new ArticleShort(in);
        }

        @Override
        public ArticleShort[] newArray(int size) {
            return new ArticleShort[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getImgSmallURL() {
        return imgSmallURL;
    }

    public String getImgBigURL() {
        return imgBigURL;
    }

    public String getArticleFullURL() {
        return articleFullURL;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(imgSmallURL);
        parcel.writeString(imgBigURL);
        parcel.writeString(articleFullURL);
        parcel.writeString(date);
    }
}
