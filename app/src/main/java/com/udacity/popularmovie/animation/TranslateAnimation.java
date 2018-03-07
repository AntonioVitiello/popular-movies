package com.udacity.popularmovie.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import com.udacity.popularmovie.adapter.FavoritesAdapter;
import com.udacity.popularmovie.adapter.PostersAdapter;

/**
 * Created by Antonio on 07/03/2018.
 */

public class TranslateAnimation {
    /**
     * Poster animation for Favorites
     * @param holder
     * @param isScrollingDown
     */
    public static void animate(FavoritesAdapter.ViewHolder holder, boolean isScrollingDown) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator translationY = ObjectAnimator.ofFloat(holder.getImageView(), "translationY", isScrollingDown == true ? 200 : -200, 0);
        translationY.setDuration(500);

//        ObjectAnimator translationX = ObjectAnimator.ofFloat(holder.getImageView(), "translationX", -50, 50, -30, 30, -20, 20, -5, 5, 0);
//        translationX.setDuration(500);

        animatorSet.play(translationY);
//        animatorSet.playTogether(translationX, translationY);
        animatorSet.start();
    }

    /**
     * Poster animation for Most Popular and Top Rated
     * @param holder
     * @param isScrollingDown
     */
    public static void animate(PostersAdapter.ViewHolder holder, boolean isScrollingDown) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator translationY = ObjectAnimator.ofFloat(holder.getImageView(), "translationY", isScrollingDown == true ? 200 : -200, 0);
        translationY.setDuration(500);

        animatorSet.play(translationY);
        animatorSet.start();
    }
}
