package cz.drabek.feedreader.feeds;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.drabek.feedreader.R;
import cz.drabek.feedreader.data.Feed;

public class FeedsCursorAdapter extends CursorAdapter {

    FeedFragment.FeedItemListener mItemListener;

    private class ViewHolder {
        TextView mName;
        TextView mUrl;

        public ViewHolder(View view) {
            mName = (TextView) view.findViewById(R.id.feed_item_name);
            mUrl = (TextView) view.findViewById(R.id.feed_item_url);
        }
    }

    public FeedsCursorAdapter(Context context, FeedFragment.FeedItemListener feedItemListener) {
        super(context, null, 0);
        mItemListener = feedItemListener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        final Feed feed = Feed.from(cursor);
        viewHolder.mName.setText(feed.getName());
        viewHolder.mUrl.setText(feed.getUrl());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onFeedClickListener(feed);
            }
        });
    }
}
