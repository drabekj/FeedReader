package cz.drabek.feedreader.feeds;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz.drabek.feedreader.R;

public class FeedFragment extends Fragment implements FeedsContract.View {

    private FeedsContract.Presenter mPresenter;

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

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.start();
    }
}
