package cz.drabek.feedreader.data.source;

import android.content.ContentValues;

import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.data.source.local.DbPersistenceContract;

public class ArticleValues {

    public static ContentValues from(Article article) {
        ContentValues values = new ContentValues();
        values.put(DbPersistenceContract.ArticleEntry._ID, article.getId());
        values.put(DbPersistenceContract.ArticleEntry.COLUMN_NAME_TITLE, article.getTitle());
        values.put(DbPersistenceContract.ArticleEntry.COLUMN_NAME_URL, article.getUrl());
        values.put(DbPersistenceContract.ArticleEntry.COLUMN_NAME_AUTHOR, article.getAuthor());
        values.put(DbPersistenceContract.ArticleEntry.COLUMN_NAME_CONTENT, article.getContent());
        return values;
    }
}
