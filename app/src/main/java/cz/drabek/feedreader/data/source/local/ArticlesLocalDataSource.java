package cz.drabek.feedreader.data.source.local;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.data.source.ArticleValues;
import cz.drabek.feedreader.data.source.ArticlesDataSource;
import static cz.drabek.feedreader.util.Preconditions.checkNotNull;

public class ArticlesLocalDataSource implements ArticlesDataSource {

    @Override
    public void getArticles(@NonNull LoadArticlesCallback callback) {

    }

    @Override
    public void getArticle(@NonNull String articleId, @NonNull GetArticleCallback callback) {

    }

    @Override
    public void saveArticle(@NonNull Article article) {
        checkNotNull(article);

        ContentValues values = ArticleValues.from(article);
        // TODO insert values
    }
}
