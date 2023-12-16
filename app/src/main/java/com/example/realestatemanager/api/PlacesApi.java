package com.example.realestatemanager.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApi {
    @GET("place/autocomplete/json")
    Call<PlacesResponse> getPlaces(
            @Query("input") String input,
            @Query("key") String apiKey
    );
}
