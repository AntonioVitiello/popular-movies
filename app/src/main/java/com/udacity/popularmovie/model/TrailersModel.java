package com.udacity.popularmovie.model;

import com.udacity.popularmovie.MainApplication;
import com.udacity.popularmovie.R;
import com.udacity.popularmovie.net.json.trailers.TmdbTrailers;

import java.util.List;

/**
 * Created by Antonio on 23/02/2018.
 */

public class TrailersModel {
    private final String trailerDefaultName = MainApplication.getStringResource(R.string.trailer_default_name);
    private final int trailerNameMaxLen = MainApplication.getIntResource(R.integer.max_trailer_name_length);

    private int trailerCount;
    private String[] name;
    private String[] videoId;
    private String[] trailerThumb;

    public TrailersModel(List<TmdbTrailers> results) {
        trailerCount = results.size();
        name = new String[trailerCount];
        videoId = new String[trailerCount];
        trailerThumb = new String[trailerCount];
        for (int i = 0; i < results.size(); i++) {
            TmdbTrailers trailer = results.get(i);
            name[i] = trailer.getName();
            if (name[i] == null || name[i].isEmpty()) {
                name[i] = trailerDefaultName;
            } else if (name[i].length() > trailerNameMaxLen) {
                name[i] = name[i].substring(0, trailerNameMaxLen) + "..";
            }
            videoId[i] = trailer.getKey();
            trailerThumb[i] = MainApplication.getStringResource(R.string.trailer_thumbnail_url, videoId[i]);
        }
    }

    public int getTrailerCount() {
        return trailerCount;
    }

    public String[] getName() {
        return name;
    }

    public String[] getVideoId() {
        return videoId;
    }

    public String[] getTrailerThumb() {
        return trailerThumb;
    }
}
