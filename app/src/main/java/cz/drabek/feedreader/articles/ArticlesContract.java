package cz.drabek.feedreader.articles;

import cz.drabek.feedreader.BasePresenter;
import cz.drabek.feedreader.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface ArticlesContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }

}
