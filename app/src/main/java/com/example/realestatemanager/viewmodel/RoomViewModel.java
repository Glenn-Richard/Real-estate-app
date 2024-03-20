package com.example.realestatemanager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.PointsOfInterest;
import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.repository.RoomRepository;

import java.util.List;

public class RoomViewModel extends AndroidViewModel {

    private final RoomRepository roomRepository;

    public RoomViewModel(@NonNull Application application) {
        super(application);
        roomRepository = new RoomRepository(application.getApplicationContext());
    }

    public LiveData<List<Property>> getAllProperties() {
        return roomRepository.getAllProperties();
    }

    public LiveData<List<PointsOfInterest>> getAllPoi(){return roomRepository.getAllPoi();}

    public LiveData<List<Photo>> getPhotosById(int id) {
        return roomRepository.getPhotosById(id);
    }

    public LiveData<Long> insertProperty(Property property) {
        return roomRepository.insertProperty(property);
    }

    public void insertPhoto(Photo photo) {
        roomRepository.insertPhoto(photo);
    }

    public void insertPhotos(List<Photo> photos) {
        roomRepository.insertPhotos(photos);
    }

    public void insertPoi(PointsOfInterest pointsOfInterest){
        roomRepository.insertPoi(pointsOfInterest);
    }

    public LiveData<Property> getProperty(int id) {
        return roomRepository.getProperty(id);
    }

    public void deleteProperty(Property property) {
        roomRepository.deleteProperty(property);
    }

    public void deletePhoto(Photo photo) {
        roomRepository.deletePhoto(photo);
    }

    public void deletePoi(PointsOfInterest pointsOfInterest){roomRepository.deletePoi(pointsOfInterest);}

    public void updateProperty(Property property) {
        roomRepository.update(property);
    }
}

