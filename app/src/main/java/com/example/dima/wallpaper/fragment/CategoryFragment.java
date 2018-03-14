package com.example.dima.wallpaper.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dima.wallpaper.R;

public class CategoryFragment extends Fragment {
    private static CategoryFragment sCategoryFragment = null;

    public CategoryFragment() {
    }

    public static CategoryFragment getInstance() {
        if (sCategoryFragment == null) {
            sCategoryFragment = new CategoryFragment();
        }
        return sCategoryFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

}
