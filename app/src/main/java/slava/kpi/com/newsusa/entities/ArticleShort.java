package slava.kpi.com.newsusa.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticleShort implements Parcelable {

    private String title, imgSmallURL, imgBigURL, articleFullURL, date;

    public ArticleShort() {
    }

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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgSmallURL() {
        return imgSmallURL;
    }

    public void setImgSmallURL(String imgSmallURL) {
        this.imgSmallURL = imgSmallURL;
    }

    public String getImgBigURL() {
        return imgBigURL;
    }

    public void setImgBigURL(String imgBigURL) {
        this.imgBigURL = imgBigURL;
    }

    public String getArticleFullURL() {
        return articleFullURL;
    }

    public void setArticleFullURL(String articleFullURL) {
        this.articleFullURL = articleFullURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
