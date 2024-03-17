package com.example.realestatemanager.models;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "property")
public class Property implements Serializable {
        @PrimaryKey(autoGenerate = true)
        private int id;
        private String title;
        private String description;
        private String price;
        private String surface;
        private String rooms;
        private String bedrooms;
        private String bathrooms;
        private AddressLoc addressLoc;
        private String agent;
        private String availableDate;
        private List<String> imageUrls = new ArrayList<>();
        private List<String> pointsOfInterest = new ArrayList<>();
        private String dateAdded;
        private String hasSchoolNearby;
        private String hasShoppingNearby;
        private String status;
        private Date marketDate;
        private Date soldDate;

        public Property() {
        }

        public Property(int id, String title, String description, String price, String surface, String rooms, String bedrooms, String bathrooms, AddressLoc addressLoc, String agent, String availableDate, List<String> imageUrls, List<String> pointsOfInterest, String dateAdded, String hasSchoolNearby, String hasShoppingNearby, String status, Date marketDate, Date soldDate) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.price = price;
            this.surface = surface;
            this.rooms = rooms;
            this.bedrooms = bedrooms;
            this.bathrooms = bathrooms;
            this.addressLoc = addressLoc;
            this.agent = agent;
            this.availableDate = availableDate;
            this.imageUrls = imageUrls;
            this.pointsOfInterest = pointsOfInterest;
            this.dateAdded = dateAdded;
            this.hasSchoolNearby = hasSchoolNearby;
            this.hasShoppingNearby = hasShoppingNearby;
            this.status = status;
            this.marketDate = marketDate;
            this.soldDate = soldDate;
        }

        public static Property fromContentValues(ContentValues values) {
            Property property = new Property();
            if (values.containsKey("id")) {
                property.setId(values.getAsInteger("id"));
            }

            return property;
        }

        public List<String> getPointsOfInterest() {
            return pointsOfInterest;
        }

        public void setPointsOfInterest(List<String> pointsOfInterest) {
            this.pointsOfInterest = pointsOfInterest;
        }

        public Date getSoldDate() {
            return soldDate;
        }

        public void setSoldDate(Date soldDate) {
            this.soldDate = soldDate;
        }

        public String getDateAdded() {
            return dateAdded;
        }

        public void setDateAdded(String dateAdded) {
            this.dateAdded = dateAdded;
        }

        public Date getMarketDate() {
            return marketDate;
        }

        public void setMarketDate(Date marketDate) {
            this.marketDate = marketDate;
        }


        public String getHasSchoolNearby() {
            return hasSchoolNearby;
        }

        public void setHasSchoolNearby(String hasSchoolNearby) {
            this.hasSchoolNearby = hasSchoolNearby;
        }

        public String getHasShoppingNearby() {
            return hasShoppingNearby;
        }

        public void setHasShoppingNearby(String hasShoppingNearby) {
            this.hasShoppingNearby = hasShoppingNearby;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSurface() {
            return surface;
        }

        public void setSurface(String surface) {
            this.surface = surface;
        }

        public String getRooms() {
            return rooms;
        }

        public void setRooms(String rooms) {
            this.rooms = rooms;
        }

        public String getBedrooms() {
            return bedrooms;
        }

        public void setBedrooms(String bedrooms) {
            this.bedrooms = bedrooms;
        }

        public String getBathrooms() {
            return bathrooms;
        }

        public void setBathrooms(String bathrooms) {
            this.bathrooms = bathrooms;
        }

        public AddressLoc getAddressLoc() {
            return addressLoc;
        }

        public void setAddressLoc(AddressLoc addressLoc) {
            this.addressLoc = addressLoc;
        }

        public String getAgent() {
            return agent;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }

        public String getAvailableDate() {
            return availableDate;
        }

        public void setAvailableDate(String availableDate) {
            this.availableDate = availableDate;
        }



        public List<String> getImageUrls() {
            return imageUrls;
        }

        public void setImageUrls(List<String> imageUrls) {
            this.imageUrls = imageUrls;
        }

        public boolean hasSchoolNearby() {
            return this.pointsOfInterest.contains("School");
        }

        public boolean hasShoppingNearby() {
            return this.pointsOfInterest.contains("Shopping");
        }

        public boolean hasTransportNearby() {
            return this.pointsOfInterest.contains("Transport");
        }

        public boolean hasPoolNearby() {
            return this.pointsOfInterest.contains("Swimming Pool");
        }

        public void addPointOfInterest(String pointOfInterest) {
            if (!this.pointsOfInterest.contains(pointOfInterest)) {
                this.pointsOfInterest.add(pointOfInterest);
            }
        }

        public void removePointOfInterest(String pointOfInterest) {
            this.pointsOfInterest.remove(pointOfInterest);
        }



        @NonNull
        @Override
        public String toString() {
            return "RealEstate{" +
                    "id=" + id +
                    '}';
        }

    }
