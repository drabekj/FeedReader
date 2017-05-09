package cz.drabek.feedreader.articles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import cz.drabek.feedreader.R;
import cz.drabek.feedreader.feeds.FeedActivity;
import cz.drabek.feedreader.util.ActivityUtils;
import cz.drabek.feedreader.util.Injection;

public class ArticlesActivity extends AppCompatActivity {

    private ArticlesPresenter mArticlesPresenter;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articles_act);

        // Set up the toolbar.
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.toolbar_progress_bar);
        setSupportActionBar(mToolbar);

        // Set up Articles Fragment
        ArticlesFragment articlesFragment =
                (ArticlesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (articlesFragment == null) {
            // Create the fragment
            articlesFragment = ArticlesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), articlesFragment, R.id.contentFrame);
        }

        // Create the presenter
        // initiate manual download service
        mArticlesPresenter = new ArticlesPresenter(
                getApplicationContext(),
                getSupportLoaderManager(),
                Injection.provideTasksRepository(getApplicationContext()),
                articlesFragment
        );

        mArticlesPresenter.initiateDownloadService();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mArticlesPresenter.unbindService();
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                Toast.makeText(this, "Settings not implemented yet.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_feeds:
                Intent intent = new Intent(this, FeedActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_item_refresh:
                mArticlesPresenter.loadArticles();
                return true;
            default:
                return false;
        }
    }

    // TODO loading indicator reset onConfigurationChange()
    public void setLoadingIndicator(boolean activated) {
        // TODO fix so that this if is not needed
        if (mProgressBar == null || mToolbar == null || mToolbar.findViewById(R.id.menu_item_refresh) == null)
            return;

        if (activated) {
            mProgressBar.setVisibility(View.VISIBLE);
            mToolbar.findViewById(R.id.menu_item_refresh).setVisibility(View.GONE);
        }
        else {
            mProgressBar.setVisibility(View.GONE);
            mToolbar.findViewById(R.id.menu_item_refresh).setVisibility(View.VISIBLE);
        }
    }

}
