package com.example.dima.wallpaper.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dima.wallpaper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyPopularFragment extends Fragment {
    private static DailyPopularFragment sDailyPopularFragment = null;

    public static DailyPopularFragment getInstance() {
        if (sDailyPopularFragment == null) {
            sDailyPopularFragment = new DailyPopularFragment();
        }
        return sDailyPopularFragment;
    }

    public DailyPopularFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_popular, container, false);
    }

}
