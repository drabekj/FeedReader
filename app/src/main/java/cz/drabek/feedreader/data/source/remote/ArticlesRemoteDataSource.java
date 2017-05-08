package cz.drabek.feedreader.data.source.remote;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.data.Feed;
import cz.drabek.feedreader.data.source.ArticlesDataSource;

public class ArticlesRemoteDataSource implements ArticlesDataSource {

    private static final String TAG = "HONZA-ArticlesRemDatSrc";
    private static ArticlesRemoteDataSource INSTANCE = null;

    private List<Article> list;

    private ArticlesRemoteDataSource() {
        list = new ArrayList<>(50);
    }

    public static ArticlesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ArticlesRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void downloadArticles(@NonNull List<Feed> feeds, @NonNull DownloadArticlesCallback callback) {
        try {
            download(feeds);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list.isEmpty())
            callback.onDataNotAvailable();
        else
            callback.onArticlesDownloaded(list);
    }

    private void download(List<Feed> feeds) throws IOException {
        for (Feed feed: feeds) {
            String urlString = feed.getUrl();
            Log.d(TAG, "downloadArticles: " + urlString);
            URL url = new URL(urlString);
            SyndFeedInput input = new SyndFeedInput();

            try {
                SyndFeed feedStream = input.build(new XmlReader(url));
                list.addAll(parseStream(feedStream));
            } catch (FeedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Article> parseStream(SyndFeed feedStream) {
        Article article;

        for (Iterator<?> entryIter = feedStream.getEntries().iterator(); entryIter.hasNext();) {
            SyndEntry syndEntry = (SyndEntry) entryIter.next();

            String articleContent = "";
            if (syndEntry.getContents() != null) {
                for (Iterator<?> it = syndEntry.getContents().iterator(); it.hasNext();) {
                    SyndContent syndContent = (SyndContent) it.next();

                    if (syndContent != null) {
                        articleContent += syndContent.getValue();
                    }
                }
            }

            article = new Article(syndEntry.getTitle(), syndEntry.getUri(), syndEntry.getAuthor(),
                    syndEntry.getPublishedDate().toString(), articleContent);
            list.add(article);
        }

        return list;
    }



    // useless
    @Override
    public void getArticle(@NonNull int articleId, @NonNull GetArticleCallback callback) { }

    @Override
    public void getFeed(@NonNull int feedId, @NonNull GetFeedCallback callback) { }

    @Override
    public void saveFeed(@NonNull Feed feed) { }

    @Override
    public void deleteFeed(@NonNull int feedId) { }

    @Override
    public void getFeeds(@NonNull LoadFeedsCallback callback) { }

    @Override
    public void saveArticle(@NonNull Article article) { }

    @Override
    public void getArticles(@NonNull LoadArticlesCallback callback) {

    }
}
