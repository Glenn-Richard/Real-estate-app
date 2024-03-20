package com.example.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PointsOfInterest")
public class PointsOfInterest {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private long propertyId;

    public PointsOfInterest() {
    }

    public PointsOfInterest(int id, String type, long propertyId) {
        this.id = id;
        this.type = type;
        this.propertyId = propertyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }
}
