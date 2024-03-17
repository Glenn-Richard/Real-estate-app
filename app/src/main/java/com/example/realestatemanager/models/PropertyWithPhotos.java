package com.example.realestatemanager.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PropertyWithPhotos {

    @Embedded
    public Property property;

    @Relation(parentColumn = "id", entityColumn = "propertyId")
    public List<Photo> photos;

    public PropertyWithPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
