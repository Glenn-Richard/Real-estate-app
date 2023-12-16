package com.example.realestatemanager.repository;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.realestatemanager.api.ApiClient;
import com.example.realestatemanager.api.PlacesApi;
import com.example.realestatemanager.api.PlacesResponse;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.PlaceTypes;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitRepository {
    MutableLiveData<PlacesResponse> predictions = new MutableLiveData<>();

    private static PlacesApi placesApi = null;

    public RetrofitRepository(){
        placesApi = ApiClient.getInterface();
    }

    public LiveData<PlacesResponse> getPredictions(String request){
        Call<PlacesResponse> call = placesApi.getPlaces(request, "AIzaSyByHmbyuuAfqa-qU3zpO5erVNhcynyx4XY");
        assert call != null;
    call.enqueue(new Callback<PlacesResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlacesResponse> call, @NonNull Response<PlacesResponse> response) {
                if (response.isSuccessful()) {
                    PlacesResponse placesResponse = response.body();
                    if (placesResponse != null) {
                        predictions.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlacesResponse> call, @NonNull Throwable t) {
                Log.e("ERROR MAPS", t.getLocalizedMessage());
            }
        });
        return predictions;
    }
    public static void initializationPLacesClient(Context context,double lat, double lng, String query){

        PlacesClient placesClient = Places.createClient(context);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setTypesFilter(Collections.singletonList(PlaceTypes.ADDRESS))
                .setLocationBias(RectangularBounds.newInstance(
                        new LatLng(lat, lng),
                        new LatLng(lat, lng)))
                .setLocationRestriction(RectangularBounds.newInstance(
                        new LatLng(lat, lng),
                        new LatLng(lat, lng)))
                .setSessionToken(token)
                .setQuery(query)
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
