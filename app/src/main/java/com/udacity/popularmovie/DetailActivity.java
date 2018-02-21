package com.udacity.popularmovie;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.popularmovie.databinding.ActivityDetailBinding;
import com.udacity.popularmovie.model.DetailActivityModel;
import com.udacity.popularmovie.model.Result;
import com.udacity.popularmovie.net.TmdbUtils;

public class DetailActivity extends AppCompatActivity {
    public static final String KEY_TMDB_RESULT = "api_tmdb_result";
    private static final String KEY_INSTANCE_STATE_PARC = "parcelable_key_save_state";
    private ActivityDetailBinding mBinding;
    private Result mResult;

    @BindingAdapter({"bind:posterUrl"})
    public static void loadImage(final ImageView view, String posterPath) {
        Uri posterUrl = TmdbUtils.buildImageUrl(posterPath);
        Picasso.with(view.getContext())
                .load(posterUrl)
                .error(R.drawable.placeholder_error)
                .placeholder(R.drawable.placeholder_animated)
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Data Binding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        if (savedInstanceState == null) {
            // Retrive extra data from Intent
            Bundle extras = getIntent().getExtras();
            mResult = extras.getParcelable(KEY_TMDB_RESULT);
        } else {
            mResult = savedInstanceState.getParcelable(KEY_INSTANCE_STATE_PARC);
        }

        setTitle(mResult.getTitle());
        mBinding.setMymodel(new DetailActivityModel(this, mResult));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_INSTANCE_STATE_PARC, mResult);
    }
}
