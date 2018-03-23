package com.example.dima.wallpaper.database.dataSource;

import com.example.dima.wallpaper.database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Dima on 21.03.2018.
 */

public class RecentRepository implements IRecentsDataSource {
    private IRecentsDataSource mLocalDataSource;
    private static RecentRepository instance;

    public RecentRepository(IRecentsDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    public static RecentRepository getInstance(IRecentsDataSource localDataSource) {
        if(instance == null)
        {
            instance = new RecentRepository(localDataSource);
        }
        return instance;
    }
    @Override
    public Flowable<List<Recents>> getAllRecents() {
        return mLocalDataSource.getAllRecents();
    }

    @Override
    public void insertRecents(Recents... recents) {
        mLocalDataSource.insertRecents(recents);
    }

    @Override
    public void updateRecents(Recents... recents) {
        mLocalDataSource.updateRecents(recents);
    }

    @Override
    public void deleteRecents(Recents... recents) {
        mLocalDataSource.deleteRecents(recents);
    }

    @Override
    public void deleteAllrecents() {
        mLocalDataSource.deleteAllrecents();
    }
}
