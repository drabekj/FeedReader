package cz.drabek.feedreader.feeds;

import android.database.Cursor;

import cz.drabek.feedreader.BasePresenter;
import cz.drabek.feedreader.BaseView;

public interface FeedsContract {

    interface View extends BaseView<Presenter> {

        void showFeeds(Cursor feeds);

        void showNoFeeds();

    }

    interface Presenter extends BasePresenter {

    }

}
