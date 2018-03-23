package com.example.dima.wallpaper.database.dataSource;

import com.example.dima.wallpaper.database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Dima on 21.03.2018.
 */

public interface IRecentsDataSource {
     Flowable<List<Recents>> getAllRecents();
     void insertRecents(Recents... recents);
     void updateRecents(Recents... recents);
     void deleteRecents(Recents... recents);
     void deleteAllrecents();
}
