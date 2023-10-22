package com.example.realestatemanager.models;

import java.util.List;

public class Property {

    private String id;
    private String type;
    private String name;
    private String location;
    private int surface;
    private String description;
    private int number_of_rooms;
    private int number_of_bedrooms;
    private List<String> images;

    public Property(String id, String type, String name, String location, int surface, String description, int number_of_rooms, int number_of_bedrooms, List<String> images) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.location = location;
        this.surface = surface;
        this.description = description;
        this.number_of_rooms = number_of_rooms;
        this.number_of_bedrooms = number_of_bedrooms;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSurface() {
        return surface;
    }

    public void setSurface(int surface) {
        this.surface = surface;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumber_of_rooms() {
        return number_of_rooms;
    }

    public void setNumber_of_rooms(int number_of_rooms) {
        this.number_of_rooms = number_of_rooms;
    }

    public int getNumber_of_bedrooms() {
        return number_of_bedrooms;
    }

    public void setNumber_of_bedrooms(int number_of_bedrooms) {
        this.number_of_bedrooms = number_of_bedrooms;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
