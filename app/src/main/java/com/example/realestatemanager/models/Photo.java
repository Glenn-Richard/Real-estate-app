package com.example.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "photo",
        indices = {@Index("propertyId")},
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

    public Photo(long id, String url, String description, long propertyId) {
        this.id = id;
        this.url = url;
        this.description = description;
        this.propertyId = propertyId;
    }

    @Ignore
    public Photo() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }
}
