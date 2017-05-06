package cz.drabek.feedreader.data;

// TODO
public class Article {

    private int mId;
    private String mTitle;
    private String mUrl;
    private String mAuthor;
    private String mContent;

    public Article(int id, String title, String url, String author, String content) {
        mId = id;
        mTitle = title;
        mUrl = url;
        mAuthor = author;
        mContent = content;
    }

    @Override
    public String toString() {
        return "Article{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mContent='" + mContent + '\'' +
                '}';
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }
}
