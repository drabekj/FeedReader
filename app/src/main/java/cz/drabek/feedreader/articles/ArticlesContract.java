package cz.drabek.feedreader.articles;

import android.database.Cursor;

import cz.drabek.feedreader.BasePresenter;
import cz.drabek.feedreader.BaseView;
import cz.drabek.feedreader.data.Article;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface ArticlesContract {

    interface View extends BaseView<Presenter> {

        void showTasks(Cursor data);

        void showNoTasks();

        void setLoadingIndicator(boolean active);

    }

    interface Presenter extends BasePresenter {

        void loadArticles();

        void openArticleDetails(Article article);

    }

}
