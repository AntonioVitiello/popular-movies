package com.udacity.popularmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.udacity.popularmovie.model.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 20/02/2018.
 */

public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = "MainActivityFragment";
    private List<Result> mResults = new ArrayList<>();
    private PostersAdapter mPosterAdapter;
    private GridView mGridView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = rootView.findViewById(R.id.posters_grid);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                launchDetailActivity(position);
            }
        });

        return rootView;
    }

    private void launchDetailActivity(int position) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.KEY_TMDB_RESULT, mResults.get(position));
        startActivity(intent);
    }


    public void updateData(List<Result> results) {
        mResults = results;
        if (mPosterAdapter == null) {
            mPosterAdapter = new PostersAdapter(getActivity(), mResults);
            if (mGridView != null) {
                mGridView.setAdapter(mPosterAdapter);
            }
            mGridView.smoothScrollToPosition(0);
        } else {
            mPosterAdapter.updateData(mResults);
        }
    }
}
