package cz.drabek.feedreader.feeds;

import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.util.Log;

import cz.drabek.feedreader.data.Feed;
import cz.drabek.feedreader.data.source.ArticlesRepository;

import static cz.drabek.feedreader.util.Preconditions.checkNotNull;

public class FeedPresenter implements FeedsContract.Presenter {

    private LoaderManager mLoaderManager;
    private ArticlesRepository mRepository;
    private FeedsContract.View mView;

    public FeedPresenter(@NonNull LoaderManager loaderManager,
                         @NonNull ArticlesRepository repository,
                         @NonNull FeedsContract.View view) {
        mLoaderManager  = checkNotNull(loaderManager, "loaderManager cannot be null");
        mRepository     = checkNotNull(repository, "articlesRepository cannot be null");
        mView           = checkNotNull(view, "view cannot be null!");

        mView.setPresenter(this);
    }

    @Override
    public void start() { loadFeeds(); }

    // TODO
    private void loadFeeds() {
        
    }

}
