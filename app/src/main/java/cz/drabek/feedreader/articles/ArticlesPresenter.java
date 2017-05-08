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
import cz.drabek.feedreader.service.DownloadBroadCastReceiver;
import cz.drabek.feedreader.util.ClientToServiceBinder;

import static cz.drabek.feedreader.util.Preconditions.checkNotNull;

public class ArticlesPresenter implements
        ArticlesContract.Presenter, ArticlesRepository.LoadArticlesCallback,
        ArticlesRepository.GetArticleCallback,
        LoaderManager.LoaderCallbacks<Cursor> {

    private final static String TAG = "HONZA-ArticlesPresenter";
    public final static int ARTICLES_LOADER = 1;
    private static final long DOWNLOAD_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES / (15 * 6);

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
        mServiceBinder = ClientToServiceBinder.getInstance(this);
        initiateDownloadService();
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

//        mArticlesView.setLoadingIndicator(true);
//        mArticlesRepository.getArticles(this);
    }

    public Context getContext() {
        return mContext;
    }

    private void initiateDownloadService() {
        // initiate repeating download service
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent launchIntent = new Intent(mContext, DownloadBroadCastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), DOWNLOAD_INTERVAL, pi);

        // initiate manual download service
        mServiceBinder.doBindService();
    }

    // TODO Why do I need to restart loader?
    /**
     * Articles saved to local storage.
     *
     * @param articles  Saved articles
     */
    @Override
    public void onArticlesLoaded(List<Article> articles) {
        // we don't care about the result since the CursorLoader will load the data for us
        if (mLoaderManager.getLoader(ARTICLES_LOADER) == null)
            mLoaderManager.initLoader(ARTICLES_LOADER, null, this);
        else
            mLoaderManager.restartLoader(ARTICLES_LOADER, null, this);
    }

    @Override
    public void onArticleLoaded(Article article) { }

    @Override
    public void onDataNotAvailable() {
        Log.d(TAG, "onDataNotAvailable: ");
    }

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

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { onDataReset(); }

    private void onDataLoaded(Cursor data) {
        mArticlesView.setLoadingIndicator(false);
        mArticlesView.showArticles(data);
    }

    private void onDataEmpty() {
        mArticlesView.setLoadingIndicator(false);
        mArticlesView.showNoArticles();
    }

    private void onDataReset() { mArticlesView.showArticles(null); }

    @Override
    public void openArticleDetails(@NonNull Article requestedArticle) {
        checkNotNull(requestedArticle, "requestedArticle cannot be null!");
        mArticlesView.showArticleDetailsUi(requestedArticle);
    }

    public void onServiceActive(boolean active) {
        onArticlesLoaded(null);
        mArticlesView.setLoadingIndicator(active);
    }

}
