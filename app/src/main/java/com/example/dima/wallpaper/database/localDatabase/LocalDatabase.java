package com.example.dima.wallpaper.database.localDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.dima.wallpaper.database.Recents;

import static com.example.dima.wallpaper.database.localDatabase.LocalDatabase.DATABASE_VERSION;

/**
 * Created by Dima on 21.03.2018.
 */
@Database(entities = Recents.class, version = DATABASE_VERSION)
public abstract class LocalDatabase extends RoomDatabase{
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME="Wallpaper";

    public abstract RecentsDAO recentsDAO();

    private static LocalDatabase instance;

    public static LocalDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context, LocalDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
