package cz.drabek.feedreader;

public interface BaseView<T> {

    /**
     * Assigns presenter to this view.
     *
     * @param presenter
     */
    void setPresenter(T presenter);

}
