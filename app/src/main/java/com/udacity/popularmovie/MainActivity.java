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
import android.widget.Toast;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String MAIN_FRAGMENT_TAG = "main_activity_fragment";
    private MainActivityFragment mMainFragment;
    private Toast mToast;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupComponent();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(MAIN_FRAGMENT_TAG);
        if (fragment == null) {
            mMainFragment = new MainActivityFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, mMainFragment, MAIN_FRAGMENT_TAG)
                    .commit();
        } else {
            mMainFragment = (MainActivityFragment) fragment;
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
        // Inflate the menu, this adds items to the action bar if it is present.
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

        switch (item.getItemId()) {
            case R.id.mi_popular:
                mMainFragment.retrieveMostPopular();
                break;
            case R.id.mi_top_rated:
                mMainFragment.retrieveTopRated();
                break;
            case R.id.mi_favorites:
                mMainFragment.showFavorites();
                break;
            default:
                displayItemAsSelect = false;
                Timber.d("Unable to manage menu action: " + item.toString());
        }


        return displayItemAsSelect;
    }

}