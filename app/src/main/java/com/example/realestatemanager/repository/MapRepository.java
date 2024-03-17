package com.example.realestatemanager.repository;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Collections;

public class MapRepository {

    void PlacesRepository(){}
    public void initializationPLacesClient(double lat, double lng, Context context){

        if (!Places.isInitialized()){
            Places.initialize(context, "AIzaSyBhBe93XEn4olaUwiB8X_o1kWvmX4mwOG4");
        }

        PlacesClient placesClient = Places.createClient(context);
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setTypesFilter(Collections.singletonList("restaurant"))
                .setLocationBias(RectangularBounds.newInstance(
                        new LatLng(lat, lng),
                        new LatLng(lat, lng)))
                .setLocationRestriction(RectangularBounds.newInstance(
                        new LatLng(lat, lng),
                        new LatLng(lat, lng)))
                .build();

        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener((response) -> {
                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        Log.d("PREDICTION", prediction.getPlaceId());
                        Log.d("PREDICTION", prediction.getPrimaryText(null).toString());
                    }})
                .addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                });
    }
}
