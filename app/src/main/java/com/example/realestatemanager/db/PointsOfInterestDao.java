package com.example.realestatemanager.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.realestatemanager.models.PointsOfInterest;
import com.example.realestatemanager.models.Property;

import java.util.List;

@Dao
public interface PointsOfInterestDao {
    @Query("SELECT * FROM PointsOfInterest")
    LiveData<List<PointsOfInterest>> getAllPoi();

    @Query("SELECT * FROM property WHERE id IN (SELECT propertyId FROM pointsofinterest WHERE type IN (:pointTypes))")

    LiveData<List<Property>> searchPoi(List<String> pointTypes);

    @Insert
    long insert(PointsOfInterest pointsOfInterest);

    @Update
    int update(PointsOfInterest pointsOfInterest);

    @Delete
    void delete(PointsOfInterest pointsOfInterest);

}
