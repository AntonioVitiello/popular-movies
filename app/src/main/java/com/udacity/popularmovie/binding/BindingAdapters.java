package com.udacity.popularmovie.binding;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.popularmovie.R;
import com.udacity.popularmovie.model.ReviewModel;
import com.udacity.popularmovie.net.TmdbUtils;

/**
 * Created by Antonio on 06/03/2018.
 */

public class BindingAdapters {
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
     * android:id="@+id/review_link"
     * android:layout_width="wrap_content"
     * android:layout_height="wrap_content"
     * android:textAppearance="?android:attr/textAppearanceSmall"
     * android:layout_marginStart="8dp"
     * android:layout_marginTop="8dp"
     * app:layout_constraintStart_toStartOf="parent"
     * app:layout_constraintTop_toBottomOf="@+id/review_content" />
     *
     * @param view  The TextView reference
     * @param model The ReviewModel instance
     */
    @BindingAdapter({"textToWebLink"})
    public static void setLinkable(final TextView view, ReviewModel model) {
        String label = model.getProgressive();
        String reviewUrl = model.getReviewUrl();
        String anchorHtml = "<a href=\"" + reviewUrl + "\">" + label + "</a>";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            view.setText(Html.fromHtml(anchorHtml));
        } else {
            view.setText(Html.fromHtml(anchorHtml, Html.FROM_HTML_MODE_COMPACT));
        }
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
