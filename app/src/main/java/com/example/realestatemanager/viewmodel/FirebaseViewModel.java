package com.example.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.repository.FirebaseRepository;

import java.util.List;

public class FirebaseViewModel extends ViewModel {

    private final FirebaseRepository firebaseRepository = new FirebaseRepository();

    public void setProperty(Property property){
        firebaseRepository.setProperty(property);
    }
    public LiveData<List<Property>> getProperties(){
        firebaseRepository.getProperties();
        return firebaseRepository.properties;
    }
}

