package cz.drabek.feedreader.feeds;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cz.drabek.feedreader.R;
import cz.drabek.feedreader.data.Feed;

public class NewFeedDialogFragment extends DialogFragment implements FeedsContract.DialogView {

    private static final String ARG_FEED_ID = "feedId";

    private FeedsContract.Presenter mPresenter;
    private int mFeedId;
    private View     mDialog;
    private TextView mNameTV;
    private TextView mUrlTV;

    public NewFeedDialogFragment() { }

    public static NewFeedDialogFragment newInstance() { return new NewFeedDialogFragment(); }

    @Override
    public void setPresenter(FeedsContract.Presenter presenter) { mPresenter = presenter; }

    public static NewFeedDialogFragment newInstance(int feedId) {
        NewFeedDialogFragment fragment = new NewFeedDialogFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_FEED_ID, feedId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mFeedId = getArguments().getInt(ARG_FEED_ID);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        mDialog = inflater.inflate(R.layout.new_feed_dialog, null);
        mNameTV = (TextView) mDialog.findViewById(R.id.dialog_new_feed_name);
        mUrlTV  = (TextView) mDialog.findViewById(R.id.dialog_new_feed_url);
        buildDialog(builder);

        mPresenter.loadFeed(mFeedId);

        return builder.create();
    }

    private AlertDialog.Builder buildDialog(AlertDialog.Builder builder) {
        builder .setView(mDialog)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = mNameTV.getText().toString();
                        String url  = mUrlTV .getText().toString();

                        mPresenter.saveFeed(name, url);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NewFeedDialogFragment.this.getDialog().cancel();
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("HONZA", "onClick: Delete");
                    }
                });

        return builder;
    }

    @Override
    public void prefillInputs(Feed feed) {
        mNameTV.setText(feed.getName());
        mUrlTV .setText(feed.getUrl());
    }
}
