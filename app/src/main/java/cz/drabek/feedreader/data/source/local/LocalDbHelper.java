package cz.drabek.feedreader.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class LocalDbHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "FeedReader.db";

    public LocalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DbPersistenceContract.ArticleEntry.onCreate(db);
        DbPersistenceContract.FeedEntry.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DbPersistenceContract.ArticleEntry.onUpgrade(db, oldVersion, newVersion);
        DbPersistenceContract.FeedEntry.onUpgrade(db, oldVersion, newVersion);
    }
}
