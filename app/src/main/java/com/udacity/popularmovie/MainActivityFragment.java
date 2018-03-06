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
import com.udacity.popularmovie.event.ClickMenuEvent;
import com.udacity.popularmovie.event.TmdbMovieEvent;
import com.udacity.popularmovie.net.TmdbUtils;
import com.udacity.popularmovie.net.callback.MovieCallbacks;
import com.udacity.popularmovie.net.json.movies.TmdbMovie;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 20/02/2018.
 */

public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {
    public static final int TYPE_MOST_POPULAR = 0;
    public static final int TYPE_TOP_RATED = 1;
    public static final int TYPE_FAVORITES = 2;
    private static final String KEY_TMDB_RESULTS = "tmdb_results_key";
    private static final String KEY_CURRENT_SERVICE = "current_service";
    private static final String KEY_GRID_POSITION = "selected_position";
    private FavoritesLoader mFavoritesLoader;
    private int mCurrentService = -1;
    private GridView mGridView;
    private PostersAdapter mPostersAdapter;
    private FavoritesAdapter mFavoritesAdapter;
    private List<TmdbMovie> mResults = new ArrayList<>();

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
    public void onStart() {
        super.onStart();
        // Register EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unregister EventBus
        EventBus.getDefault().unregister(this);
    }

    /**
     * On poster click start Activity Detail
     * Implementation method for interface AdapterView.OnItemClickListener
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

    private void retrieve(int serviceRequested) {
        // Call TMDb api only if the necessary data are not already present
        if (mCurrentService == serviceRequested) {
            refreshView(true);
            return;
        }
        // Check connettivity before lauch search
        if (TmdbUtils.checkConnection(getActivity())) {
            switch (serviceRequested) {
                case TYPE_MOST_POPULAR:
                    // Enqueue MostPolpular Request for TMDb
                    TmdbUtils.mostPopularRequest(new MovieCallbacks());
                    break;
                case TYPE_TOP_RATED:
                    // Enqueue TopRated Request for TMDb
                    TmdbUtils.topRatedRequest(new MovieCallbacks());
                    break;
                case TYPE_FAVORITES:
                    showFavorites();
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown service type: " + serviceRequested);
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

    /**
     * EventBus received on Toolbar or BottomNavigationView item click
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClickMenuEvent(ClickMenuEvent event) {
        retrieve(event.getServiceRequested());
        // EventBus.getDefault().removeStickyEvent(event);
    }

    /**
     * EventBus received on TMDb API Response
     * TmdbService: movie/popular
     * TmdbService: movie/top_rated
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTmdbMovieEvent(TmdbMovieEvent event) {
        List<TmdbMovie> results = event.getResults();
        int serviceType = event.getServiceType();
        updateData(results, serviceType);
        // EventBus.getDefault().removeStickyEvent(event);
    }
}