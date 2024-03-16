package com.example.realestatemanager.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.realestatemanager.db.PropertyDao;
import com.example.realestatemanager.db.RoomDB;
import com.example.realestatemanager.models.Property;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RoomRepository {

    private final PropertyDao dao;

    private final Executor executor;

    public RoomRepository(Context context) {
        RoomDB db = RoomDB.getInstance(context);
        dao = db.getAllProperties();
        this.executor = Executors.newSingleThreadExecutor();
    }
    public LiveData<List<Property>> getAllProperties() {
        return dao.getAllProperties();
    }

    public void insertProperty(Property property) {
        MutableLiveData<Long> resultId = new MutableLiveData<>();
        executor.execute(() -> {
            long id = dao.insert(property);
            resultId.postValue(id);
        });
    }

    public LiveData<Property> getProperty(int id) {
        return dao.getRealEstate(id);
    }

    public void update(Property property) {
       MutableLiveData<Long> resultId = new MutableLiveData<>();
       executor.execute(() -> {
           long id = dao.update(property);
           resultId.postValue(id);
       });
    }

    public void deleteProperty(Property property) {
        executor.execute(() -> dao.delete(property));

    }


}
