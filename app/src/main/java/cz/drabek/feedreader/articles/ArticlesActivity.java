package cz.drabek.feedreader.articles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import cz.drabek.feedreader.R;
import cz.drabek.feedreader.util.ActivityUtils;

public class ArticlesActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articles_act);

        // Set up the toolbar.
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        // Set up Articles Fragment
        ArticlesFragment articlesFragment =
                (ArticlesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (articlesFragment == null) {
            // Create the fragment
            articlesFragment = ArticlesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), articlesFragment, R.id.contentFrame);
        }


    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

}
