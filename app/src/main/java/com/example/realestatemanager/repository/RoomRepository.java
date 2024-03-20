package com.example.realestatemanager.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.realestatemanager.db.PhotoDao;
import com.example.realestatemanager.db.PointsOfInterestDao;
import com.example.realestatemanager.db.PropertyDao;
import com.example.realestatemanager.db.RoomDB;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.PointsOfInterest;
import com.example.realestatemanager.models.Property;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RoomRepository {

    private final PropertyDao propertyDao;
    private final PhotoDao photoDao;
    private final PointsOfInterestDao interestDao;
    private final Executor executor;

    public RoomRepository(Context context) {
        RoomDB db = RoomDB.getInstance(context);
        propertyDao = db.getAllProperties();
        photoDao = db.getAllPhotos();
        interestDao = db.getAllPoi();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Property>> getAllProperties() {
        return propertyDao.getAllProperties();
    }

    public LiveData<List<Photo>> getAllPhotos() {
        return photoDao.getAllPhotos();
    }

    public LiveData<List<PointsOfInterest>> getAllPoi(){
        return interestDao.getAllPoi();
    }

    public LiveData<Long> insertProperty(Property property) {
        MutableLiveData<Long> resultId = new MutableLiveData<>();
        executor.execute(() -> {
            long id = propertyDao.insert(property);
            resultId.postValue(id);
        });
        return resultId;
    }

    public void insertPhoto(Photo photo) {
        MutableLiveData<Long> resultId = new MutableLiveData<>();
        executor.execute(() -> {
            long id = photoDao.insert(photo);
            resultId.postValue(id);
        });
    }

    public void insertPoi(PointsOfInterest pointsOfInterest){
        MutableLiveData<Long> resultId = new MutableLiveData<>();
        executor.execute(() -> {
            long id = interestDao.insert(pointsOfInterest);
            resultId.postValue(id);
        });
    }

    public void insertPhotos(List<Photo> photos) {
        MutableLiveData<List<Long>> resultId = new MutableLiveData<>();
        executor.execute(() -> {
            List<Long> ids = photoDao.insertPhotos(photos);
            resultId.postValue(ids);
        });
    }

    public LiveData<Property> getProperty(int id) {
        return propertyDao.getRealEstate(id);
    }

    public LiveData<List<Photo>> getPhotosById(int id) {
        return photoDao.getPhotosById(id);
    }

    public void update(Property property) {
        MutableLiveData<Long> resultId = new MutableLiveData<>();
        executor.execute(() -> {
            long id = propertyDao.update(property);
            resultId.postValue(id);
        });
    }

    public void deleteProperty(Property property) {
        executor.execute(() -> propertyDao.delete(property));
    }

    public void deletePhoto(Photo photo) {
        executor.execute(() -> photoDao.delete(photo));
    }

    public void deletePoi(PointsOfInterest pointsOfInterest){
        executor.execute(() -> interestDao.delete(pointsOfInterest));
    }
}
