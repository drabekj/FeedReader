package cz.drabek.feedreader.articles;


import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.data.source.ArticlesRepository;
import static cz.drabek.feedreader.util.Preconditions.checkNotNull;

public class ArticlesPresenter implements
        ArticlesContract.Presenter, ArticlesRepository.LoadArticlesCallback,
        ArticlesRepository.GetArticleCallback {

    private ArticlesContract.View mArticlesView;
    private ArticlesRepository mArticlesRepository;

    public ArticlesPresenter(@NonNull ArticlesRepository articlesRepository,
                             @NonNull ArticlesContract.View articlesView) {
        mArticlesRepository = checkNotNull(articlesRepository, "articlesRepository cannot be null");
        mArticlesView = checkNotNull(articlesView, "articlesView cannot be null!");

        mArticlesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadArticles();
    }

    private void loadArticles() {
        mArticlesRepository.getArticles(this);
    }

    @Override
    public void onArticleLoaded(Article article) {

    }

    /**
     * Articles saved to local storage.
     *
     * @param articles  Saved articles
     */
    @Override
    public void onArticlesLoaded(List<Article> articles) {
        for (Article a: articles)
            Log.d("Mock Data", "onArticlesLoaded: " + a);



        // TODO trigger loader to refresh data from local storage
    }

    @Override
    public void onDataNotAvailable() {

    }
}
