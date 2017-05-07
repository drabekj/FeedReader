package cz.drabek.feedreader.feeds;

import android.database.Cursor;

import cz.drabek.feedreader.BasePresenter;
import cz.drabek.feedreader.BaseView;
import cz.drabek.feedreader.data.Feed;

public interface FeedsContract {

    interface View extends BaseView<Presenter> {

        void showFeeds(Cursor feeds);

        void showNoFeeds();

    }

    interface Presenter extends BasePresenter {

        void saveFeed(String name, String url);

    }

    interface DialogView extends BaseView<Presenter> {

    }

}
