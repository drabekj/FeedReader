package cz.drabek.feedreader.feeds;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cz.drabek.feedreader.R;

public class NewFeedDialogFragment extends DialogFragment implements FeedsContract.DialogView {

    private FeedsContract.Presenter mPresenter;

    public NewFeedDialogFragment() { }

    public static NewFeedDialogFragment newInstance() { return new NewFeedDialogFragment(); }

    @Override
    public void setPresenter(FeedsContract.Presenter presenter) { mPresenter = presenter; }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.new_feed_dialog, null);
        builder .setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = ((TextView) view.findViewById(R.id.dialog_new_feed_name))
                                .getText().toString();
                        String url  = ((TextView) view.findViewById(R.id.dialog_new_feed_url))
                                .getText().toString();

                        mPresenter.saveFeed(name, url);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NewFeedDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
