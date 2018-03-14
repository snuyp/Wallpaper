package com.example.dima.wallpaper.adapter;

/**
 * Created by Dima on 13.03.2018.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.dima.wallpaper.fragment.CategoryFragment;
import com.example.dima.wallpaper.fragment.DailyPopularFragment;
import com.example.dima.wallpaper.fragment.RecentsFragment;

public class WallpaperFragmentAdapter extends FragmentPagerAdapter {
    private Context context;

    public WallpaperFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return CategoryFragment.getInstance();
        } else if (position == 1) {
            return DailyPopularFragment.getInstance();
        } else if (position == 2) {
            return RecentsFragment.getInstance();
        } else return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Category";
            case 1:
                return "Recents";
            case 2:
                return "Daily Popular";
            case 3:
                return "Recents";
        }
        return "";
    }
}
