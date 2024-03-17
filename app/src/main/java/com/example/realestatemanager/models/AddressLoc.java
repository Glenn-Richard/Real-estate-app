package com.example.realestatemanager.models;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class AddressLoc {
    private int id;
    private LatLng latLng;
    private String addressLabel;

    public AddressLoc() {
    }

    public AddressLoc(int id, LatLng latLng, String addressLabel) {
        this.id = id;
        this.latLng = latLng;
        this.addressLabel = addressLabel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getAddressLabel() {
        return addressLabel;
    }

    public void setAddressLabel(String addressLabel) {
        this.addressLabel = addressLabel;
    }

    @NonNull
    @Override
    public String toString() {
        return "AddressLoc{" +
                "id=" + id +
                ", latLng=" + latLng +
                ", addressLabel='" + addressLabel + '\'' +
                '}';
    }

}
