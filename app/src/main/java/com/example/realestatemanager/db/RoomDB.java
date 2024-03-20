package com.example.realestatemanager.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.realestatemanager.models.Converters;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.PointsOfInterest;
import com.example.realestatemanager.models.Property;

@Database(entities = {Property.class, Photo.class, PointsOfInterest.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB instance;
    private static RoomDB TEST_INSTANCE;

    public static synchronized RoomDB getInstance(Context context) {

        if (TEST_INSTANCE != null) {
            return TEST_INSTANCE;
        }

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDB.class, "realty_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract PropertyDao getAllProperties();

    public abstract PhotoDao getAllPhotos();

    public abstract PointsOfInterestDao getAllPoi();

    public static void setTestInstance(RoomDB testInstance) {
        TEST_INSTANCE = testInstance;
    }
    public static RoomDB createInMemoryDatabase(Context context) {
        return Room.inMemoryDatabaseBuilder(context.getApplicationContext(), RoomDB.class)
                .allowMainThreadQueries()
                .build();
    }
}
