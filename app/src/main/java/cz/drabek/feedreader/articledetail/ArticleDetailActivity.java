package cz.drabek.feedreader.articledetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cz.drabek.feedreader.R;
import cz.drabek.feedreader.util.ActivityUtils;
import cz.drabek.feedreader.util.Injection;

public class ArticleDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ARTICLE_ID = "ARTICLE_ID";
    private ArticleDetailPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articledetail_act);

        // Set up the toolbar.
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        int articleId = getIntent().getIntExtra(EXTRA_ARTICLE_ID, 0);

        ArticleDetailFragment articleDetailFragment =
                (ArticleDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (articleDetailFragment == null) {
            articleDetailFragment = ArticleDetailFragment.newInstance(articleId);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), articleDetailFragment, R.id.contentFrame);
        }

        // Create the presenter
        mPresenter = new ArticleDetailPresenter(
                getApplicationContext(),
                articleId,
                getSupportLoaderManager(),
                Injection.provideTasksRepository(this),
                articleDetailFragment
        );
    }


}
