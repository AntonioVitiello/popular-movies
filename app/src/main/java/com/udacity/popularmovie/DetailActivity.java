package com.udacity.popularmovie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.popularmovie.adapter.ReviewsAdapter;
import com.udacity.popularmovie.data.FavoritesContract;
import com.udacity.popularmovie.databinding.ActivityDetailBinding;
import com.udacity.popularmovie.model.DetailModel;
import com.udacity.popularmovie.model.ReviewModel;
import com.udacity.popularmovie.model.TrailersModel;
import com.udacity.popularmovie.net.TmdbUtils;
import com.udacity.popularmovie.net.json.movies.TmdbMovie;
import com.udacity.popularmovie.net.json.reviews.TmdbReview;
import com.udacity.popularmovie.net.json.reviews.TmdbReviewsContainer;
import com.udacity.popularmovie.net.json.trailers.TmdbTrailersContainer;
import com.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.udacity.popularmovie.data.FavoritesContract.FavoritesEntry.CONTENT_URI;

public class DetailActivity extends AppCompatActivity {
    public static final String KEY_TMDB_RESULT = "tmdb_result";
    private static final String LOG_TAG = "DetailActivity";
    private static final String KEY_INSTANCE_STATE_PARC = "parcelable_key_save_state";
    private ActivityDetailBinding mBinding;
    private TmdbMovie mResult;

    private retrofit2.Callback<TmdbTrailersContainer> trailersCallback = new retrofit2.Callback<TmdbTrailersContainer>() {
        @Override
        public void onResponse(Call<TmdbTrailersContainer> call, Response<TmdbTrailersContainer> response) {
            Log.d(LOG_TAG, "TMDb Request URL: %s", call.request().url());
            if (response.isSuccessful()) {
                TmdbTrailersContainer trailersContainer = response.body();
                TrailersModel trailersModel = new TrailersModel(trailersContainer.getResults());
                mBinding.content.setTrailersModel(trailersModel);
            } else {
                // handle request errors depending on status code...
                int statusCode = response.code();
                Log.e(LOG_TAG, "Received HTTP %d on TMDb trailers data request.", statusCode);
            }
        }

        @Override
        public void onFailure(Call<TmdbTrailersContainer> call, Throwable tr) {
            Log.e(LOG_TAG, "Error loading JSON from TMDb: ", tr);
        }
    };

    private retrofit2.Callback<TmdbReviewsContainer> reviewsCallback = new retrofit2.Callback<TmdbReviewsContainer>() {
        @Override
        public void onResponse(Call<TmdbReviewsContainer> call, Response<TmdbReviewsContainer> response) {
            Log.d(LOG_TAG, "TMDb Request URL: %s", call.request().url());
            if (response.isSuccessful()) {
                TmdbReviewsContainer reviewsContainer = response.body();
                List<TmdbReview> results = reviewsContainer.getResults();
                if(results.size() == 0){
                    mBinding.content.setNoReviews(true);
                    mBinding.content.executePendingBindings();
                    return;
                }

                ReviewsAdapter reviewsAdapter = new ReviewsAdapter(results);
                LinearLayoutManager layoutManager = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                RecyclerView recyclerView = mBinding.content.reviewsRv;
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(reviewsAdapter);

            } else {
                // handle request errors depending on status code...
                int statusCode = response.code();
                Log.e(LOG_TAG, "Received HTTP %d on TMDb reviews data request.", statusCode);
            }
        }

        @Override
        public void onFailure(Call<TmdbReviewsContainer> call, Throwable tr) {
            Log.e(LOG_TAG, "Error loading JSON from TMDb: ", tr);
        }
    };

    /**
     * Bind for app:posterUrl attribute in <ImageView> element of an xml layout
     * eg: <ImageView app:posterUrl="@{detailModel.posterPath}"/>
     *
     * @param view       The View that calls this function
     * @param posterPath The Image path without base url
     */
    @BindingAdapter({"posterUrl"})
    public static void loadImage(final ImageView view, String posterPath) {
        Uri posterUrl = TmdbUtils.buildImageUrl(posterPath);
        Picasso.with(view.getContext())
                .load(posterUrl)
                .error(R.drawable.im_poster_placeholder_error)
                .placeholder(R.drawable.im_poster_placeholder)
                .into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        TmdbUtils.checkConnection(view.getContext());
                    }
                });
    }

    /**
     * Bind for app:thumbUrl attribute in <ImageView> element of an xml layout
     * eg: <ImageView app:thumbUrl="@{trailerModel.trailerThumb[0]}"/>
     *
     * @param view     The View that calls this function
     * @param thumbUrl The Image complete url
     */
    @BindingAdapter({"thumbUrl"})
    public static void loadVideoThumbnail(final ImageView view, String thumbUrl) {
        Picasso.with(view.getContext())
                .load(thumbUrl)
                .error(R.drawable.im_poster_placeholder_error)
                .placeholder(R.drawable.im_poster_placeholder)
                .into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        TmdbUtils.checkConnection(view.getContext());
                    }
                });
    }

    /**
     * Bind for app:textToWebLink attribute in <TextView> element of xml layout
     * Transform a TextView in a web browser hyperlink eg:
     * <TextView app:textToWebLink="@{reviewModel.commentUrl}"
     *   android:id="@+id/review_link"
     *   android:layout_width="wrap_content"
     *   android:layout_height="wrap_content"
     *   android:textAppearance="?android:attr/textAppearanceSmall"
     *   android:layout_marginStart="8dp"
     *   android:layout_marginTop="8dp"
     *   app:layout_constraintStart_toStartOf="parent"
     *   app:layout_constraintTop_toBottomOf="@+id/review_content" />
     *
     * @param view  The TextView reference
     * @param model The ReviewModel instance
     */
    @BindingAdapter({"textToWebLink"})
    public static void setLinkable(final TextView view, ReviewModel model) {
        String label = model.getProgressive();
        String reviewUrl = model.getReviewUrl();
        String anchorHtml = "<a href=\"" + reviewUrl + "\">" + label + "</a>";
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            view.setText(Html.fromHtml(anchorHtml));
        } else {
            view.setText(Html.fromHtml(anchorHtml, Html.FROM_HTML_MODE_COMPACT));
        }
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }


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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_INSTANCE_STATE_PARC, mResult);
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
        TmdbUtils.trailersRequest(movieId, trailersCallback);

        // Enqueue Reviews Request for TMDb
        TmdbUtils.reviewsRequest(movieId, reviewsCallback);
    }

    /**
     * Defines action on trailer click
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
        Log.d(LOG_TAG, "Opening Youtube video: %s", intent.getDataString());
        startActivity(intent);
    }

    /**
     * Defines action on review click
     * @param view
     */
    public void onClickComment(View view) {
        String reviewUrl = view.getTag().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(reviewUrl));
        // Open browser app if exists on device
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        Log.d(LOG_TAG, "Opening review in browser: %s", intent.getDataString());
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
     * @param isFavorite
     */
    private void setFabImage(boolean isFavorite){
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
        Log.d(LOG_TAG, "Movie inserted in Favorite DB: %s ", uri.toString());
    }

    /**
     * Remove movie from favorites: DELETE from DB
     */
    private void removeFavorite() {
        String selection = FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {String.valueOf(mResult.getId())};
        int count = getContentResolver().delete(CONTENT_URI, selection, selectionArgs);
        Snackbar.make(mBinding.coordinatorLayout, R.string.movie_removed, Snackbar.LENGTH_SHORT).show();
        Log.d(LOG_TAG, "Movie %1$d deleted from Favorite DB", mResult.getId());
    }

    /**
     * Check if this movie is already present in Favorites
     * Retrive the movie in db using just _id field like projection
     * @return
     */
    private boolean isFavorite() {
        int _id = -1;
        String[] projection = {FavoritesContract.FavoritesEntry._ID};
        String selection = FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {String.valueOf(mResult.getId())};
        Cursor cursor = getContentResolver().query(CONTENT_URI, projection, selection, selectionArgs, null);
        if(cursor != null && cursor.moveToFirst()){
            _id = cursor.getInt(0);
            cursor.close();
        }
        Log.d(LOG_TAG, "isFavorite: %s", (_id != -1 ? "Yes" : "No"));
        return _id != -1;
    }

}
