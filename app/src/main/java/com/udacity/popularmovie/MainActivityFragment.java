package com.udacity.popularmovie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.udacity.popularmovie.adapter.FavoritesAdapter;
import com.udacity.popularmovie.adapter.PostersAdapter;
import com.udacity.popularmovie.data.loader.FavoritesLoader;
import com.udacity.popularmovie.net.TmdbUtils;
import com.udacity.popularmovie.net.json.movies.TmdbMovie;
import com.udacity.popularmovie.net.json.movies.TmdbMoviesContainer;
import com.util.Log;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Antonio on 20/02/2018.
 */

public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {
    public static final int TYPE_MOST_POPULAR = 0;
    public static final int TYPE_TOP_RATED = 1;
    public static final int TYPE_FAVORITES = 2;
    private static final String LOG_TAG = "MainActivityFragment";
    private static final String KEY_TMDB_RESULTS = "tmdb_results_key";
    private static final String KEY_CURRENT_SERVICE = "current_service";
    private static final String KEY_GRID_POSITION = "selected_position";
    FavoritesLoader mFavoritesLoader;
    private int mCurrentService = -1;
    private GridView mGridView;
    private PostersAdapter mPostersAdapter;
    private FavoritesAdapter mFavoritesAdapter;
    private List<TmdbMovie> mResults = new ArrayList<>();

    private Callback<TmdbMoviesContainer> tmdbCallback = new Callback<TmdbMoviesContainer>() {
        @Override
        public void onResponse(Call<TmdbMoviesContainer> call, Response<TmdbMoviesContainer> response) {
            HttpUrl url = call.request().url();
            Log.d(LOG_TAG, "HTTP Request: %s", url.toString());
            if (response.isSuccessful()) {
                int serviceType = TmdbUtils.getServiceType(url.pathSegments());
                mResults = response.body().getResults();
                updateData(mResults, serviceType);
            } else {
                // handle request errors depending on status code...
                int statusCode = response.code();
                Log.e(LOG_TAG, "Received HTTP %d on %s", statusCode, url.toString());
            }
        }

        @Override
        public void onFailure(Call<TmdbMoviesContainer> call, Throwable tr) {
            Log.e(LOG_TAG, "Error loading JSON from TMDb: ", tr);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = rootView.findViewById(R.id.posters_grid);
        mGridView.setOnItemClickListener(this);

        // Set GridView Adapter
        mPostersAdapter = new PostersAdapter(getActivity(), mResults);
        mFavoritesAdapter = new FavoritesAdapter(getActivity(), null);
        if (mCurrentService == TYPE_FAVORITES) {
            mGridView.setAdapter(mFavoritesAdapter);
        } else {
            mGridView.setAdapter(mPostersAdapter);
        }

        // Init loader
        mFavoritesLoader = new FavoritesLoader(getActivity(), mFavoritesAdapter);
        getLoaderManager().initLoader(FavoritesLoader.FAVORITES_LOADER_ID, null, mFavoritesLoader);

        return rootView;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // Restore previous state if exists
        if (savedInstanceState != null) {
            mResults = savedInstanceState.getParcelableArrayList(KEY_TMDB_RESULTS);
            mCurrentService = savedInstanceState.getInt(KEY_CURRENT_SERVICE, -1);
            mGridView.smoothScrollToPosition(savedInstanceState.getInt(KEY_GRID_POSITION, 0));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView(false);
    }

    /**
     * On poster click start Activity Detail
     *
     * @param adapterView
     * @param view
     * @param position
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        ListAdapter adapter = mGridView.getAdapter();
        TmdbMovie result = adapter instanceof FavoritesAdapter ?
                ((FavoritesAdapter) adapter).buildResult(position) : mResults.get(position);
        intent.putExtra(DetailActivity.KEY_TMDB_RESULT, result);

        // Manage poster transition
        String transitionName = getString(R.string.transition_name);
        ImageView poster = view.findViewById(R.id.poster_iv);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), poster, transitionName);

        // Start Detail Activity
        startActivity(intent, options.toBundle());
    }

    public void updateData(List<TmdbMovie> results, int serviceCode) {
        mCurrentService = serviceCode;
        mResults = results;
        refreshView(true);
    }

    public void refreshView(boolean scrollTop) {
        if (mCurrentService == TYPE_FAVORITES) {
            if (mFavoritesAdapter.getCount() == 0) {
                showFavorites();
            } else {
                if (scrollTop) {
                    mGridView.smoothScrollToPosition(0);
                }
                if (!(mGridView.getAdapter() instanceof FavoritesAdapter)) {
                    mGridView.setAdapter(mFavoritesAdapter);
                }
            }
        } else {
            if (mResults.size() == 0) {
                retrieveMostPopular();
            } else {
                if (scrollTop) {
                    mGridView.smoothScrollToPosition(0);
                }
                if (!(mGridView.getAdapter() instanceof PostersAdapter)) {
                    mGridView.setAdapter(mPostersAdapter);
                }
                mPostersAdapter.updateData(mResults);
            }
        }
    }

    public void retrieveMostPopular() {
        retrieve(TYPE_MOST_POPULAR);
    }

    public void retrieveTopRated() {
        retrieve(TYPE_TOP_RATED);
    }

    private void retrieve(int serviceCode) {
        // Call TMDb api only if the necessary data are not already present
        switch (mCurrentService - serviceCode) {
            case 0:
                refreshView(true);
                break;
            default:
                // Check connettivity before lauch search
                if (TmdbUtils.checkConnection(getActivity())) {
                    switch (serviceCode) {
                        case TYPE_MOST_POPULAR:
                            // Enqueue MostPolpular Request for TMDb
                            TmdbUtils.mostPopularRequest(tmdbCallback);
                            break;
                        case TYPE_TOP_RATED:
                            // Enqueue TopRated Request for TMDb
                            TmdbUtils.topRatedRequest(tmdbCallback);
                            break;
                        default:
                            throw new UnsupportedOperationException("Unknown search type: " + serviceCode);
                    }
                }
        }
    }

    public void showFavorites() {
        if ((mGridView.getAdapter() instanceof FavoritesAdapter)) {
            mGridView.smoothScrollToPosition(0);
        } else {
            mGridView.setAdapter(mFavoritesAdapter);
        }
        getLoaderManager().restartLoader(FavoritesLoader.FAVORITES_LOADER_ID, null, mFavoritesLoader);
        mCurrentService = TYPE_FAVORITES;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_TMDB_RESULTS, (ArrayList<? extends Parcelable>) mResults);
        outState.putInt(KEY_CURRENT_SERVICE, mCurrentService);
        outState.putInt(KEY_GRID_POSITION, mGridView.getFirstVisiblePosition() + 1);
    }

}
