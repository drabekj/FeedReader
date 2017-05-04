package cz.drabek.feedreader.articles;


import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.data.source.ArticlesDataSource;
import cz.drabek.feedreader.data.source.ArticlesRepository;

public class ArticlesPresenter implements ArticlesContract.Presenter {

    private ArticlesContract.View mArticlesView;
    private ArticlesRepository mArticlesRepository;

    public ArticlesPresenter(@NonNull ArticlesRepository articlesRepository,
                             @NonNull ArticlesContract.View articlesView) {
//        mArticlesRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null");
//        mArticlesView = checkNotNull(tasksView, "tasksView cannot be null!");
        mArticlesRepository = articlesRepository;
        mArticlesView = articlesView;

        mArticlesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadArticles();
    }

    private void loadArticles() {
        mArticlesRepository.getArticles(new ArticlesDataSource.LoadArticlesCallback() {
            @Override
            public void onArticlesLoaded(List<Article> articles) {
                List<Article> tasksToShow = articles;

                for (Article a: articles)
                    Log.d("Mock Data", "onArticlesLoaded: " + a);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }
}
