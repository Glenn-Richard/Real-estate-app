package com.example.realestatemanager.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.repository.MapRepository;
import com.example.realestatemanager.repository.RoomRepository;

import java.util.List;

public class MapViewModel extends AndroidViewModel {
    private final LiveData<Boolean> locationPermissionGranted;
    private final RoomRepository realEstateRepository;
    private LiveData<List<Property>> realtyList;
    private MapRepository mapRepository = new MapRepository();

    public MapViewModel(@NonNull Application application) {
        super(application);
        realEstateRepository = new RoomRepository(application);
        locationPermissionGranted = new MutableLiveData<>();
        loadRealtyList();
    }

    public LiveData<Boolean> getLocationPermissionGranted() {
        return locationPermissionGranted;
    }

    public void updateLocationPermissionStatus(boolean isGranted) {
        ((MutableLiveData<Boolean>) locationPermissionGranted).setValue(isGranted);
    }

    public LiveData<List<Property>> getRealtyList() {
        return realtyList;
    }

    private void loadRealtyList() {
        realtyList = realEstateRepository.getAllProperties();
    }

    public void initializePlace(double lat, double lng, Context context){
        mapRepository.initializationPLacesClient(lat,lng,context);
    }
}