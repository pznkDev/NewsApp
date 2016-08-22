package slava.kpi.com.newsusa.entities;

public class ArticleShort {

    private String title, imgSmallURL, imgBigURL, fullArticleURL, date;

    public ArticleShort() {
    }

    public ArticleShort(String title, String imgSmallURL, String imgBigURL, String fullArticleURL, String date) {
        this.title = title;
        this.imgSmallURL = imgSmallURL;
        this.imgBigURL = imgBigURL;
        this.fullArticleURL = fullArticleURL;
        this.date = date;
    }

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

    public String getFullArticleURL() {
        return fullArticleURL;
    }

    public void setFullArticleURL(String fullArticleURL) {
        this.fullArticleURL = fullArticleURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
