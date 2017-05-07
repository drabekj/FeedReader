package cz.drabek.feedreader.feeds;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cz.drabek.feedreader.R;
import cz.drabek.feedreader.util.ActivityUtils;
import cz.drabek.feedreader.util.Injection;

public class FeedActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private NewFeedDialogFragment mDialog;
    private FeedPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_act);

        // Set up toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDialog = NewFeedDialogFragment.newInstance();


        // Set up fragment
        FeedFragment feedFragment = (FeedFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (feedFragment == null) {
            feedFragment = FeedFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), feedFragment, R.id.contentFrame);
        }

        // Crete presenter
        mPresenter = new FeedPresenter(
                getApplicationContext(),
                getSupportLoaderManager(),
                Injection.provideTasksRepository(getApplicationContext()),
                feedFragment,
                mDialog
        );
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_feed:
                mDialog.show(getSupportFragmentManager(), "dialog");
                return true;
            default:
                return false;
        }
    }
}
