package cz.drabek.feedreader.articles;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.List;

import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.data.source.local.ArticlesContentProvider;
import cz.drabek.feedreader.data.source.ArticlesRepository;
import cz.drabek.feedreader.service.DownloadAlarmManager;
import cz.drabek.feedreader.service.DownloadBroadCastReceiver;
import cz.drabek.feedreader.util.ClientToServiceBinder;

import static cz.drabek.feedreader.util.Preconditions.checkNotNull;

public class ArticlesPresenter implements
        ArticlesContract.Presenter, ArticlesRepository.LoadArticlesCallback,
        LoaderManager.LoaderCallbacks<Cursor> {

    private final static String TAG = "HONZA-ArticlesPresenter";
    public final static int ARTICLES_LOADER = 1;

    private Context mContext;
    private LoaderManager mLoaderManager;
    private ArticlesRepository mArticlesRepository;
    private ArticlesContract.View mArticlesView;
    private ClientToServiceBinder mServiceBinder;

    public ArticlesPresenter(@NonNull Context context,
                             @NonNull LoaderManager loaderManager,
                             @NonNull ArticlesRepository articlesRepository,
                             @NonNull ArticlesContract.View articlesView) {
        mContext = checkNotNull(context);
        mLoaderManager = checkNotNull(loaderManager, "loaderManager cannot be null");
        mArticlesRepository = checkNotNull(articlesRepository, "articlesRepository cannot be null");
        mArticlesView = checkNotNull(articlesView, "articlesView cannot be null!");

        mArticlesView.setPresenter(this);
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void initiateDownloadService() {
        // initiate manual download service
        mServiceBinder = ClientToServiceBinder.getInstance(this);
        mServiceBinder.doBindService();

        // initiate repeating download service
        DownloadAlarmManager.setAlarm(mContext);
    }

    @Override
    public void start() {
        loadArticles();
    }

    /**
     * Starts service and refreshes articles.
     */
    @Override
    public void loadArticles() {
        // start service here => loads articles to DB
        mServiceBinder.startService();
    }

    // TODO Why do I need to restart loader?
    /**
     * Callback: Articles downloaded and saved to local storage.
     * Using loader, that makes {@param articles} useless
     *
     * @param articles  Saved articles (can be null)
     */
    @Override
    public void onArticlesLoaded(List<Article> articles) {
        // we don't care about the result since the CursorLoader will load the data for us
        if (mLoaderManager.getLoader(ARTICLES_LOADER) == null)
            mLoaderManager.initLoader(ARTICLES_LOADER, null, this);
        else
            mLoaderManager.restartLoader(ARTICLES_LOADER, null, this);
    }

    /**
     * Callback: Data not available.
     */
    @Override
    public void onDataNotAvailable() {
        Log.d(TAG, "onDataNotAvailable: ");
    }

    /**
     * Request to open {@param requestedArticle} in article detail view.
     *
     * @param requestedArticle  Article to be displayed in detail view
     */
    @Override
    public void openArticleDetails(@NonNull Article requestedArticle) {
        checkNotNull(requestedArticle, "requestedArticle cannot be null!");
        mArticlesView.showArticleDetailsUi(requestedArticle);
    }

    /**
     * Service state changed.
     * Loads new data if service finished work.
     * @param active
     */
    @Override
    public void onServiceActive(boolean active) {
        if (!active)
            onArticlesLoaded(null);
        mArticlesView.setLoadingIndicator(active);
    }

    // Loader method
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ARTICLES_LOADER:
                return new CursorLoader(mContext, ArticlesContentProvider.CONTENT_ARTICLES_URI,
                        null, null, null, null);
            default:
                break;
        }

        return null;
    }

    // Loader method
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
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

    // Loader method
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mArticlesView.showArticles(null);
    }

    // Data loaded by Loader and are ready to be refreshed (cursor swap)
    private void onDataLoaded(Cursor data) {
        mArticlesView.showArticles(data);
    }

    // Data loaded by Loader and are empty
    private void onDataEmpty() {
        mArticlesView.showNoArticles();
    }

}
