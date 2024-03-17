package com.example.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "photo",
        foreignKeys = @ForeignKey(
        entity = Property.class,
        parentColumns = "id",
        childColumns = "propertyId"))
public class Photo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String url;
    private String description;
    private long propertyId;

    public Photo(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public Photo() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPropertyId() {
        return propertyId;
    }
    @Ignore
    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }
}
