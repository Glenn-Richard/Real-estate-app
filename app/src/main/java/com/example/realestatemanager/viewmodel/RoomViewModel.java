package com.example.realestatemanager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.repository.RoomRepository;

import java.util.List;

public class RoomViewModel extends AndroidViewModel {

    private final RoomRepository roomRepository;

    public RoomViewModel(@NonNull Application application) {
        super(application);
        roomRepository = new RoomRepository(application.getApplicationContext());
    }

    public LiveData<List<Property>> getAllProperties(){
        return roomRepository.getAllProperties();
    }

    public void insertProperty(Property property){
        roomRepository.insertProperty(property);
    }

    public LiveData<Property> getProperty(int id){
        return roomRepository.getProperty(id);
    }
    public void deleteProperty(Property property){
        roomRepository.deleteProperty(property);
    }

    public void updateProperty(Property property){
        roomRepository.update(property);
    }
}

