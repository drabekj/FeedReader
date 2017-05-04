package cz.drabek.feedreader.data;

/**
 * Created by JD-mac on 04/05/2017.
 */

// TODO
public class Article {

    private String mTitle;
    private String mUrl;
    private String mAuthor;
    private String mContent;

    public Article(String title, String url, String author, String content) {
        mTitle = title;
        mUrl = url;
        mAuthor = author;
        mContent = content;
    }

    @Override
    public String toString() {
        return "Article{" +
                "mTitle='" + mTitle + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mContent='" + mContent + '\'' +
                '}';
    }
}
