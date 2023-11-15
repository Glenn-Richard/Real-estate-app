package com.example.realestatemanager.models;

import java.io.Serializable;
import java.util.List;

public class Property implements Serializable {

    private String id;
    private String type;
    private int price;
    private String location;
    private int surface;
    private String description;
    private int number_of_rooms;
    private int number_of_bedrooms;
    private List<Photo> images;

    public Property(String id, String type, String location, int price, int surface, String description, int number_of_rooms, int number_of_bedrooms, List<Photo> images) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.price = price;
        this.surface = surface;
        this.description = description;
        this.number_of_rooms = number_of_rooms;
        this.number_of_bedrooms = number_of_bedrooms;
        this.images = images;
    }

    public Property() {
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

    public List<Photo> getImages() {
        return images;
    }

    public void setImages(List<Photo> images) {
        this.images = images;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
