package cz.drabek.feedreader.articledetail;

import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;

import cz.drabek.feedreader.data.source.ArticlesRepository;

public class ArticleDetailPresenter implements ArticleDetailContract.Presenter {

    private int mArticleId;
    private LoaderManager mLoaderManager;
    private ArticlesRepository mArticlesRepository;
    private ArticleDetailContract.View mView;

    public ArticleDetailPresenter(@NonNull int articleId,
                                  @NonNull LoaderManager loaderManager,
                                  @NonNull ArticlesRepository articlesRepository,
                                  @NonNull ArticleDetailContract.View view) {
        mArticleId = articleId;
        mLoaderManager = loaderManager;
        mArticlesRepository = articlesRepository;
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadArticle();
    }

    // TODO implement loadArticle
    private void loadArticle() {
        //...
    }
}
