package cz.drabek.feedreader.data.source.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

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
    public void getArticles(@NonNull LoadArticlesCallback callback) { }

    @Override
    public void getArticle(@NonNull int articleId, @NonNull GetArticleCallback callback) {
        Uri uri = Uri.withAppendedPath(ArticlesContentProvider.CONTENT_ARTICLES_URI, String.valueOf(articleId));

        Cursor cursor = mContentResolver.query(
                uri,
                null, null, null, null);

        cursor.moveToFirst();
        callback.onArticleLoaded(Article.from(cursor));
    }

    @Override
    public void saveArticle(@NonNull Article article) {
        checkNotNull(article);

        ContentValues values = Article.from(article);
        mContentResolver.insert(ArticlesContentProvider.CONTENT_ARTICLES_URI,values);
    }

    @Override
    public void saveFeed(@NonNull Feed feed) {
        checkNotNull(feed);

        ContentValues values = Feed.from(feed);
        mContentResolver.insert(ArticlesContentProvider.CONTENT_FEEDS_URI, values);
    }
}
