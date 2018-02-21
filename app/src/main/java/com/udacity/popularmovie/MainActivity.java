package com.udacity.popularmovie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.popularmovie.model.TmdbResponseData;
import com.udacity.popularmovie.net.TmdbService;
import com.udacity.popularmovie.net.TmdbUtils;
import com.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_TMDB_RESPONSE_DATA = "api_tmdb_response_data";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String MAIN_FRAGMENT_TAG = "main_activity_fragment";
    private static final long MIN_TIME_BEFORE_NEXT_CALL = 10000l;
    private static final String KEY_API_CALL_TIME_MILLISEC = "api_call_time_millisec";
    private static final int TYPE_MOST_POPULAR = 2;
    private static final int TYPE_TOP_RATED = 1;
    private static final int TYPE_FAVORITES = 0;
    private MainActivityFragment mMainActivityfragment;
    private TmdbService mService;
    private long mLastApiCallTimeMillisec;
    private TmdbResponseData mTmdbResponseData;
    private int mCurrentType = TYPE_MOST_POPULAR;
    private Callback<TmdbResponseData> tmdbCallback = new Callback<TmdbResponseData>() {

        @Override
        public void onResponse(Call<TmdbResponseData> call, Response<TmdbResponseData> response) {
            Log.d(LOG_TAG, "TMDb Request URL: " + response.raw().request().url().toString());
            if (response.isSuccessful()) {
                mTmdbResponseData = response.body();
                mMainActivityfragment.updateData(mTmdbResponseData.getResults());
                mLastApiCallTimeMillisec = System.currentTimeMillis();
            } else {
                // handle request errors depending on status code...
                int statusCode = response.code();
                Log.e(LOG_TAG, "TMDB Response HTTP status: " + statusCode);
            }
        }

        @Override
        public void onFailure(Call<TmdbResponseData> call, Throwable tr) {
            Log.e(LOG_TAG, "Error loading JSON: ", tr);
        }
    };
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mService = TmdbUtils.getService();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        setupBottomNav(bottomNav);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mMainActivityfragment = new MainActivityFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, mMainActivityfragment, MAIN_FRAGMENT_TAG)
                    .commit();
        } else {
            mLastApiCallTimeMillisec = savedInstanceState.getLong(KEY_API_CALL_TIME_MILLISEC, 0);
            mTmdbResponseData = savedInstanceState.getParcelable(KEY_TMDB_RESPONSE_DATA);
            mMainActivityfragment = (MainActivityFragment) fragmentManager.findFragmentByTag(MAIN_FRAGMENT_TAG);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Fragment view has not been created yet on Activity create, so Fragment should be only updated on Activity start or resume
        retrive(mCurrentType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_most_popular:
                mLastApiCallTimeMillisec = 0;
                retriveMostPopular();
                break;
            case R.id.action_top_rated:
                mLastApiCallTimeMillisec = 0;
                retriveTopRated();
                break;
            case R.id.action_favorites:
                mLastApiCallTimeMillisec = 0;
                retriveFavorites();
                break;
            default:
                Log.e(LOG_TAG, "Unable to manage menu action: " + item.toString());
        }

        return false;
    }

    private void retriveMostPopular() {
        retrive(TYPE_MOST_POPULAR);
    }

    private void retriveTopRated() {
        retrive(TYPE_TOP_RATED);
    }

    private void retriveFavorites() {
        retrive(TYPE_FAVORITES);
    }

    private void retrive(int type) {
        if(!TmdbUtils.checkConnection(this)){
            return;
        }
        long offs = System.currentTimeMillis() - mLastApiCallTimeMillisec;
        Log.d(LOG_TAG, "currentTimeMillis = " + System.currentTimeMillis() + ", mLastApiCallTimeMillisec = " + mLastApiCallTimeMillisec + ", offs = " + offs);
        if (mTmdbResponseData == null || offs > MIN_TIME_BEFORE_NEXT_CALL) {
            // Make new API call: wait MIN_TIME_BEFORE_NEXT_CALL millisec before same api call
            switch (type) {
                case TYPE_FAVORITES:
                    mCurrentType = TYPE_FAVORITES;
                    mService.getFavoriteMovies(BuildConfig.TMDB_API_KEY)
                            .enqueue(tmdbCallback);
                    break;
                case TYPE_TOP_RATED:
                    mCurrentType = TYPE_TOP_RATED;
                    mService.getTopRatedMovies(BuildConfig.TMDB_API_KEY)
                            .enqueue(tmdbCallback);
                    break;
                default:
                    mCurrentType = TYPE_MOST_POPULAR;
                    mService.getPopularMovies(BuildConfig.TMDB_API_KEY)
                            .enqueue(tmdbCallback);
                    break;
            }
        } else {
            // No API call needed just update views
            mMainActivityfragment.updateData(mTmdbResponseData.getResults());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_API_CALL_TIME_MILLISEC, mLastApiCallTimeMillisec);
        outState.putParcelable(KEY_TMDB_RESPONSE_DATA, mTmdbResponseData);
    }

    private void setupBottomNav(BottomNavigationView bottomNav) {
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bnv_favorite:
                        mLastApiCallTimeMillisec = 0;
                        retriveFavorites();
                        break;
                    case R.id.bnv_top_rated:
                        mLastApiCallTimeMillisec = 0;
                        retriveTopRated();
                        break;
                    default:
                        mLastApiCallTimeMillisec = 0;
                        retriveMostPopular();
                }
                item.setChecked(true);
                return true;
            }
        });
    }

}
