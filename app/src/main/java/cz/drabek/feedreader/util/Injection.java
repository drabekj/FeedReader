package cz.drabek.feedreader.util;

import android.content.Context;
import android.support.annotation.NonNull;

import cz.drabek.feedreader.data.source.ArticlesDataSource;
import cz.drabek.feedreader.data.source.ArticlesRepository;
import cz.drabek.feedreader.data.source.local.ArticlesLocalDataSource;
import cz.drabek.feedreader.data.source.remote.ArticlesRemoteDataSource;
import cz.drabek.feedreader.data.source.remote.FakeArticlesRemoteDataSource;

import static cz.drabek.feedreader.util.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * {@link ArticlesRepository} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static ArticlesRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);

        return ArticlesRepository.getInstance(provideRemoteDataSource(), provideLocalDataSource(context)
        );
    }

    public static ArticlesDataSource provideRemoteDataSource() {
        return ArticlesRemoteDataSource.getInstance();
    }

    public static ArticlesDataSource provideMockRemoteDataSource() {
        return FakeArticlesRemoteDataSource.getInstance();
    }

    public static ArticlesDataSource provideLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        return ArticlesLocalDataSource.getInstance(context.getContentResolver());
    }

}
