package cz.drabek.feedreader.data.source.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.util.Log;

import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.data.source.ArticleValues;
import cz.drabek.feedreader.data.source.ArticlesContentProvider;
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
    public void getArticles(@NonNull LoadArticlesCallback callback) {

    }

    @Override
    public void getArticle(@NonNull String articleId, @NonNull GetArticleCallback callback) {

    }

    @Override
    public void saveArticle(@NonNull Article article) {
        checkNotNull(article);

        ContentValues values = ArticleValues.from(article);
        mContentResolver.insert(ArticlesContentProvider.CONTENT_ARTICLES_URI,values);
    }
}
