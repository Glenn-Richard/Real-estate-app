package com.example.realestatemanager.db;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.realestatemanager.models.Property;

import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM property")
    LiveData<List<Property>> getAllProperties();

    @Query("SELECT * FROM property WHERE id = :id")
    LiveData<Property> getRealEstate(int id);

    @Query("SELECT * FROM property")
    Cursor getItemsWithCursor();

    @Insert
    long insert(Property property);

    @Update
    int update(Property property);

    @Query("DELETE FROM property WHERE id = :id")
    int deleteById(long id);

    @Delete
    void delete(Property property);

    @Query("SELECT * FROM property WHERE (:minSurface IS NULL OR surface >= :minSurface) AND (:maxSurface IS NULL OR surface <= :maxSurface) AND (:minPrice IS NULL OR price >= :minPrice) AND (:maxPrice IS NULL OR price <= :maxPrice) AND (:minRooms IS NULL OR rooms >= :minRooms) AND (:maxRooms IS NULL OR rooms <= :maxRooms) AND (:startDate IS NULL OR marketDate >= :startDate) AND (:endDate IS NULL OR marketDate <= :endDate)")
    LiveData<List<Property>> searchProperties(Integer minSurface, Integer maxSurface, Integer minPrice, Integer maxPrice, Integer minRooms, Integer maxRooms, Long startDate, Long endDate);


}
