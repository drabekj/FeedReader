package cz.drabek.feedreader.data.source;


import android.support.annotation.NonNull;

import java.util.List;

import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.data.Feed;

/**
 * Callback interface to inform the user of network/database errors.
 */
public interface ArticlesDataSource {

    interface LoadArticlesCallback {

        void onArticlesLoaded(List<Article> articles);

        void onDataNotAvailable();
    }

    interface GetArticleCallback {

        void onArticleLoaded(Article article);

        void onDataNotAvailable();
    }

    interface LoadFeedsCallback {

        void onFeedsLoaded(List<Feed> feeds);

        void onDataNotAvailable();
    }

    interface GetFeedCallback {

        void onFeedLoaded(Feed feed);

        void onDataNotAvailable();

    }

    interface DownloadArticlesCallback {

        void onArticlesDownloaded(List<Article> articles);

        void onDataNotAvailable();
    }

    void downloadArticles(@NonNull List<Feed> feeds,@NonNull DownloadArticlesCallback callback);

    void getArticles(@NonNull LoadArticlesCallback callback);

    void getArticle(@NonNull int articleId, @NonNull GetArticleCallback callback);

    void saveArticle(@NonNull Article article);

    void getFeeds(@NonNull LoadFeedsCallback callback);

    void getFeed(@NonNull int feedId, @NonNull GetFeedCallback callback);

    void saveFeed(@NonNull Feed feed);

    void deleteFeed(@NonNull int feedId);

}
