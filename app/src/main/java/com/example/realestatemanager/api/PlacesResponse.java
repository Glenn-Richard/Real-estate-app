package com.example.realestatemanager.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesResponse {
    @SerializedName("predictions")
    private List<Prediction> predictions;

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public static class Prediction {
        @SerializedName("description")
        private String description;

        @SerializedName("place_id")
        private String placeId;

        public String getDescription() {
            return description;
        }

        public String getPlaceId() {
            return placeId;
        }
    }
}


