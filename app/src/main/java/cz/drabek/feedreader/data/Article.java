package cz.drabek.feedreader.data;

import android.database.Cursor;

import cz.drabek.feedreader.data.source.local.DbPersistenceContract;

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

    /**
     * Use this constructor to return a Task from a Cursor
     *
     * @return
     */
    public static Article from(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(
                DbPersistenceContract.ArticleEntry._ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(
                DbPersistenceContract.ArticleEntry.COLUMN_NAME_TITLE));
        String url = cursor.getString(cursor.getColumnIndexOrThrow(
                DbPersistenceContract.ArticleEntry.COLUMN_NAME_URL));
        String author = cursor.getString(cursor.getColumnIndexOrThrow(
                DbPersistenceContract.ArticleEntry.COLUMN_NAME_AUTHOR));
        String content = cursor.getString(cursor.getColumnIndexOrThrow(
                DbPersistenceContract.ArticleEntry.COLUMN_NAME_CONTENT));

        return new Article(id, title, url, author, content);
    }
}
