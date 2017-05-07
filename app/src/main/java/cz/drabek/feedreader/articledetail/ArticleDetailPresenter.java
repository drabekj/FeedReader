package cz.drabek.feedreader.articledetail;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.data.source.ArticlesContentProvider;
import cz.drabek.feedreader.data.source.ArticlesDataSource;
import cz.drabek.feedreader.data.source.ArticlesRepository;

public class ArticleDetailPresenter implements ArticleDetailContract.Presenter,
        ArticlesDataSource.GetArticleCallback, LoaderManager.LoaderCallbacks<Cursor> {

    public final static int ARTICLE_LOADER = 2;

    private Context mContext;
    private int mArticleId;
    private LoaderManager mLoaderManager;
    private ArticlesRepository mArticlesRepository;
    private ArticleDetailContract.View mView;

    public ArticleDetailPresenter(@NonNull Context context,
                                  @NonNull int articleId,
                                  @NonNull LoaderManager loaderManager,
                                  @NonNull ArticlesRepository articlesRepository,
                                  @NonNull ArticleDetailContract.View view) {
        mContext = context;
        mArticleId = articleId;
        mLoaderManager = loaderManager;
        mArticlesRepository = articlesRepository;
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadArticle();
    }

    private void loadArticle() {
        Log.d("HONZA", "loadArticle: ");
        mArticlesRepository.getArticle(mArticleId, this);
    }

    // TODO
    @Override
    public void onArticleLoaded(Article article) {
        Log.d("HONZA", "onArticleLoaded: ");
        // the data is refreshed locally now but
        // we don't need this result since the CursorLoader will load it for us
        mLoaderManager.initLoader(ARTICLE_LOADER, null, this);
    }

    // TODO
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("HONZA", "onCreateLoader: ");
        switch (id) {
            case ARTICLE_LOADER:
                return new CursorLoader(
                        mContext,
                        Uri.withAppendedPath(ArticlesContentProvider.CONTENT_ARTICLES_URI, String.valueOf(mArticleId)),
                        null, null, null, null);
            default:
                return null;
        }
    }

    // TODO
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("HONZA", "onLoadFinished: ");
        if (data != null) {
            if (data.moveToLast()) {
                onDataLoaded(data);
            } else {
                onDataEmpty();
            }
        } else {
            onDataNotAvailable();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

    private void onDataLoaded(Cursor data) {
        Log.d("HONZA", "onDataLoaded: ");
        Article article = Article.from(data);
        mView.showArticle(article);
    }

    // TODO
    private void onDataEmpty() { }

    // TODO
    @Override
    public void onDataNotAvailable() { }

}
