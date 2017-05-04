package cz.drabek.feedreader.data.source;


import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.data.source.local.StaticArticlesDataSource;

public class ArticlesRepository implements ArticlesDataSource {

    public final String TAG = "ArticlesRepository";

    // TODO currently implemented for mock static data
    @Override
    public void getArticles(@NonNull final LoadArticlesCallback callback) {
        final StaticArticlesDataSource mMockData = new StaticArticlesDataSource();

        mMockData.getArticles(new LoadArticlesCallback() {
            @Override
            public void onArticlesLoaded(List<Article> articles) {
                callback.onArticlesLoaded(articles);
            }

            @Override
            public void onDataNotAvailable() {
                Log.w(TAG, "onDataNotAvailable: there was an error loading data");
            }
        });
    }

    @Override
    public void getArticle(@NonNull String articleId, @NonNull GetArticleCallback callback) { }
}
