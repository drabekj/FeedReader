package cz.drabek.feedreader.feeds;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import cz.drabek.feedreader.R;
import cz.drabek.feedreader.data.Feed;

public class FeedFragment extends Fragment implements FeedsContract.View {

    private FeedsContract.Presenter mPresenter;
    private FeedsCursorAdapter mListAdapter;
    private FeedItemListener mItemListener = new FeedItemListener() {
        @Override
        public void onFeedClickListener(Feed clickedFeed) {
            NewFeedDialogFragment mDialog = NewFeedDialogFragment.newInstance(clickedFeed.getId());
            mDialog.setPresenter(mPresenter);
            mDialog.show(getFragmentManager(), "dialog");
        }
    };
    private ListView mListView;
    private LinearLayout mFeedsView;
    private LinearLayout mNoFeedsView;

    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment newInstance() { return new FeedFragment(); }

    @Override
    public void setPresenter(FeedsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.feed_frag, container, false);

        // Set up feeds view
        mListAdapter = new FeedsCursorAdapter(getActivity(), mItemListener);
        mListView = (ListView) root.findViewById(R.id.feeds_list);
        mListView.setAdapter(mListAdapter);
        mFeedsView = (LinearLayout) root.findViewById(R.id.feedsLL);

        // Set up no feeds view
        mNoFeedsView = (LinearLayout) root.findViewById(R.id.noFeeds);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.start();
    }

    public interface FeedItemListener {
        void onFeedClickListener(Feed clickedFeed);
    }

    @Override
    public void showFeeds(Cursor feeds) {
        // swap loader cursor
        mListAdapter.swapCursor(feeds);

        mFeedsView.setVisibility(View.VISIBLE);
        mNoFeedsView.setVisibility(View.GONE);
    }

    @Override
    public void showNoFeeds() {
        mFeedsView.setVisibility(View.GONE);
        mNoFeedsView.setVisibility(View.VISIBLE);
    }
}
