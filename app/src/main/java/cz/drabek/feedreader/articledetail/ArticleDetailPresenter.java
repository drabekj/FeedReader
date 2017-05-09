package cz.drabek.feedreader.articledetail;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.data.source.local.ArticlesContentProvider;
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

    /**
     * Load article which should be displayed in view.
     */
    private void loadArticle() {
        mArticlesRepository.getArticle(mArticleId, this);
    }

    /**
     * Data gets loaded by Loader, no need to use {@param article}
     *
     * @param article   loaded article (not needed)
     */
    @Override
    public void onArticleLoaded(Article article) {
        mLoaderManager.initLoader(ARTICLE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
        if (data != null) {
            if (data.moveToLast()) {
                onDataLoaded(data);
            } else {
                // data empty
            }
        } else {
            onDataNotAvailable();
        }
    }

    // TODO - low priority
    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

    private void onDataLoaded(Cursor data) {
        Article article = Article.from(data);
        mView.showArticle(article);
    }

    // TODO - low priority
    @Override
    public void onDataNotAvailable() { }

}
