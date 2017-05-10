package cz.drabek.feedreader.articledetail;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cz.drabek.feedreader.R;
import cz.drabek.feedreader.articles.ArticlesPresenter;
import cz.drabek.feedreader.util.ActivityUtils;
import cz.drabek.feedreader.util.Injection;

public class ArticleDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ARTICLE_ID = "ARTICLE_ID";
    private ArticleDetailFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articledetail_act);

        // Set up the toolbar.
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        int articleId = getIntent().getIntExtra(EXTRA_ARTICLE_ID, 0);

        // Set up fragment
        mFragment = (ArticleDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (mFragment == null) {
            mFragment = ArticleDetailFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.contentFrame);
        }

        // Create the presenter
        ArticleDetailContract.Presenter presenter = new ArticleDetailPresenter(
                getApplicationContext(),
                getSupportLoaderManager(),
                Injection.provideTasksRepository(this),
                mFragment
        );
        presenter.setArticleId(articleId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_article_detail_menu, menu);
        return true;
    }

    // TODO better getArticleUrl() - dont want to need mFragment => (intent extra) change from ID to URL
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                String message = getResources().getString(R.string.message_share_article) + mFragment.getArticleUrl();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            default:
                return false;
        }
    }

}
