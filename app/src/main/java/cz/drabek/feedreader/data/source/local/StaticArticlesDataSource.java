package cz.drabek.feedreader.data.source.local;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.data.source.ArticlesDataSource;

public class StaticArticlesDataSource implements ArticlesDataSource {

    private List<Article> list;

    public StaticArticlesDataSource() {
        list = new ArrayList<>(20);

        for (int i = 0; i < 20; i++)
            list.add(new Article(
                    "Title" + i,
                    "www.datasource" + i + ".com",
                    "Adolf " + i,
                    "Content: " + i));
    }

    @Override
    public void getArticles(@NonNull LoadArticlesCallback callback) {
        if (list.isEmpty())
            callback.onDataNotAvailable();
        else
            callback.onArticlesLoaded(list);
    }

    @Override
    public void getArticle(@NonNull String articleId, @NonNull GetArticleCallback callback) { }
}
