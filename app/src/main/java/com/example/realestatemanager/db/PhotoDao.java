package com.example.realestatemanager.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.realestatemanager.models.Photo;

import java.util.List;

@Dao
public interface PhotoDao {

    @Insert
    long insert(Photo photo);

    @Insert
    List<Long> insertPhotos(List<Photo> photos);

    @Query("SELECT * FROM photo WHERE id = :photoId")
    LiveData<List<Photo>> getPhotosById(int photoId);

    @Query("SELECT * FROM photo")
    LiveData<List<Photo>> getAllPhotos();

    @Delete
    void delete(Photo photo);
}

