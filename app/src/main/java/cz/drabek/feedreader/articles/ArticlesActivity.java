package cz.drabek.feedreader.articles;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import cz.drabek.feedreader.R;
import cz.drabek.feedreader.data.source.ArticlesRepository;
import cz.drabek.feedreader.feeds.FeedActivity;
import cz.drabek.feedreader.service.DownloadBroadCastReceiver;
import cz.drabek.feedreader.service.DownloadService;
import cz.drabek.feedreader.util.ActivityUtils;
import cz.drabek.feedreader.util.ClientToServiceBinder;
import cz.drabek.feedreader.util.Injection;

public class ArticlesActivity extends AppCompatActivity {

    private static final long DOWNLOAD_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES / (15 * 6);

    private ClientToServiceBinder mServiceBinder;
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
        mArticlesPresenter = new ArticlesPresenter(
                getApplicationContext(),
                getSupportLoaderManager(),
                Injection.provideTasksRepository(getApplicationContext()),
                articlesFragment
        );

        // Start service
//        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent launchIntent = new Intent(ArticlesActivity.this.getApplicationContext(), DownloadBroadCastReceiver.class);
//        PendingIntent pi = PendingIntent.getBroadcast(ArticlesActivity.this.getApplicationContext(),
//                0, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), DOWNLOAD_INTERVAL,
//                pi);
//        Log.d("HONZA", "ArticlesActivity - alarm set up");
        mServiceBinder = ClientToServiceBinder.getInstance(mArticlesPresenter);
        initiateDownloadService();
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
                mServiceBinder.startService();
                mArticlesPresenter.loadArticles();
                return true;
            default:
                return false;
        }
    }

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

    private void initiateDownloadService() {
        // initiate repeating download service
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent launchIntent = new Intent(getApplicationContext(), DownloadBroadCastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), DOWNLOAD_INTERVAL, pi);

        // initiate manual download service
        mServiceBinder.doBindService();
    }
}
