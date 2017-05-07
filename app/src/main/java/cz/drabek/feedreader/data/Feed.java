package cz.drabek.feedreader.data;

import android.content.ContentValues;
import android.database.Cursor;

import cz.drabek.feedreader.data.source.local.DbPersistenceContract;

// TODO
public class Feed {

    int mId;
    String mName;
    String mUrl;

    public Feed(int id, String name, String url) {
        mId = id;
        mName = name;
        mUrl = url;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
    }

    /**
     * Use this constructor to return a Task from a Cursor
     *
     * @return
     */
    public static Feed from(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(
                DbPersistenceContract.FeedEntry._ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(
                DbPersistenceContract.FeedEntry.COLUMN_NAME_NAME));
        String url = cursor.getString(cursor.getColumnIndexOrThrow(
                DbPersistenceContract.FeedEntry.COLUMN_NAME_URL));

        return new Feed(id, name, url);
    }

    public static ContentValues from(Feed feed) {
        ContentValues values = new ContentValues();
        values.put(DbPersistenceContract.FeedEntry._ID, feed.getId());
        values.put(DbPersistenceContract.FeedEntry.COLUMN_NAME_NAME, feed.getName());
        values.put(DbPersistenceContract.FeedEntry.COLUMN_NAME_URL, feed.getUrl());

        return values;
    }
}
