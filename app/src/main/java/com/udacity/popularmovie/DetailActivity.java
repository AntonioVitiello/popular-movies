package com.udacity.popularmovie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.udacity.popularmovie.adapter.ReviewsAdapter;
import com.udacity.popularmovie.data.FavoritesContract;
import com.udacity.popularmovie.databinding.ActivityDetailBinding;
import com.udacity.popularmovie.event.TmdbReviewsEvent;
import com.udacity.popularmovie.event.TmdbTrailersEvent;
import com.udacity.popularmovie.model.DetailModel;
import com.udacity.popularmovie.model.TrailersModel;
import com.udacity.popularmovie.net.TmdbUtils;
import com.udacity.popularmovie.net.callback.ReviewsCallback;
import com.udacity.popularmovie.net.callback.TrailersCallback;
import com.udacity.popularmovie.net.json.movies.TmdbMovie;
import com.udacity.popularmovie.net.json.reviews.TmdbReview;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import timber.log.Timber;

import static com.udacity.popularmovie.data.FavoritesContract.FavoritesEntry.CONTENT_URI;

public class DetailActivity extends AppCompatActivity {
    public static final String KEY_TMDB_RESULT = "tmdb_result";
    private static final String KEY_INSTANCE_STATE_PARC = "parcelable_key_save_state";
    private ActivityDetailBinding mBinding;
    private TmdbMovie mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Retrive extra data from Intent
            Bundle extras = getIntent().getExtras();
            mResult = extras.getParcelable(KEY_TMDB_RESULT);
        } else {
            // Retrive data from saved Bundle
            mResult = savedInstanceState.getParcelable(KEY_INSTANCE_STATE_PARC);
        }

        // Data Binding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        mBinding.setDetailModel(new DetailModel(mResult));
        initComponents();

        // Retrive data from TMDb server
        retrieveDetailsData();
    }

    private void initComponents() {
        // Sets Toolbar and UP Navigation image action
        Toolbar toolbar = mBinding.detailToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set Favorite FAB image
        setFabImage(isFavorite());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Register EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_INSTANCE_STATE_PARC, mResult);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unregister EventBus
        EventBus.getDefault().unregister(this);
    }

    /**
     * Retrieve trailers from TMDb and make data binding
     */
    private void retrieveDetailsData() {
        if (!TmdbUtils.checkConnection(this)) {
            return;
        }
        int movieId = mResult.getId();

        // Enqueue Trailers Request for TMDb
        TmdbUtils.trailersRequest(movieId, new TrailersCallback());

        // Enqueue Reviews Request for TMDb
        TmdbUtils.reviewsRequest(movieId, new ReviewsCallback());
    }

    /**
     * Defines action on trailer click
     *
     * @param view
     */
    public void onTrailerClick(View view) {
        String videoId = (String) view.getTag();
        Uri videoUrl = Uri.parse(getString(R.string.trailer_video_url_app, videoId));
        Intent intent = new Intent(Intent.ACTION_VIEW, videoUrl);
        // Check if the youtube app exists on device
        if (intent.resolveActivity(getPackageManager()) == null) {
            // If the youtube app doesn't exist on device, then use the browser
            videoUrl = Uri.parse(getString(R.string.trailer_video_url_browser, videoId));
            intent = new Intent(Intent.ACTION_VIEW, videoUrl);
        }
        Timber.d("Opening Youtube video: %s", intent.getDataString());
        startActivity(intent);
    }

    /**
     * Defines action on review click
     *
     * @param view
     */
    public void onClickComment(View view) {
        String reviewUrl = view.getTag().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(reviewUrl));
        // Open browser app if exists on device
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        Timber.d("Opening review in browser: %s", intent.getDataString());
    }

    /**
     * Manage click on FAB Floating Action Button
     *
     * @param view
     */
    public void onClickFab(View view) {
        if (isFavorite()) {
            removeFavorite();
            setFabImage(false);
        } else {
            addFavorite();
            setFabImage(true);
        }
    }

    /**
     * Set favorite FAB (Floating Action Button) with on/off image
     *
     * @param isFavorite
     */
    private void setFabImage(boolean isFavorite) {
        DetailModel detailModel = mBinding.getDetailModel();
        detailModel.setFavorite(isFavorite);
    }

    public void setDrawableColor(int drawablResourceId, int color) {
        Drawable drawable = getResources().getDrawable(drawablResourceId);
        Drawable mutated = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(mutated, color);
        DrawableCompat.setTintMode(mutated, PorterDuff.Mode.SRC_IN);
    }

    /**
     * Add movie to favorites: INSERT in DB
     */
    private void addFavorite() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_BACKDROP_PATH, mResult.getBackdropPath());
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, mResult.getId());
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW, mResult.getOverview());
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH, mResult.getPosterPath());
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE, mResult.getReleaseDate());
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_TITLE, mResult.getTitle());
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_VOTE_AVERAGE, mResult.getVoteAverage());
        Uri uri = getContentResolver().insert(CONTENT_URI, contentValues);
        Snackbar.make(mBinding.coordinatorLayout, R.string.movie_added, Snackbar.LENGTH_SHORT).show();
        Timber.d("Movie inserted in Favorite DB: %s ", uri.toString());
    }

    /**
     * Remove movie from favorites: DELETE from DB
     */
    private void removeFavorite() {
        String selection = FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {String.valueOf(mResult.getId())};
        int count = getContentResolver().delete(CONTENT_URI, selection, selectionArgs);
        Snackbar.make(mBinding.coordinatorLayout, R.string.movie_removed, Snackbar.LENGTH_SHORT).show();
        Timber.d("Movie %1$d deleted from Favorite DB", mResult.getId());
    }

    /**
     * Check if this movie is already present in Favorites
     * Retrive the movie in db using just _id field like projection
     *
     * @return
     */
    private boolean isFavorite() {
        int _id = -1;
        String[] projection = {FavoritesContract.FavoritesEntry._ID};
        String selection = FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {String.valueOf(mResult.getId())};
        Cursor cursor = getContentResolver().query(CONTENT_URI, projection, selection, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst()) {
            _id = cursor.getInt(0);
            cursor.close();
        }
        Timber.d("isFavorite: %s", (_id != -1 ? "Yes" : "No"));
        return _id != -1;
    }

    /**
     * EventBus received on TMDb API Response
     * TmdbService: : movie/{id}/videos
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTrailersEvent(TmdbTrailersEvent event) {
        TrailersModel trailersModel = new TrailersModel(event.getResults());
        mBinding.content.setTrailersModel(trailersModel);
    }

    /**
     * EventBus received on TMDb API Response
     * TmdbService: : movie/{id}/reviews
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReviewsEvent(TmdbReviewsEvent event) {
        List<TmdbReview> results = event.getResults();
        if (results.size() == 0) {
            mBinding.content.setNoReviews(true);
            return;
        }

        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(results);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = mBinding.content.reviewsRv;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(reviewsAdapter);
    }

}