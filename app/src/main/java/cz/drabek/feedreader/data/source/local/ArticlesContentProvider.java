package cz.drabek.feedreader.data.source.local;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class ArticlesContentProvider extends ContentProvider {

    private LocalDbHelper mDbHelper;
    public static final String AUTHORITY = "cz.drabek.feedreader";
    private static final String ARTICLE_PATH = "article";
    private static final String FEED_PATH = "feed";
    public static final Uri CONTENT_ARTICLES_URI = Uri.parse("content://" + AUTHORITY + "/" + ARTICLE_PATH);
    public static final Uri CONTENT_FEEDS_URI = Uri.parse("content://" + AUTHORITY + "/" + FEED_PATH);
    private static final int ARTICLE_LIST = 1;
    private static final int ARTICLE_ID = 2;
    private static final int FEED_LIST = 3;
    private static final int FEED_ID = 4;
    private static final UriMatcher sURIMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(AUTHORITY, DbPersistenceContract.ArticleEntry.TABLE_NAME, ARTICLE_LIST);
        matcher.addURI(AUTHORITY, DbPersistenceContract.ArticleEntry.TABLE_NAME + "/#", ARTICLE_ID);
        matcher.addURI(AUTHORITY, DbPersistenceContract.FeedEntry.TABLE_NAME, FEED_LIST);
        matcher.addURI(AUTHORITY, DbPersistenceContract.FeedEntry.TABLE_NAME + "/#", FEED_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new LocalDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = sURIMatcher.match(uri);
        Cursor retCursor;

        switch (uriType) {
            case ARTICLE_LIST:
                retCursor = mDbHelper.getReadableDatabase().query(
                        DbPersistenceContract.ArticleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ARTICLE_ID:
                String where = uri.getLastPathSegment();
                retCursor = mDbHelper.getReadableDatabase().query(
                        DbPersistenceContract.ArticleEntry.TABLE_NAME,
                        projection,
                        DbPersistenceContract.ArticleEntry._ID + " = ?",
                        new String[]{where},
                        null,
                        null,
                        sortOrder
                );
                break;
            case FEED_LIST:
                retCursor = mDbHelper.getReadableDatabase().query(
                        DbPersistenceContract.FeedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FEED_ID:
                where = uri.getLastPathSegment();
                retCursor = mDbHelper.getReadableDatabase().query(
                        DbPersistenceContract.FeedEntry.TABLE_NAME,
                        projection,
                        DbPersistenceContract.FeedEntry._ID + " = ?",
                        new String[]{where},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int uriType = sURIMatcher.match(uri);
        Uri returnUri;

        Cursor exists;
        switch (uriType) {
            case ARTICLE_LIST:
                exists = db.query(
                        DbPersistenceContract.ArticleEntry.TABLE_NAME,
                        new String[]{DbPersistenceContract.ArticleEntry._ID},
                        DbPersistenceContract.ArticleEntry._ID + " = ?",
                        new String[]{contentValues.getAsString(DbPersistenceContract.ArticleEntry._ID)},
                        null,
                        null,
                        null
                );
                if (exists.moveToLast()) {
                    long _id = db.update(
                            DbPersistenceContract.ArticleEntry.TABLE_NAME, contentValues,
                            DbPersistenceContract.ArticleEntry._ID + " = ?",
                            new String[]{contentValues.getAsString(DbPersistenceContract.ArticleEntry._ID)}
                    );
                    if (_id > 0) {
                        returnUri = Uri.parse(ARTICLE_PATH + "/" + _id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                } else {
                    long _id = db.insert(DbPersistenceContract.ArticleEntry.TABLE_NAME, null, contentValues);
                    if (_id >= 0) {
                        returnUri = Uri.parse(ARTICLE_PATH + "/" + _id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                }
                exists.close();
                break;
            case FEED_LIST:
                exists = db.query(
                        DbPersistenceContract.FeedEntry.TABLE_NAME,
                        new String[]{DbPersistenceContract.FeedEntry._ID},
                        DbPersistenceContract.FeedEntry._ID + " = ?",
                        new String[]{contentValues.getAsString(DbPersistenceContract.FeedEntry._ID)},
                        null,
                        null,
                        null
                );
                if (exists.moveToLast()) {
                    long _id = db.update(
                            DbPersistenceContract.FeedEntry.TABLE_NAME, contentValues,
                            DbPersistenceContract.FeedEntry._ID + " = ?",
                            new String[]{contentValues.getAsString(DbPersistenceContract.FeedEntry._ID)}
                    );
                    if (_id > 0) {
                        returnUri = Uri.parse(FEED_PATH + "/" + _id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                } else {
                    long _id = db.insert(DbPersistenceContract.FeedEntry.TABLE_NAME, null, contentValues);
                    if (_id >= 0) {
                        returnUri = Uri.parse(FEED_PATH + "/" + _id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                }
                exists.close();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int uriType = sURIMatcher.match(uri);
        int rowsDeleted;

        switch (uriType) {
            case ARTICLE_ID:
                rowsDeleted = db.delete(
                        DbPersistenceContract.ArticleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FEED_ID:
                rowsDeleted = db.delete(
                        DbPersistenceContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sURIMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case ARTICLE_ID:
                rowsUpdated = db.update(DbPersistenceContract.ArticleEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs
                );
                break;
            case FEED_ID:
                rowsUpdated = db.update(DbPersistenceContract.FeedEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
