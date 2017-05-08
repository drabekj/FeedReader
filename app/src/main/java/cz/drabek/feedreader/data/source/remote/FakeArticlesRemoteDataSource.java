package cz.drabek.feedreader.data.source.remote;

import android.os.SystemClock;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.data.Feed;
import cz.drabek.feedreader.data.source.ArticlesDataSource;

public class FakeArticlesRemoteDataSource implements ArticlesDataSource {

    private static FakeArticlesRemoteDataSource INSTANCE = null;

    private List<Article> list;

    // Prevent direct instantiation.
    private FakeArticlesRemoteDataSource() {
        list = new ArrayList<>(20);
        
        for (int i = 0; i < 20; i++) {
            list.add(new Article(
                    "Title " + i,
                    "http://www.datasource" + i + ".com",
                    "Adolf " + i,
                    "Content: " + i + " Jelly beans powder cookie dragée lemon drops sweet sweet I love marshmallow. Topping danish I love jelly beans powder jelly lollipop dessert oat cake. Danish sweet roll cheesecake. Halvah cheesecake cotton candy carrot cake icing pastry marshmallow toffee lemon drops. Powder carrot cake jelly donut cake I love. Lemon drops halvah chupa chups sweet roll. Caramels topping bonbon jelly cupcake chocolate soufflé. Macaroon jelly tiramisu I love chocolate cake dragée toffee icing. Apple pie I love tootsie roll pudding. I love sweet roll oat cake caramels powder sweet roll I love candy. Cake chocolate caramels jelly pudding cookie tart. Gummies chocolate dessert ice cream cupcake. Jelly beans lemon drops I love fruitcake icing gummies fruitcake. Sesame snaps pastry cookie."));
        }
    }

    public static FakeArticlesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeArticlesRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getArticles(@NonNull LoadArticlesCallback callback) {
        if (list.isEmpty())
            callback.onDataNotAvailable();
        else
            callback.onArticlesLoaded(list);
    }

    @Override
    public void getArticle(@NonNull int articleId, @NonNull GetArticleCallback callback) { }

    @Override
    public void saveArticle(@NonNull Article article) { }

    @Override
    public void saveFeed(@NonNull Feed feed) { }

    @Override
    public void getFeeds(@NonNull LoadFeedsCallback callback) { }

    @Override
    public void getFeed(@NonNull int feedId, @NonNull GetFeedCallback callback) { }

    @Override
    public void deleteFeed(@NonNull int feedId) { }

}
