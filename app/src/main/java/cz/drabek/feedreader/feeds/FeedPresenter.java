package cz.drabek.feedreader.feeds;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.List;

import cz.drabek.feedreader.data.Feed;
import cz.drabek.feedreader.data.source.ArticlesDataSource;
import cz.drabek.feedreader.data.source.ArticlesRepository;
import cz.drabek.feedreader.data.source.local.ArticlesContentProvider;

import static cz.drabek.feedreader.util.Preconditions.checkNotNull;

public class FeedPresenter implements FeedsContract.Presenter,
        ArticlesDataSource.LoadFeedsCallback,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "FeedPresenter";
    private static final int FEEDS_LOADER = 2;

    private Context mContext;
    private LoaderManager mLoaderManager;
    private ArticlesRepository mRepository;
    private FeedsContract.View mView;

    public FeedPresenter(@NonNull Context context,
                         @NonNull LoaderManager loaderManager,
                         @NonNull ArticlesRepository repository,
                         @NonNull FeedsContract.View view,
                         @NonNull NewFeedDialogFragment dialogView) {
        mContext        = checkNotNull(context, "context cannot be null");
        mLoaderManager  = checkNotNull(loaderManager, "loaderManager cannot be null");
        mRepository     = checkNotNull(repository, "articlesRepository cannot be null");
        mView           = checkNotNull(view, "view cannot be null!");

        mView.setPresenter(this);
        dialogView.setPresenter(this);
    }

    @Override
    public void start() { loadFeeds(); }
    
    private void loadFeeds() { mRepository.getFeeds(this); }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case FEEDS_LOADER:
                return new CursorLoader(mContext, ArticlesContentProvider.CONTENT_FEEDS_URI,
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

    private void onDataLoaded(Cursor data) { mView.showFeeds(data); }

    private void onDataEmpty() { mView.showNoFeeds(); }

    private void onDataReset() { }

    @Override
    public void onFeedsLoaded(List<Feed> feeds) {
        // we don't care about the result since the CursorLoader will load the data for us
        if (mLoaderManager.getLoader(FEEDS_LOADER) == null)
            mLoaderManager.initLoader(FEEDS_LOADER, null, this);
        else
            mLoaderManager.restartLoader(FEEDS_LOADER, null, this);
    }

    @Override
    public void onDataNotAvailable() {
        Log.w(TAG, "onDataNotAvailable: error");
        mView.showNoFeeds();
    }

    @Override
    public void saveFeed(String name, String url) {
        mRepository.saveFeed(new Feed(name, url));
    }
}
