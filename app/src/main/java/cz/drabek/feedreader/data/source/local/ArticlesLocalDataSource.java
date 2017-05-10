package cz.drabek.feedreader.data.source.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.data.Feed;
import cz.drabek.feedreader.data.source.ArticlesDataSource;
import static cz.drabek.feedreader.util.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
public class ArticlesLocalDataSource implements ArticlesDataSource {

    private static final String TAG = "ArticlesLocalDataSource";

    private static ArticlesLocalDataSource INSTANCE = null;
    ContentResolver mContentResolver;

    // Prevent direct instantiation.
    private ArticlesLocalDataSource(@NonNull ContentResolver contentResolver) {
        checkNotNull(contentResolver);
        mContentResolver = contentResolver;
    }

    public static ArticlesLocalDataSource getInstance(@NonNull ContentResolver contentResolver) {
        if (INSTANCE == null) {
            INSTANCE = new ArticlesLocalDataSource(contentResolver);
        }
        return INSTANCE;
    }

    @Override
    public void getArticle(@NonNull int articleId, @NonNull GetArticleCallback callback) {
        Uri uri = Uri.withAppendedPath(ArticlesContentProvider.CONTENT_ARTICLES_URI, String.valueOf(articleId));

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(uri, null, null, null, null);
        } catch (Exception e) {
            callback.onDataNotAvailable();
            return;
        }
        if (cursor == null || !cursor.moveToLast())
            callback.onDataNotAvailable();
        callback.onArticleLoaded(Article.from(cursor));
    }

    @Override
    public void saveArticle(@NonNull Article article) {
        checkNotNull(article);

        ContentValues values = Article.from(article);
        mContentResolver.insert(ArticlesContentProvider.CONTENT_ARTICLES_URI,values);
    }

    @Override
    public void getFeed(@NonNull int feedId, @NonNull GetFeedCallback callback) {
        Uri uri = Uri.withAppendedPath(ArticlesContentProvider.CONTENT_FEEDS_URI, String.valueOf(feedId));

        Cursor cursor = mContentResolver.query(uri, null, null, null, null);

        Feed feed = (cursor.moveToFirst()) ? Feed.from(cursor) : new Feed();
        callback.onFeedLoaded(feed);
    }

    @Override
    public void saveFeed(@NonNull Feed feed) {
        checkNotNull(feed);

        ContentValues values = Feed.from(feed);
        mContentResolver.insert(ArticlesContentProvider.CONTENT_FEEDS_URI, values);
    }

    @Override
    public void deleteFeed(@NonNull int feedId) {
        checkNotNull(feedId);

        Uri uri = Uri.withAppendedPath(ArticlesContentProvider.CONTENT_FEEDS_URI, String.valueOf(feedId));
        mContentResolver.delete(
                uri,
                DbPersistenceContract.FeedEntry._ID + " = ?",
                new String[]{String.valueOf(feedId)});
    }

    @Override
    public void getArticles(@NonNull LoadArticlesCallback callback) { }

    @Override
    public void getFeeds(@NonNull LoadFeedsCallback callback) {
        List<Feed> feeds = new ArrayList<>();

        Cursor cursor = mContentResolver.query(ArticlesContentProvider.CONTENT_FEEDS_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()){
            do {
                feeds.add(Feed.from(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        callback.onFeedsLoaded(feeds);
    }

    @Override
    public void downloadArticles(@NonNull List<Feed> feeds, @NonNull DownloadArticlesCallback callback) { }
}
