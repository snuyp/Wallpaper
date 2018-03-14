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
public class RecentsFragment extends Fragment {

    private static RecentsFragment sRecentsFragment = null;

    public static RecentsFragment getInstance()
    {
        if(sRecentsFragment == null)
        {
            sRecentsFragment = new RecentsFragment();
        }
        return sRecentsFragment;
    }
    public RecentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recents, container, false);
    }

}
