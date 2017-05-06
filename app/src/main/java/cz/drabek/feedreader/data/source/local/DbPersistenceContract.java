package cz.drabek.feedreader.data.source.local;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DbPersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DbPersistenceContract() {}

    public static class ArticleEntry implements BaseColumns {

        public static final String TABLE_NAME = "article";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_CREATION_DATE = "creation_date";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CONTENT = "content";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_URL           + " TEXT," +
                        COLUMN_NAME_TITLE         + " TEXT," +
                        COLUMN_NAME_AUTHOR        + " TEXT," +
                        COLUMN_NAME_CREATION_DATE + " DATE," +
                        COLUMN_NAME_DESCRIPTION   + " TEXT," +
                        COLUMN_NAME_CONTENT       + " TEXT)";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;



        public static void onCreate(SQLiteDatabase db) { db.execSQL(SQL_CREATE_ENTRIES); }

        public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            dropAndCreateTable(db);
        }

        public static void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            dropAndCreateTable(db);
        }

        public static void dropAndCreateTable(SQLiteDatabase db){
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }

}
