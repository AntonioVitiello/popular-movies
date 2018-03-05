package com.udacity.popularmovie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.udacity.popularmovie.events.ClickMenuEvent;

import org.greenrobot.eventbus.EventBus;

import timber.log.Timber;

import static com.udacity.popularmovie.MainActivityFragment.TYPE_FAVORITES;
import static com.udacity.popularmovie.MainActivityFragment.TYPE_MOST_POPULAR;
import static com.udacity.popularmovie.MainActivityFragment.TYPE_TOP_RATED;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String MAIN_FRAGMENT_TAG = "main_activity_fragment";
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupComponent();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(MAIN_FRAGMENT_TAG);
        if (fragment == null) {
            MainActivityFragment mainFragment = new MainActivityFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, mainFragment, MAIN_FRAGMENT_TAG)
                    .commit();
        }
    }

    private void setupComponent() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        mBottomNavigationView = findViewById(R.id.bottom_nav);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.posters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mBottomNavigationView.setSelectedItemId(item.getItemId());
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean displayItemAsSelect = true;
        EventBus eventBus = EventBus.getDefault();

        switch (item.getItemId()) {
            case R.id.mi_popular:
                eventBus.post(new ClickMenuEvent(TYPE_MOST_POPULAR));
                break;
            case R.id.mi_top_rated:
                eventBus.post(new ClickMenuEvent(TYPE_TOP_RATED));
                break;
            case R.id.mi_favorites:
                eventBus.post(new ClickMenuEvent(TYPE_FAVORITES));
                break;
            default:
                displayItemAsSelect = false;
                Timber.d("Unable to manage menu action: " + item.toString());
        }

        return displayItemAsSelect;
    }

}