package com.udacity.popularmovie.model;

import com.udacity.popularmovie.MainApplication;
import com.udacity.popularmovie.R;
import com.udacity.popularmovie.net.json.reviews.TmdbReview;

/**
 * Created by Antonio on 26/02/2018.
 */

public class ReviewModel {
    private static final String sEmptyComment = MainApplication.getStringResource(R.string.review_no_comment);
    private static final int sMaxCommentChars = MainApplication.getIntResource(R.integer.max_review_comment_chars);

    private String content;
    private String author;
    private String reviewUrl;
    private String progressive;

    public ReviewModel(TmdbReview result, int position, int size) {
        content = result.getContent();
        if(content == null || content.isEmpty()){
            content = sEmptyComment;
        } else if(content.length() > sMaxCommentChars) {
            content = content.substring(0, sMaxCommentChars) + "...";
        }
        author = result.getAuthor();
        reviewUrl = result.getUrl();
        this.progressive = MainApplication.getStringResource(R.string.review_progressive_label, ++position, size);
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public String getProgressive() {
        return progressive;
    }
}
