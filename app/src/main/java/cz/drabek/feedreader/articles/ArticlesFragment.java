package cz.drabek.feedreader.articles;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cz.drabek.feedreader.R;

public class ArticlesFragment extends Fragment
        implements ArticlesContract.View, LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = "ArticleListFragment";
    private final int FEED_ENTRY_LOADER = 1;

    private ArticlesContract.Presenter mPresenter;


    public ArticlesFragment() {
        // Required empty public constructor
    }

    public static ArticlesFragment newInstance() {
        ArticlesFragment fragment = new ArticlesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.articles_frag, container, false);

        return root;
    }

    @Override
    public void setPresenter(@NonNull ArticlesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    // TODO
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    // TODO
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    // TODO
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
