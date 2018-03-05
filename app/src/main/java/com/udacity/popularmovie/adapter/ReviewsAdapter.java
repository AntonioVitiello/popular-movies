package com.udacity.popularmovie.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.udacity.popularmovie.R;
import com.udacity.popularmovie.databinding.ReviewItemBinding;
import com.udacity.popularmovie.model.ReviewModel;
import com.udacity.popularmovie.net.json.reviews.TmdbReview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 26/02/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private List<TmdbReview> mReviews = new ArrayList<>();

    public ReviewsAdapter(@NonNull List<TmdbReview> results) {
        mReviews.addAll(results);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ReviewItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.review_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TmdbReview review = mReviews.get(position);
        holder.bind(new ReviewModel(review, position, getItemCount()));
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ReviewItemBinding mBinding;

        public ViewHolder(ReviewItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(@NonNull ReviewModel model) {
            mBinding.setReviewModel(model);
            mBinding.executePendingBindings();
        }
    }

}