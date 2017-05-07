package cz.drabek.feedreader.feeds;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

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

        void loadFeed(int feedId);

    }

    interface DialogView extends BaseView<Presenter> {

        void prefillInputs(Feed feed);

    }

}
