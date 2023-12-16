package com.example.realestatemanager.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.realestatemanager.api.PlacesResponse;
import com.example.realestatemanager.repository.RetrofitRepository;

public class RetrofitViewModel extends ViewModel {


    private final RetrofitRepository retrofitRepository = new RetrofitRepository();

    public LiveData<PlacesResponse> predictions(String request){
        return retrofitRepository.getPredictions(request);
    }
    public void initializePlaces(Context context,double lat,double lng,String query){
        RetrofitRepository.initializationPLacesClient(context,lat,lng,query);
    }

}
