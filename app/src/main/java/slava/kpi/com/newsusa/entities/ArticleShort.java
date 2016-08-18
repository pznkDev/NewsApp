package slava.kpi.com.newsusa.entities;

public class ArticleShort {

    private String title, imgURL, fullArticleURL, date;

    public ArticleShort() {
    }

    public ArticleShort(String title, String imgURL, String fullArticleURL, String date) {

        this.title = title;
        this.imgURL = imgURL;
        this.fullArticleURL = fullArticleURL;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
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
